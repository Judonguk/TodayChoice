package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.EntryRepository

class EntryViewModel : ViewModel() {

    private val repository: EntryRepository = EntryRepository() // Repository를 직접 생성

    private val _isSaveSuccessful = MutableLiveData<Boolean>()
    val isSaveSuccessful: LiveData<Boolean> get() = _isSaveSuccessful

    /**
     * 데이터를 저장하는 함수
     */
    fun saveEntry(question: String, option1: String, option2: String) {
        repository.postEntry(question, option1, option2,
            onSuccess = { _isSaveSuccessful.value = true },
            onFailure = { _isSaveSuccessful.value = false }
        )
    }
}
