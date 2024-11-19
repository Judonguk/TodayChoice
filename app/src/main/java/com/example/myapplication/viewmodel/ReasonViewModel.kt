package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReasonViewModel: ViewModel() {
    private val _reason = MutableLiveData<String>("선택한 이유")
    val reason : LiveData<String> get() = _reason
}