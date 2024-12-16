package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.databinding.ListReasonsBinding
import com.example.myapplication.data.Reason

class ReasonsAdapter(val reasons: Array<Reason>)
    : RecyclerView.Adapter<ReasonsAdapter.Holder>() { //Adapter가 하는 일: UI렌더링 시 필요한 내용을 달라고 하면 넘겨줌

    //어댑터에도 뷰모델 되는지 실험..
    //val viewModel: ReasonViewModel by activityViewModels()
    //왜 안되지..?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListReasonsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    //override fun getItemCount() = reasons.size
    override fun getItemCount() = 7 //둘중 뭘 써야 할지..

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reasons[position])
    }

    class Holder(private val binding: ListReasonsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(reason: Reason){
            binding.txtName.text = reason.name
            binding.txtReason.text = reason.reason
            binding.txtLike.text = reason.like.toString()

            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context, "유저: ${reason.name} 추천수: ${reason.like}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}