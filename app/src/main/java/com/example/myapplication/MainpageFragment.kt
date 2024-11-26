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

class MainpageFragment : Fragment() {

    private var _binding: FragmentMainpageBinding? = null
    private val binding get() = _binding!!

    // ViewModel 초기화 (Activity 범위 공유)
    private val viewModel: HotViewModel by activityViewModels()

    // Adapter를 by lazy로 초기화
    private val adapter by lazy {
        UsersAdapter(emptyList(), showImageAndName = true, viewModel = viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainpageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    // RecyclerView 초기화
    private fun setupRecyclerView() {
        binding.recUsers.layoutManager = LinearLayoutManager(context)
        binding.recUsers.adapter = adapter
    }

    // ViewModel의 데이터를 관찰하여 UI 업데이트
    private fun observeViewModel() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }
    }

    // 클릭 이벤트 등 Listener 설정
    private fun setupListeners() {
        binding.profileButton.setOnClickListener {
            changeFragment(MypageFragment())
        }

        // 정렬 버튼 클릭 시 정렬된 데이터를 RecyclerView에 반영
        binding.loadingButton.setOnClickListener {
            viewModel.sortUsersByViewCount()
        }
    }

    // 다른 Fragment로 전환
    private fun changeFragment(frag: Fragment) {
        parentFragmentManager.beginTransaction().run {
            replace(R.id.frm_frag, frag)
            commit()
        }
    }

    // 메모리 누수를 방지하기 위해 binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
