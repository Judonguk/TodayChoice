package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReasonViewModel: ViewModel() {
    private val _reason = MutableLiveData<String>("선택한 이유") //내부적으로는 바꿀 수 있지만
    val reason : LiveData<String> get() = _reason //밖에 공개할 때는 바꿀 수 없는 데이터로 지정

    private val _choiceRate = MutableLiveData<Int>(10)
    val choiceRate : LiveData<Int> get() = _choiceRate

    fun choiceRatePlus(){
        _choiceRate.value = (_choiceRate.value ?: 0) + 1
    }

    fun choiceRateMinus(){
        _choiceRate.value = (_choiceRate.value ?: 0) - 1
    }
    /*
    fun choiceRateMinus(){
        _choiceRate.value?.minus(1)
    }
    fun choiceRatePlus(){
        _choiceRate.value?.plus(1)
    }
    */
}