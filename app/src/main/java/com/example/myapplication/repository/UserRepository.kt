package com.example.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

/**
 * 사용자 데이터를 Firebase Realtime Database와 동기화하는 Repository 클래스
 */
class UserRepository {
    private val database = Firebase.database
    private val userRef = database.getReference("user")

    fun observeUser(user: MutableLiveData<List<User>>) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (childSnapshot in snapshot.children) {
                    val user = childSnapshot.getValue(User::class.java)
                    if (user != null) {
                        val key = childSnapshot.key ?: ""
                        val name = key.split("_").firstOrNull() ?: ""
                        user.name = name
                        userList.add(user)
                    }
                }
                user.postValue(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }

    fun postUser(user: User) {
        val timestamp = System.currentTimeMillis()
        val key = "${user.name}_$timestamp"
        userRef.child(key).setValue(user)
    }

    fun updateViewCount(user: User) {
        userRef.get().addOnSuccessListener { snapshot ->
            for (childSnapshot in snapshot.children) {
                val currentUser = childSnapshot.getValue(User::class.java)
                if (currentUser != null &&
                    currentUser.first_Choice == user.first_Choice &&
                    currentUser.second_Choice == user.second_Choice) {
                    val key = childSnapshot.key ?: continue
                    val updates = hashMapOf<String, Any>(
                        "viewCount" to user.viewCount
                    )
                    userRef.child(key).updateChildren(updates)
                    break
                }
            }
        }
    }
}