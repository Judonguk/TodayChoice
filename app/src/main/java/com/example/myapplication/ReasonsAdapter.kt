package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListReasonsBinding
import com.example.myapplication.data.Reason
import com.example.myapplication.viewmodel.ReasonViewModel

class ReasonsAdapter(var reasons: List<Reason>,
                     val viewModel: ReasonViewModel,
                     val link: ResultFragment.recCall?)
    : RecyclerView.Adapter<ReasonsAdapter.Holder>() {
        //Adapter가 하는 일: UI렌더링 시 필요한 내용을 달라고 하면 넘겨줌
/*
    //어댑터에도 뷰모델 되는지 실험..
    //val viewModel: ReasonViewModel by activityViewModels()
    //왜 안되지..? -> activityViewModels()는 lifecycle를 알지 못하는 단순 클래스이므로 불가능!

 */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListReasonsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun getItemCount() = reasons.size

    fun updateData(newData: List<Reason>) {
        reasons = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reasons[position])
        //수명 주기에 접근할 수 없는 adapter에서 inner class를 통해 외부에 접근
        link?.let {
            it.giveText(reasons[position].selectedOption)
        }

        holder.itemView.setOnClickListener{
            link?.let {
                it.changeFragment(ReasonFragment())
            }
        }
    }

    class Holder(private val binding: ListReasonsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(reason: Reason){
            binding.txtName.text = reason.userId
            binding.txtReason.text = reason.reason
            binding.txtTest.text = reason.selectedOption
            //binding.txtLike.text = reason.viewcount.toString()
/*
            binding.root.setOnClickListener{
                //Toast.makeText(binding.root.context, "유저: ${reason.name} 추천수: ${reason.viewcount}", Toast.LENGTH_SHORT).show()
            }

 */
        }
    }
}