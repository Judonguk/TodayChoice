import android.content.Context
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.ImageSelectionAdapter
import com.example.myapplication.MainpageFragment
import com.example.myapplication.R
import com.example.myapplication.UsersAdapter
import com.example.myapplication.databinding.FragmentMypageBinding
import com.example.myapplication.viewmodel.HotViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    // ViewModel 초기화 (Activity 범위에서 공유)
    private val viewModel: HotViewModel by activityViewModels()

    // Firebase Storage 참조 초기화
    private val storage = Firebase.storage
    private val storageRef = storage.reference.child("profile_images")

    // Adapter 초기화
    private val fullInformationAdapter by lazy {
        UsersAdapter(emptyList(), showImageAndName = true, viewModel = viewModel)
    }
    private val noInformationAdapter by lazy {
        UsersAdapter(emptyList(), showImageAndName = false, viewModel = viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
        setupListeners()
        viewModel.loadProfileImage() // 프로필 이미지 로드
    }

    // RecyclerView 초기화
    private fun setupRecyclerViews() {
        with(binding) {
            recAdviseChoice.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = fullInformationAdapter
            }
            recMyChoice.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = noInformationAdapter
            }
        }
    }

    // ViewModel의 데이터를 관찰하여 RecyclerView와 프로필 이미지 업데이트
    private fun observeViewModel() {
        // User 데이터 관찰
        viewModel.users.observe(viewLifecycleOwner) { users ->
            // 스테판의 데이터만 필터링
            val MyData = users.filter { it.name == "스테판" }
            val otherData = users.filter { it.name != "스테판" }

            fullInformationAdapter.updateUsers(otherData)
            noInformationAdapter.updateUsers(MyData)
        }

        // 프로필 이미지 URL 관찰
        viewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                binding.profileButton.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    private fun setupListeners() {
        binding.homeButton.setOnClickListener {
            navigateToFragment(MainpageFragment())
        }
        binding.profileButton.setOnClickListener {
            showImageSelectionDialog()
        }
    }

    private fun showImageSelectionDialog() {
        // 커스텀 다이얼로그 레이아웃 생성
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_image_selection, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewImages)

        // 다이얼로그 생성
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("프로필 이미지 선택")
            .setView(dialogView)
            .setNegativeButton("취소", null)
            .create()

        // RecyclerView 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Storage에서 이미지 목록 가져오기
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val imageAdapter = ImageSelectionAdapter(listResult.items) { selectedImageRef ->
                    // 이미지 선택 시 처리
                    showLoading(true)
                    selectedImageRef.downloadUrl.addOnSuccessListener { uri ->
                        updateProfileImageUrl(uri.toString())
                        dialog.dismiss()
                    }
                }
                recyclerView.adapter = imageAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "이미지 목록 로드 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        dialog.show()
    }

    private fun updateProfileImageUrl(imageUrl: String) {
        // SharedPreferences에 URL 저장
        requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)
            .edit()
            .putString("profileImageUrl", imageUrl)
            .apply()

        showLoading(false)
        Toast.makeText(
            requireContext(),
            "프로필 이미지가 업데이트되었습니다.",
            Toast.LENGTH_SHORT
        ).show()

        // ViewModel을 통해 이미지 URL 업데이트
        viewModel.updateProfileImage(imageUrl)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frm_frag, fragment)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
