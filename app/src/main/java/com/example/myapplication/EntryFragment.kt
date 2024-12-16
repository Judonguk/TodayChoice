package com.example.myapplication

import MypageFragment
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentEntryBinding
import com.example.myapplication.viewmodel.EntryViewModel

class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeSaveStatus()
    }

    private fun setupUI() {
        binding.tvQuestion.apply {
            text = "투표 질문을 입력하세요."
            setOnClickListener { showInputDialog("질문 입력", "투표 질문을 입력하세요") { input -> text = input } }
        }

        binding.rbOption1.setOnClickListener { showOptionInputDialog(binding.rbOption1) }
        binding.rbOption2.setOnClickListener { showOptionInputDialog(binding.rbOption2) }

        binding.EntryBtn.setOnClickListener { handleSubmit() }
        binding.profileButton.setOnClickListener { navigateToFragment(MypageFragment()) }
    }

    private fun showOptionInputDialog(radioButton: RadioButton) {
        showInputDialog("옵션 입력", "옵션 내용을 입력하세요") { input -> radioButton.text = input }
    }

    private fun showInputDialog(title: String, hint: String, onInputConfirmed: (String) -> Unit) {
        val inputEditText = EditText(requireContext()).apply { this.hint = hint }

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(inputEditText)
            .setPositiveButton("확인") { _, _ ->
                val inputText = inputEditText.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    onInputConfirmed(inputText)
                } else {
                    Toast.makeText(requireContext(), "$title(를) 입력하세요.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun observeSaveStatus() {
        viewModel.isSaveSuccessful.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                showToast("투표가 등록되었습니다.")
                resetFields()
                viewModel.resetSaveStatus()
                navigateToFragment(MainpageFragment())
            }
        }
    }

    private fun handleSubmit() {
        val question = binding.tvQuestion.text.toString().trim()
        val option1 = binding.rbOption1.text.toString().trim()
        val option2 = binding.rbOption2.text.toString().trim()

        if (listOf(question, option1, option2).any { it.isEmpty() || it == "투표 질문을 입력하세요." }) {
            showToast("모든 필드를 입력하세요.")
        } else {
            confirmSubmission(question, option1, option2)
        }
    }

    private fun confirmSubmission(question: String, option1: String, option2: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("투표 등록 확인")
            .setMessage("내용이 맞습니까?\n\n선택지 1: $option1\n선택지 2: $option2")
            .setPositiveButton("예") { _, _ ->
                viewModel.saveEntry(question, option1, option2)
            }
            .setNegativeButton("아니요", null)
            .show()
    }

    private fun resetFields() {
        binding.tvQuestion.text = "투표 질문을 입력하세요."
        binding.rbOption1.text = ""
        binding.rbOption2.text = ""
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frm_frag, fragment)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}