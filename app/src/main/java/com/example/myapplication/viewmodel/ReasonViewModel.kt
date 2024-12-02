package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.ReasonRepository

class ReasonViewModel: ViewModel() {
    private val repository = ReasonRepository()

    private val _reason = MutableLiveData<String>("선택한 이유") //내부적으로는 바꿀 수 있지만
    val reason : LiveData<String> get() = _reason //밖에 공개할 때는 바꿀 수 없는 데이터로 지정

    private val _choiceRate = MutableLiveData<Int>(10)
    val choiceRate : LiveData<Int> get() = _choiceRate

    init {
        repository.observeReason(_reason)
        repository.observeRate(_choiceRate) //2. 모델에서 데이터를 옵저브해서 뷰모델의 데이터 업데이트
    }
    ////
    fun choiceRatePlus(){
        val newRate = _choiceRate.value!! + 1
        repository.postRate(newRate) //1. 뷰모델의 데이터를 직접 건들지 않고, 모델의 데이터만 업데이트
    }

    fun choiceRateMinus(){
        val newRate = _choiceRate.value!! - 1
        repository.postRate(newRate)
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