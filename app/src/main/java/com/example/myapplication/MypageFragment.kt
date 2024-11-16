package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMypageBinding
import com.example.myapplication.viewmodel.HotViewModel

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    // ViewModel 초기화 (Activity 범위에서 공유)
    private val viewModel: HotViewModel by activityViewModels()

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

        setupRecyclerViews()
        observeViewModel()
        setupListeners()

        return binding.root
    }



    //RecyclerView 초기화

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


    //ViewModel의 데이터를 관찰하여 RecyclerView 업데이트

    private fun observeViewModel() {
        // 모든 정보가 포함된 데이터를 관찰
        viewModel.users.observe(viewLifecycleOwner) { users ->
            fullInformationAdapter.updateUsers(users)
        }

        // 제한된 정보를 관찰
        viewModel.decisionsWithoutInfo.observe(viewLifecycleOwner) { users ->
            noInformationAdapter.updateUsers(users)
        }
    }


    //클릭 이벤트 등 Listener 설정

    private fun setupListeners() {
        binding.homeButton.setOnClickListener {
            navigateToFragment(MainpageFragment())
        }
    }


     //다른 Fragment로 전환
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frm_frag, fragment)
            addToBackStack(null) // 뒤로 가기 스택 추가
            commit()
        }
    }

    //메모리 누수를 방지하기 위해 View 관련 리소스 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}