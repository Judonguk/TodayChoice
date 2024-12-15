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

class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInputDialog(binding.tvQuestion, "질문 입력", "질문을 입력하세요.")
        setupInputDialog(binding.rbOption1, "옵션 1 입력", "옵션 1을 입력하세요.")
        setupInputDialog(binding.rbOption2, "옵션 2 입력", "옵션 2를 입력하세요.")

        binding.EntryBtn.setOnClickListener { handleSubmit() }
        binding.profileButton.setOnClickListener { changeFragment(MypageFragment()) }
    }

    private fun setupInputDialog(view: View, title: String, message: String) {
        view.setOnClickListener {
            showInputDialog(title, message) { input ->
                when (view) {
                    binding.tvQuestion -> binding.tvQuestion.text = input
                    binding.rbOption1 -> binding.rbOption1.text = input
                    binding.rbOption2 -> binding.rbOption2.text = input
                }
            }
        }
    }

    private fun handleSubmit() {
        val question = binding.tvQuestion.text.toString()
        val option1 = binding.rbOption1.text.toString()
        val option2 = binding.rbOption2.text.toString()

        if (listOf(question, option1, option2).any { it.isEmpty() }) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
        } else {
            showConfirmationDialog(question, option1, option2)
        }
    }

    private fun showConfirmationDialog(question: String, option1: String, option2: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("투표 등록 확인")
            .setMessage("질문: $question\n옵션 1: $option1\n옵션 2: $option2\n\n등록하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                saveVotingData(question, option1, option2)
                clearFields()
                Toast.makeText(requireContext(), "투표가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                changeFragment(MainpageFragment())
            }
            .setNegativeButton("아니요", null)
            .show()
    }

    private fun saveVotingData(question: String, option1: String, option2: String) {
        requireActivity().getSharedPreferences("votingData",0)
            .edit()
            .apply {
                putString("question", question)
                putString("option1", option1)
                putString("option2", option2)
                apply()
            }
    }

    private fun clearFields() = with(binding) {
        tvQuestion.text = ""
        rbOption1.text = ""
        rbOption2.text = ""
    }

    private fun showInputDialog(title: String, message: String, callback: (String) -> Unit) {
        val inputField = EditText(requireContext()).apply { isSingleLine = true }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setView(inputField)
            .setPositiveButton("확인") { _, _ ->
                val input = inputField.text.toString().trim()
                if (input.isNotEmpty()) callback(input)
                else Toast.makeText(requireContext(), "입력이 비어 있습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun changeFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.frm_frag, fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
