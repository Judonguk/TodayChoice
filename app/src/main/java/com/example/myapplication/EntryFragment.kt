package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentEntryBinding
import com.example.myapplication.viewmodel.EntryViewModel

class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EntryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EntryViewModel::class.java)

        // 초기 투표 질문 설정
        binding.tvQuestion.text = "투표 질문을 입력하세요."

        // 투표 질문 클릭 시 입력 다이얼로그 실행
        binding.tvQuestion.setOnClickListener { showInputDialogForQuestion() }

        // RadioButton 클릭 시 입력 다이얼로그 실행
        binding.rbOption1.setOnClickListener { showInputDialog(binding.rbOption1) }
        binding.rbOption2.setOnClickListener { showInputDialog(binding.rbOption2) }

        // 버튼 리스너 설정
        binding.EntryBtn.setOnClickListener { handleSubmit() }
        binding.profileButton.setOnClickListener { changeFragment(MypageFragment()) }

        observeSaveStatus()
    }

    // 투표 질문 입력 다이얼로그
    private fun showInputDialogForQuestion() {
        val inputEditText = EditText(requireContext()).apply {
            hint = "투표 질문을 입력하세요"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("질문 입력")
            .setView(inputEditText)
            .setPositiveButton("확인") { _, _ ->
                val inputText = inputEditText.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    binding.tvQuestion.text = inputText // 입력된 텍스트를 투표 질문에 적용
                } else {
                    Toast.makeText(requireContext(), "질문을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 옵션 입력 다이얼로그
    private fun showInputDialog(radioButton: RadioButton) {
        val inputEditText = EditText(requireContext()).apply {
            hint = "옵션 내용을 입력하세요"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("옵션 입력")
            .setView(inputEditText)
            .setPositiveButton("확인") { _, _ ->
                val inputText = inputEditText.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    radioButton.text = inputText // 입력된 텍스트를 해당 RadioButton에 적용
                } else {
                    Toast.makeText(requireContext(), "옵션을 입력하세요.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun observeSaveStatus() {
        viewModel.isSaveSuccessful.observe(viewLifecycleOwner, Observer { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(requireContext(), "투표가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                clearFields()
                changeFragment(MainpageFragment())
            } else {
                Toast.makeText(requireContext(), "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSubmit() {
        val question = binding.tvQuestion.text.toString().trim()
        val option1 = binding.rbOption1.text.toString().trim()
        val option2 = binding.rbOption2.text.toString().trim()

        if (listOf(question, option1, option2).any { it.isEmpty() }) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
        } else {
            showConfirmationDialog(question, option1, option2)
        }
    }

    private fun showConfirmationDialog(question: String, option1: String, option2: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("투표 등록 확인")
            .setMessage("질문: $question\n옵션 1: $option1\n옵션 2: $option2\n등록하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                viewModel.saveEntry(question, option1, option2)
            }
            .setNegativeButton("아니요", null)
            .show()
    }

    private fun clearFields() {
        binding.rbOption1.text = ""
        binding.rbOption2.text = ""
        binding.tvQuestion.text = ""
    }

    private fun changeFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frm_frag, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
