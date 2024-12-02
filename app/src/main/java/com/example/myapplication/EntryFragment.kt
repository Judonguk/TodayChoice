package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentEntryBinding
import androidx.appcompat.app.AppCompatActivity

class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding: FragmentEntryBinding
        get() = _binding ?: throw IllegalStateException("ViewBinding is not initialized")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 질문 입력 처리
        binding.tvQuestion.setOnClickListener {
            showInputDialog("질문 입력", "질문을 입력하세요.") { input ->
                binding.tvQuestion.text = input
            }
        }

        // 옵션 1 입력 처리
        binding.rbOption1.setOnClickListener {
            showInputDialog("옵션 1 입력", "옵션 1을 입력하세요.") { input ->
                binding.rbOption1.text = input
            }
        }

        // 옵션 2 입력 처리
        binding.rbOption2.setOnClickListener {
            showInputDialog("옵션 2 입력", "옵션 2를 입력하세요.") { input ->
                binding.rbOption2.text = input
            }
        }

        // 버튼 클릭 시 동작 설정
        binding.EntryBtn.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        val question = binding.tvQuestion.text.toString()
        val option1 = binding.rbOption1.text.toString()
        val option2 = binding.rbOption2.text.toString()

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        saveVotingData(question, option1, option2)
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
        binding.tvQuestion.text = ""
        binding.rbOption1.text = ""
        binding.rbOption2.text = ""
    }

    private fun showInputDialog(title: String, message: String, callback: (String) -> Unit) {
        val inputField = EditText(requireContext()).apply {
            inputType = android.text.InputType.TYPE_CLASS_TEXT // 일반 텍스트 입력 가능
            isSingleLine = true // 한 줄 입력
        }

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setView(inputField)
            .setPositiveButton("확인") { _, _ ->
                val inputText = inputField.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    callback(inputText)
                } else {
                    Toast.makeText(requireContext(), "입력이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun setupListeners() {
        binding.EntryBtn.setOnClickListener {
            navigateToFragment(MainpageFragment())
        }
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
