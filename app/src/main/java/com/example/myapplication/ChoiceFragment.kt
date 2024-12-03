package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentChoiceBinding

class ChoiceFragment : Fragment() {

    // View Binding 변수 선언
    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View Binding 초기화
        _binding = FragmentChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = "대학교 졸업 후에 어떤 것을 할까요?"
        val option1 = "취업"
        val option2 = "대학원"

        // 질문 및 선택지 설정
        binding.tvQuestion.text = question
        binding.rbOption1.text = option1
        binding.rbOption2.text = option2

        binding.profileButton.setOnClickListener {
            changeFragment(MypageFragment())
        }

        // '제출' 버튼 클릭 리스너 설정
        binding.submitBtn.setOnClickListener {
            val selectedOptionId = binding.rgOptions.checkedRadioButtonId
            val reason = binding.reasonEditText.text.toString().trim()

            // 선택지 유효성 검사
            if (selectedOptionId == -1) {
                Toast.makeText(requireContext(), "투표 옵션을 선택하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이유 입력 검사
            if (reason.isEmpty()) {
                Toast.makeText(requireContext(), "투표 이유를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 투표 결과 확인
            val selectedOption = when (selectedOptionId) {
                binding.rbOption1.id -> option1
                binding.rbOption2.id -> option2
                else -> ""
            }

            // 다이얼로그로 확인받기
            AlertDialog.Builder(requireContext())
                .setTitle("투표 확인")
                .setMessage("선택한 옵션: $selectedOption\n투표한이유: $reason\n\n투표하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    Toast.makeText(requireContext(), "투표가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    // 투표 완료 후 메인 페이지로 이동
                    changeFragment(ResultFragment())
                }
                .setNegativeButton("아니요") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    // 다른 Fragment로 전환
    private fun changeFragment(frag: Fragment) {
        parentFragmentManager.beginTransaction().run {
            replace(R.id.frm_frag, frag)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
