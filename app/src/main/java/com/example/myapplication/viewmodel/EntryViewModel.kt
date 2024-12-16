package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.User
import com.example.myapplication.repository.UserRepository

class EntryViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val _isSaveSuccessful = MutableLiveData<Boolean>()
    val isSaveSuccessful: LiveData<Boolean> get() = _isSaveSuccessful

    fun saveEntry(question: String, option1: String, option2: String) {
        val user = User(
            name = "스테판",
            first_Choice = option1,
            second_Choice = option2,
            viewCount = 0,
        )

        userRepository.postUser(user)
        _isSaveSuccessful.value = true
    }
    fun resetSaveStatus(){
        _isSaveSuccessful.value = false
    }
}