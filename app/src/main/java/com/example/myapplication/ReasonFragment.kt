package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentReasonBinding
import com.example.myapplication.viewmodel.ReasonViewModel

class ReasonFragment : Fragment() {

    var binding : FragmentReasonBinding? = null

    inner class recCall{
        fun changeFragment(frag: Fragment) {
            parentFragmentManager.beginTransaction().run {
                replace(R.id.frm_frag, frag)
                commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReasonBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(ReasonViewModel::class.java)

        val link = recCall()

        binding?.recMyChoice?.layoutManager = LinearLayoutManager(context) //어떻게 쌓을건지 설정
        val adapter = ReasonsAdapter(viewModel.reason.value ?: emptyList(), viewModel, null)
        binding?.recMyChoice?.adapter = adapter

        viewModel.reason.observe(viewLifecycleOwner) { reason ->
            if (reason != null) {
                adapter.updateData(reason)
            }
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun changeFragment(frag: Fragment) {
            parentFragmentManager.beginTransaction().run {
                replace(R.id.frm_frag, frag)
                commit()
            }
        }

        binding?.homeButton?.setOnClickListener{
            changeFragment(MainpageFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}