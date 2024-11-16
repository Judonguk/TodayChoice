package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListUsersBinding
import com.example.myapplication.viewmodel.HotViewModel

class UsersAdapter(private var users: List<User>,
                   private val showImageAndName: Boolean,
                   private val viewModel:HotViewModel
) : RecyclerView.Adapter<UsersAdapter.Holder>() {


    //viewHolder 생성 (아이템)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListUsersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding,viewModel)
    }

    // viewHolder에 데이터 바인딩

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(users[position],showImageAndName)
    }

    // 아이템 개수 반환

    override fun getItemCount()= users.size


    //데이터 업데이트

    fun updateUsers(newUsers: List<User>) {
        users = newUsers // 기존 데이터를 새로운 데이터로 변경
    }


    //viewHolder 정의

    inner class Holder(private val binding : ListUsersBinding,private val viewModel: HotViewModel): RecyclerView.ViewHolder(binding.root){

        // 데이터를 View에 바인딩

        fun bind(user: User, showImageAndName: Boolean) {
            with(binding) {
                setChoices(user)
                setVisibility(showImageAndName)

                // 클릭 이벤트 설정
                root.setOnClickListener {
                    viewModel.incrementViewCountForUser(adapterPosition)
                }
            }
        }




        //사용자 선택 텍스트 설정

        private fun ListUsersBinding.setChoices(user: User) {
            txtName.text = user.name
            firstChoice.text = user.first_Choice
            secondChoice.text = user.second_Choice
        }



        //View visibility 설정

        private fun ListUsersBinding.setVisibility(showImageAndName: Boolean) {
            if (showImageAndName) {
                txtName.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
            } else {
                txtName.visibility = View.GONE
                imageView.visibility = View.GONE
            }
        }
    }
}