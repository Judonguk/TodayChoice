package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMainpageBinding
import com.example.myapplication.viewmodel.HotViewModel

/**
 * 메인 페이지를 담당하는 Fragment
 * 사용자 목록을 표시하고 프로필, 등록, 정렬 기능을 제공
 */
class MainpageFragment : Fragment() {

    // ViewBinding 객체 (nullable로 선언)
    private var _binding: FragmentMainpageBinding? = null
    // Null-safe한 binding 접근을 위한 위임 프로퍼티
    private val binding get() = _binding!!

    // Activity 범위에서 공유되는 ViewModel 초기화
    private val viewModel: HotViewModel by activityViewModels()

    // Adapter 지연 초기화
    private val adapter by lazy {
        UsersAdapter(emptyList(), showImageAndName = true, viewModel = viewModel)
    }

    /**
     * Fragment의 View를 생성하고 초기화하는 메서드
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding 초기화
        _binding = FragmentMainpageBinding.inflate(inflater, container, false)

        // UI 컴포넌트 초기화 및 설정
        setupRecyclerView()
        observeViewModel()
        setupListners()

        return binding.root
    }

    /**
     * RecyclerView 초기화 및 설정
     */
    private fun setupRecyclerView() {
        binding.recUsers.layoutManager = LinearLayoutManager(context)
        binding.recUsers.adapter = adapter
    }

    /**
     * ViewModel의 데이터 변경을 관찰하여 UI 업데이트
     */
    private fun observeViewModel() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }
    }

    /**
     * 버튼 클릭 이벤트 리스너 설정
     */
    private fun setupListners() {
        // 프로필 버튼 - 마이페이지로 이동
        binding.profileButton.setOnClickListener {
            changeFragment(MypageFragment())
        }

        // 등록 버튼 - 등록 페이지로 이동
        binding.button.setOnClickListener {
            changeFragment(EntryFragment())
        }

        // 정렬 버튼 - 조회수 기준 정렬
        binding.loadingButton.setOnClickListener {
            viewModel.sortUsersByViewCount()
        }
    }


    /**
     * Fragment 전환을 처리하는 메서드
     *
     * @param frag 전환할 Fragment 인스턴스
     */
    private fun changeFragment(frag: Fragment) {
        parentFragmentManager.beginTransaction().run {
            replace(R.id.frm_frag, frag)
            commit()
        }
    }

    /**
     * Fragment View가 제거될 때 호출
     * 메모리 누수 방지를 위해 binding 참조 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 아래 주석 처리된 코드들은 불필요한 코드이므로 제거 권장:
// - companion object와 newInstance는 현재 사용되지 않음
// - 기타 주석 처리된 설명은 이미 위의 코드에 반영됨