package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentEntryBinding
import androidx.appcompat.app.AppCompatActivity

class EntryFragment : Fragment() {

    // View Binding 객체 선언
    private var _binding: FragmentEntryBinding? = null
    private val binding: FragmentEntryBinding
        get() = _binding ?: throw IllegalStateException("ViewBinding is not initialized")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View Binding 초기화
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 시 동작 설정
        binding.EntryBtn.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        // TextView에 입력된 질문 가져오기
        val question = binding.tvQuestion.text.toString().trim()

        // RadioButton의 텍스트로 옵션 가져오기
        val option1 = binding.rbOption1.text.toString().trim()
        val option2 = binding.rbOption2.text.toString().trim()

        // 입력 값 유효성 검사
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // SharedPreferences에 데이터 저장
        saveVotingData(question, option1, option2)

        // 입력 필드 초기화
        clearFields()
    }

    private fun saveVotingData(question: String, option1: String, option2: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("votingData", AppCompatActivity.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("question", question)
            putString("option1", option1)
            putString("option2", option2)
            apply()
        }
        Toast.makeText(requireContext(), "투표가 등록되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        // TextView의 텍스트 초기화
        binding.tvQuestion.text = ""

        // RadioButton의 텍스트 초기화
        binding.rbOption1.text = ""
        binding.rbOption2.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지하기 위해 Binding 해제
    }
}
