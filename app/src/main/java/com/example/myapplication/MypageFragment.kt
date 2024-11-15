package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMypageBinding
import com.example.myapplication.viewmodel.HotViewModel

class MypageFragment: Fragment() {

    private val advise  = arrayOf(
        User("ㅇㅇㅇ님","남기","떠나기"),
        User("ㅁㅁㅁ님","짜장","짬뽕"),
        User("ㅂㅂㅂ님","옷","바지"),
        User("ㅅㅅㅅ님","취업","대학원"),
        User("ㅋㅋㅋ님","아이폰","갤럭시"),
        User("ㅈㅈㅈ님","고백","고백X"),
        )

    private val viewModel: HotViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMypageBinding.inflate(inflater,container,false)
        binding.recAdviseChoice.layoutManager = LinearLayoutManager(requireContext())
        binding.recAdviseChoice.adapter=UsersAdapter(advise, showImageAndName = true,viewModel)

        binding.recMyChoice.layoutManager = LinearLayoutManager(requireContext())
        binding.recMyChoice.adapter=UsersAdapter(advise, showImageAndName = false,viewModel)
        fun changeFragment(frag: Fragment) {
            //Fragment 설정
            parentFragmentManager.beginTransaction().run {
                replace(R.id.frm_frag, frag)
                commit()
            }
        }
        binding.run{
            homeButton.setOnClickListener {
                changeFragment(MainpageFragment())
            }
        }
        return binding.root

    }

























/*
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
 */
}