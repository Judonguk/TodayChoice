
// HotViewModel.kt: 뷰 모델 정의
package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.User
import com.example.myapplication.repository.UserRepository

class HotViewModel : ViewModel() {

    // User 목록 관리 (초기값 설정)
    private val _users = MutableLiveData<List<User>>() // 원본 데이터
    val users: LiveData<List<User>>
        get() = _users

    private var sortedUsers : List<User> = listOf()// 정렬된 데이터를 저장하는 변수


    private val repository = UserRepository()
    init{
        // Firebase 에서 사용자 데이터 관찰 시작
        repository.observeUser(_users)
    }

     //특정 User의 조회수를 증가시키는 메서드
     //position 조회수를 증가시킬 User의 위치
    fun incrementViewCountForUser(position: Int) {
        val currentList = _users.value.orEmpty()
        val updatedList = currentList.toMutableList()


        if (position in currentList.indices) {
            val user = currentList[position]
            val updatedUser = user.copy(viewCount = user.viewCount + 1)

            // 리스트 업데이트
            updatedList[position] = updatedUser
            sortedUsers = updatedList// 정렬된 상태로 유지

            // Firebase에 업데이트
            repository.postUser(updatedUser)
            Log.d("HotViewModel", "User: ${user.name}, New ViewCount: ${updatedUser.viewCount}")

            // LiveData 업데이트
            _users.value =sortedUsers
        }
    }


    /**
     * 조회수 순으로 데이터 정렬
     */
    fun sortUsersByViewCount() {
        val currentList = _users.value.orEmpty()
        sortedUsers = currentList.sortedByDescending { it.viewCount }
        _users.value = sortedUsers
    }
}