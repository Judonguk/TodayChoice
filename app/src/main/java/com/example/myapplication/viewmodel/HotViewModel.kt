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

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private var isViewCountSorted = false
    private val repository = UserRepository()

    init {
        val tempLiveData = MutableLiveData<List<User>>()
        tempLiveData.observeForever { newUsers ->
            newUsers?.let { users ->
                if (isViewCountSorted) {
                    val sortedUsers = users.sortedByDescending { it.viewCount }
                    _users.value = sortedUsers
                } else {
                    _users.value = users
                }
            }
        }
        repository.observeUser(tempLiveData)
    }

    fun incrementViewCountForUser(position: Int) {
        val currentList = _users.value.orEmpty()
        if (position in currentList.indices) {
            val user = currentList[position]
            val updatedUser = user.copy(viewCount = user.viewCount + 1)
            repository.updateViewCount(updatedUser)
            Log.d("HotViewModel", "User: ${user.name}, New ViewCount: ${updatedUser.viewCount}")
        }
    }

    fun sortUsersByViewCount() {
        isViewCountSorted = true
        _users.value?.let { currentList ->
            _users.value = currentList.sortedByDescending { it.viewCount }
        }
    }

    fun updateProfileImage(url: String) {
        _profileImageUrl.value = url
    }

    fun loadProfileImage() {
        val prefs = getApplication<Application>().getSharedPreferences("profile", Context.MODE_PRIVATE)
        prefs.getString("profileImageUrl", null)?.let {
            _profileImageUrl.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}