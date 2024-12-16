package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.databinding.FragmentMypageBinding
import com.example.myapplication.viewmodel.HotViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
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
            // RecyclerView 설정
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
            fullInformationAdapter.updateUsers(users)
            noInformationAdapter.updateUsers(users)
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

    // 클릭 이벤트 등 Listener 설정
    private fun setupListeners() {
        binding.homeButton.setOnClickListener {
            navigateToFragment(MainpageFragment())
        }
        // 프로필 버튼 클릭 시 이미지 선택 다이얼로그 표시
        binding.profileButton.setOnClickListener {
            showImageSelectionDialog()
        }
    }

    private fun showImageSelectionDialog() {
        // Storage 참조
        val storageRef = Firebase.storage.reference.child("profile_images")

        // 모든 이미지 목록을 가져옵니다
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                // 이미지 파일 이름 목록 생성
                val items = listResult.items.map { it.name }.toTypedArray()

                // 이미지 선택 다이얼로그 표시
                AlertDialog.Builder(requireContext())
                    .setTitle("프로필 이미지 선택")
                    .setItems(items) { _, which ->
                        showLoading(true)
                        // 선택된 이미지 다운로드 URL 가져오기
                        val selectedImageRef = listResult.items[which]
                        selectedImageRef.downloadUrl.addOnSuccessListener { uri ->
                            // 선택된 이미지 URL을 데이터베이스에 저장
                            updateProfileImageUrl(uri.toString())
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "이미지 목록 로드 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateProfileImageUrl(imageUrl: String) {
        // Firebase Realtime Database의 프로필 이미지 URL 업데이트
        val database = Firebase.database
        val userRef = database.getReference("user")

        userRef.child("스테판").child("profileImageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                showLoading(false)
                Toast.makeText(
                    requireContext(),
                    "프로필 이미지가 업데이트되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                // ViewModel을 통해 이미지 URL 업데이트
                viewModel.updateProfileImage(imageUrl)
            }
            .addOnFailureListener { e ->
                showLoading(false)
                Toast.makeText(
                    requireContext(),
                    "프로필 정보 업데이트 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    // 다른 Fragment로 전환
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frm_frag, fragment)
            commit()
        }
    }

    // 메모리 누수를 방지하기 위해 View 관련 리소스 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}