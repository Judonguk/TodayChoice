
// HotViewModel.kt: 뷰 모델 정의
package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.myapplication.User

class HotViewModel : ViewModel() {

    // User 목록 관리 (초기값 설정)
    private val _users = MutableLiveData<List<User>>().apply {
        value = listOf(
            User("김철수", "남기", "떠나기",0),
            User("신짱구", "짜장", "짬뽕",0),
            User("이유리", "옷", "바지",0),
            User("맹구", "취업", "대학원",0),
            User("이훈이", "아이폰", "갤럭시",0),
            User("신짱아", "고백", "고백X",0),
            User("신형만", "내과", "외과",0),
            User("봉미선", "빨래", "화장실 청소",0),
        User("두목님", "유치원", "교사",0),
        )
    }
    val users: LiveData<List<User>>
        get() = _users

    // UI에서 필요한 데이터를 가공하여 전달
    val decisionsWithoutInfo: LiveData<List<User>> = _users.map { userList ->
        userList.map { it.copyForUI() }
    }

    /**
     * 특정 User의 조회수를 증가시키는 메서드
     * position 조회수를 증가시킬 User의 위치
     */
    fun incrementViewCountForUser(position: Int) {
        _users.value = _users.value?.mapIndexed { index, user ->
            if (index == position) {
                user.incrementViewCount()
                Log.d("HotViewModel", "User: ${user.name}, New ViewCount: ${user.viewCount}")
                user
            } else {
                user
            }
        }
    }


    /**
     * 조회수 순으로 데이터 정렬
     */
    fun sortUsersByViewCount() {
        _users.value = _users.value?.sortedByDescending { it.viewCount }
    }
}