package com.example.myapplication.repository

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

/**
 * 사용자 데이터를 Firebase Realtime Database와 동기화하는 Repository 클래스
 */
class UserRepository {
    // Firebase Database 인스턴스 초기화
    private val database = Firebase.database
    // 'user' 노드에 대한 참조 생성
    private val userRef = database.getReference("user")

    /**
     * Firebase Database의 사용자 데이터를 관찰하고 변경사항을 LiveData에 반영
     *
     * @param user 사용자 목록을 저장할 MutableLiveData 객체
     */
    fun observeUser(user: MutableLiveData<List<User>>) {
        userRef.addValueEventListener(object : ValueEventListener {
            // 데이터가 변경될 때마다 호출되는 콜백
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                // snapshot의 모든 자식 노드를 순회
                for (childSnapshot in snapshot.children) {
                    // 각 자식 노드의 데이터를 User 객체로 변환
                    val user = childSnapshot.getValue(User::class.java)
                    if (user != null) {
                        // 데이터베이스의 키를 사용자 이름으로 설정
                        user.name = childSnapshot.key ?: ""
                        userList.add(user)
                    }
                }
                // LiveData를 통해 새로운 사용자 목록을 UI에 전달
                user.postValue(userList)
            }

            // 데이터 읽기가 취소되었을 때 호출되는 콜백
            override fun onCancelled(error: DatabaseError) {
                // 에러 처리 로직을 추가할 수 있음
            }
        })
    }

    /**
     * 새로운 사용자 데이터를 Firebase Database에 추가
     *
     * @param user 추가할 사용자 객체
     */
    fun postUser(user: User) {
        // 사용자 이름을 키로 사용하여 데이터 저장
        userRef.child(user.name).setValue(user)
    }
}