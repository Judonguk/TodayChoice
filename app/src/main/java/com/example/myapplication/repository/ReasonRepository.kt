package com.example.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Reason
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ReasonRepository {
    val database = Firebase.database
    val userRef = database.getReference("reason1")

    fun observeReason(reason: MutableLiveData<List<Reason>>){ //뷰모델에서 모델의 데이터 참조
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reasonList = mutableListOf<Reason>()
                for (childSnapshot in snapshot.children) {
                    val reason = childSnapshot.getValue(Reason::class.java)
                    if (reason != null) {
                        val key = childSnapshot.key ?: ""
                        val name = key.split("_").firstOrNull() ?: ""
                        reason.userId = name
                        reasonList.add(reason)
                    }
                }
                reason.postValue(reasonList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    val rateRef = database.getReference("choiceRate")

    fun observeRate(rate: MutableLiveData<Int>){
        rateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rate.postValue(snapshot.value?.toString()?.toInt() ?: 0) //toString()했다가 toInt()하는게 맞는걸까.. 암튼 된다 야호!
                //rate.postValue(snapshot.value as Int)  이건 왜 안될까..
            }

            override fun onCancelled(error: DatabaseError) {
                //에러 처리
            }
        })
    }

    fun postRate(newValue: Int){ //모델의 데이터 변경
        rateRef.setValue(newValue)
    }
}