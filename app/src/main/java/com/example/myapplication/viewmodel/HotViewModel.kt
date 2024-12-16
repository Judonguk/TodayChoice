package com.example.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.User
import com.example.myapplication.repository.UserRepository

/**
 * Hot 화면의 데이터를 관리하는 ViewModel 클래스
 * 사용자 목록의 조회수 관리 및 정렬 기능을 제공
 */
class HotViewModel(application: Application) : AndroidViewModel(application) {

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
     */
    fun incrementViewCountForUser(position: Int) {
        val currentList = _users.value.orEmpty()

        if (position in currentList.indices) {
            val user = currentList[position]
            val updatedUser = user.copy(viewCount = user.viewCount + 1)
            repository.postUser(updatedUser)
            Log.d("HotViewModel", "User: ${user.name}, New ViewCount: ${updatedUser.viewCount}")
        }
    }

    /**
     * 사용자 목록을 조회수 기준으로 내림차순 정렬하는 메서드
     */
    fun sortUsersByViewCount() {
        isViewCountSorted = true
        _users.value = _users.value?.sortedByDescending { it.viewCount }
    }

    fun updateProfileImage(url: String) {
        _profileImageUrl.value = url
    }

    // SharedPreferences에서 프로필 이미지 로드
    fun loadProfileImage() {
        val prefs = getApplication<Application>().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val savedUrl = prefs.getString("profileImageUrl", null)
        savedUrl?.let {
            _profileImageUrl.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}