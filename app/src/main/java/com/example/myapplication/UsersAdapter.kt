package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListUsersBinding
import com.example.myapplication.viewmodel.HotViewModel

class UsersAdapter(private val users: Array<User>, private val showImageAndName: Boolean, private val viewModel:HotViewModel) : RecyclerView.Adapter<UsersAdapter.Holder>() {

    // 반복되는 뷰 한칸  뷰 홀더 만들기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListUsersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding,viewModel)
    }

    // 렌더링 해줄때 사용
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(users[position],showImageAndName)
    }

    // 뷰안에 들어가는 아이템이 몇개냐?
    override fun getItemCount()= users.size

    class Holder(private val binding : ListUsersBinding,private val viewModel: HotViewModel): RecyclerView.ViewHolder(binding.root){
        fun bind( user:User,showImageAndName: Boolean){
            binding.firstChoice.text = user.first_Choice
            binding.secondChoice.text = user.second_Choice

            if (showImageAndName) {
                binding.txtName.text = user.name
                binding.txtName.visibility = View.VISIBLE
                binding.imageView.visibility = View.VISIBLE
            } else {
                binding.txtName.visibility = View.GONE
                binding.imageView.visibility = View.GONE
            }

            // 아이템 클릭 리스너 설정 - 클릭 시 ViewModel의 조회 수 증가
            binding.root.setOnClickListener {
                viewModel.incrementViewCount() // 조회 수 증가 메서드 호출
            }

        }
    }

}