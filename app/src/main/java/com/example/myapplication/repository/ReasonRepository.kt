package com.example.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ReasonRepository {
    val database = Firebase.database

    val userRef = database.getReference("user")

    fun observeReason(reason: MutableLiveData<String>){ //뷰모델에서 모델의 데이터 참조
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reason.postValue(snapshot.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    val rateRef = database.getReference("choiceRate")

    fun observeRate(rate: MutableLiveData<Int>){
        rateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rate.postValue(snapshot.value.toString().toInt()) //toString()했다가 toInt()하는게 맞는걸까.. 암튼 된다 야호!
                //rate.postValue(snapshot.value as Int)  이건 왜 안될까..
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun postRate(newValue: Int){ //모델의 데이터 변경
        rateRef.setValue(newValue)
    }
}