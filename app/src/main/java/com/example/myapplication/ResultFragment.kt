package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import com.example.myapplication.viewmodel.ReasonViewModel
import com.google.firebase.database.FirebaseDatabase

class ResultFragment : Fragment() {

    val viewModel: ReasonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false) // 먼저 inflate

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        // 버튼 초기화
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)

        val database = FirebaseDatabase.getInstance()
        val buttonCountsRef = database.getReference("buttonCounts")

        // 초기화 (앱 실행 시 1회만 실행 필요)
        buttonCountsRef.setValue(mapOf("button1Count" to 0, "button2Count" to 0))

        progressBar.progress = 50

        button3.setOnClickListener {
            progressBar.progress += 10
//            buttonCountsRef.child("button1Count").get().addOnSuccessListener { snapshot ->
//                val currentCount = snapshot.getValue(Int::class.java) ?: 0
//                buttonCountsRef.child("button1Count").setValue(currentCount + 1)
//            }
//
//            // ProgressBar 업데이트
//            buttonCountsRef.child("button1Count").get().addOnSuccessListener { snapshot ->
//                val currentCount = snapshot.getValue(Int::class.java) ?: 0
//                progressBar.progress = currentCount // ProgressBar 값 설정
//            }
        }

        button4.setOnClickListener {
            buttonCountsRef.child("button2Count").get().addOnSuccessListener { snapshot ->
                val currentCount = snapshot.getValue(Int::class.java) ?: 0
                buttonCountsRef.child("button2Count").setValue(currentCount + 1)
            }

            // ProgressBar 업데이트
            buttonCountsRef.child("button2Count").get().addOnSuccessListener { snapshot ->
                val currentCount = snapshot.getValue(Int::class.java) ?: 0
                progressBar.progress = currentCount // ProgressBar 값 설정
            }
        }



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
        }
}