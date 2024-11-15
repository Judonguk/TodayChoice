package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HotViewModel :ViewModel() {
   private val _viewCount= MutableLiveData<Int>(0)
    val viewCount:LiveData<Int> get() = _viewCount

    // 조회 수 증가 메서드
    fun incrementViewCount() {
        _viewCount.value = (_viewCount.value ?: 0) + 1
        Log.d("HotViewModel", "조회수 증가: ${_viewCount.value}") // 로그 출력
    }

}
