package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentResultBinding
import com.example.myapplication.viewmodel.ReasonViewModel

class ResultFragment : Fragment() {

    var binding : FragmentResultBinding? = null
    val viewModel: ReasonViewModel by activityViewModels() //lifecycle을 fragment가 아닌 activity에 종속시킴

/*
    val reasons = arrayOf(
        Reason("김긍비", "이유는 딱히 없고요", 2),
        Reason("주동욱", "이유는 저도 없고요", 120),
        Reason("이승민", "이유는 저도 없는데요", 99),
        Reason("임재범", "이유는 저는 있는데요", 9),
        Reason("박정현", "이유는 저만 아는데요", 100),
        Reason("김범수", "이유는 저도 모르는데요", 42),
        Reason("엄희찬", "이유는 너만 아는데요", 0)
    )
*/
    var givenText = "취업"
    var givenText2 = "대학원"

    ////////////////////////////// inner class
    inner class recCall{
        fun changeFragment(frag: Fragment) {
            parentFragmentManager.beginTransaction().run {
                replace(R.id.frm_frag, frag)
                commit()
            }
        }
        fun giveText(text: String){
            givenText = text
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false) //view를 inflate하면서 UI와 code를 연결해주는 binding을 가져옴
        val viewModel = ViewModelProvider(this).get(ReasonViewModel::class.java)

        val link = recCall()

        binding?.recReasons?.layoutManager = LinearLayoutManager(context) //어떻게 쌓을건지 설정

        val adapter = ReasonsAdapter(viewModel.reason.value ?: emptyList(), viewModel, link)
        binding?.recReasons?.adapter = adapter //내부에 담을 내용 -강의에서는 임시로 만든 배열 사용. DB에서 어떻게?

        viewModel.reason.observe(viewLifecycleOwner) { reason ->
            if (reason != null) {
                adapter.updateData(reason)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.choiceRate.observe(viewLifecycleOwner){ //viewModel에서 choiceRate 데이터를 observe
            binding?.progressBar?.progress = viewModel.choiceRate.value!! //viewModel의 choiceRate의 value 참조
        }
        viewModel.reason.observe(viewLifecycleOwner){ //viewModel에서 choiceRate 데이터를 observe
            binding?.textView?.text = givenText //viewModel의 choiceRate의 value 참조
            binding?.textView3?.text = givenText2
        }

        //곧 삭제될 친구들 ㅠㅠ
        binding?.button3?.setOnClickListener{
            viewModel.choiceRatePlus() //반대로 UI에서 변경되었을 때 viewModel의 데이터를 수정
        }
        binding?.button4?.setOnClickListener{
            viewModel.choiceRateMinus()
        }
        //ㅠㅠ

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