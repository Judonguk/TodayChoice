package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.User
import com.example.myapplication.databinding.ListUsersBinding
import com.example.myapplication.viewmodel.HotViewModel

/**
 * 사용자 목록을 표시하는 RecyclerView용 Adapter
 *
 * @param users 표시할 사용자 목록
 * @param showImageAndName 이미지와 이름 표시 여부
 * @param viewModel 사용자 데이터 처리를 위한 ViewModel
 */
class UsersAdapter(
    private var users: List<User>,
    private val showImageAndName: Boolean,
    private val viewModel: HotViewModel
) : RecyclerView.Adapter<UsersAdapter.Holder>() {

    /**
     * ViewHolder 생성
     *
     * @param parent 부모 ViewGroup
     * @param viewType 뷰 타입
     * @return 새로운 ViewHolder 인스턴스
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, viewModel)
    }

    /**
     * ViewHolder에 데이터 바인딩
     *
     * @param holder 데이터를 바인딩할 ViewHolder
     * @param position 데이터 위치
     */
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(users[position], showImageAndName)
    }

    /**
     * 전체 아이템 개수 반환
     */
    override fun getItemCount() = users.size

    /**
     * 사용자 목록 업데이트
     *
     * @param newUsers 새로운 사용자 목록
     */
    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged() // 전체 목록 갱신
    }

    /**
     * 개별 사용자 항목을 표시하는 ViewHolder 클래스
     */
    inner class Holder(
        private val binding: ListUsersBinding,
        private val viewModel: HotViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * 데이터를 뷰에 바인딩
         *
         * @param user 표시할 사용자 데이터
         * @param showImageAndName 이미지와 이름 표시 여부
         */
        fun bind(user: User, showImageAndName: Boolean) {
            with(binding) {
                setChoices(user)
                setVisibility(showImageAndName)

                // 항목 클릭 시 해당 사용자의 조회수 증가
                root.setOnClickListener {
                    viewModel.incrementViewCountForUser(adapterPosition)
                }
            }
        }

        /**
         * 사용자의 선택 항목 텍스트 설정
         *
         * @param user 표시할 사용자 데이터
         */
        private fun ListUsersBinding.setChoices(user: User) {
            txtName.text = user.name
            firstChoice.text = user.first_Choice
            secondChoice.text = user.second_Choice
        }

        /**
         * 이미지와 이름의 표시 여부 설정
         *
         * @param showImageAndName true일 경우 이미지와 이름 표시
         */
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