package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.User
import com.example.myapplication.repository.UserRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Hot 화면의 데이터를 관리하는 ViewModel 클래스
 * 사용자 목록의 조회수 관리 및 정렬 기능을 제공
 */
class HotViewModel : ViewModel() {

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> = _profileImageUrl
    // 내부에서 수정 가능한 MutableLiveData
    private val _users = MutableLiveData<List<User>>()
    // 외부에서는 읽기 전용 LiveData로 노출
    val users: LiveData<List<User>>
        get() = _users


    private var isViewCountSorted = false  // 정렬 상태를 추적하는 변수 추가

    // UserRepository 인스턴스 생성
    private val repository = UserRepository()

    // ViewModel 초기화 시 Firebase 데이터 관찰 시작
    init {
        val tempLiveData = MutableLiveData<List<User>>()
        tempLiveData.observeForever { newUsers ->
            if (isViewCountSorted) {
                // 정렬 상태면 정렬된 순서 유지하면서 조회수만 업데이트
                _users.value?.let { currentUsers ->
                    val updatedList = currentUsers.map { currentUser ->
                        newUsers.find { it.name == currentUser.name } ?: currentUser
                    }
                    _users.value = updatedList
                }
            } else {
                // 정렬되지 않은 상태면 Firebase 순서 그대로
                _users.value = newUsers
            }
        }
        repository.observeUser(tempLiveData)
    }

    /**
     * 특정 위치의 사용자 조회수를 증가시키는 메서드
     *
     * @param position 조회수를 증가시킬 사용자의 리스트 내 위치
     */
    fun incrementViewCountForUser(position: Int) {
        val currentList = _users.value.orEmpty()

        // 유효한 위치인지 확인
        if (position in currentList.indices) {
            val user = currentList[position]
            // 조회수가 증가된 새로운 User 객체 생성
            val updatedUser = user.copy(viewCount = user.viewCount + 1)

            // 리스트 내 해당 위치의 데이터 업데이트
            repository.postUser(updatedUser)

            Log.d("HotViewModel", "User: ${user.name}, New ViewCount: ${updatedUser.viewCount}")

        }
    }

    /**
     * 사용자 목록을 조회수 기준으로 내림차순 정렬하는 메서드
     */
    fun sortUsersByViewCount() {
        isViewCountSorted=true
        _users.value = _users.value?.sortedByDescending { it.viewCount }
    }

    fun updateProfileImage(url: String) {
        _profileImageUrl.value = url
    }

    // Firebase에서 프로필 이미지 로드
    fun loadProfileImage() {
        val database = Firebase.database
        val userRef = database.getReference("user")

        userRef.child("스테판").child("profileImageUrl").get()
            .addOnSuccessListener { snapshot ->
                snapshot.value?.toString()?.let { url ->
                    _profileImageUrl.value = url
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
    }

}