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

    private var _binding: FragmentMainpageBinding?=null
    private val binding get() = _binding!!

    // ViewModel 초기화 (Activity 범위 공유)
    private val viewModel: HotViewModel by activityViewModels() // viewModel 초기화 (FRAGMENT 바뀔떄마다 뷰모델 사라짐)

    // Adapter를 by lazy로 초기화
    private val adapter by lazy {
        UsersAdapter(emptyList(), showImageAndName = true, viewModel = viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainpageBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        setupListners()

        return binding.root
    }

    //recyclerview 초기화

    private fun setupRecyclerView(){
        binding.recUsers.layoutManager = LinearLayoutManager(context)
        binding.recUsers.adapter = adapter
    }

    //viewmodel의 데이터를 관찰하여 UI업데이트

    private fun observeViewModel(){
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.updateUsers(users)
        }

    }

    // 클릭 이벤트 등 listener 설정

    private fun setupListners(){
        binding.profileButton.setOnClickListener {
            changeFragment(MypageFragment())
        }
        binding.loadingButton.setOnClickListener{
            viewModel.sortUsersByViewCount() // 조회수 정렬
        }
    }
    // 다른 Fragment로 전환
    private fun changeFragment(frag: Fragment) {
        parentFragmentManager.beginTransaction().run {
            replace(R.id.frm_frag, frag)
            commit()
        }
    }

    // 메모리 누수 방지를 위해  binding 해제
    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}

















   /* //VIEW MODEL 함수 VAL A: VIEWMODEL()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 함수분리 안됨 함수안에 함수 말안됨
    // FRAGMENT인데 rament -> 메모리 누수 , framgentview( b)

    // FRAGMENT < FRAGMENT VIEW(빨리끝남)

*/





    /*
        companion object {
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                MainpageFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }


     */