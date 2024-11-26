    package com.example.myapplication.repository

    import androidx.lifecycle.MutableLiveData
    import com.example.myapplication.User
    import com.google.firebase.Firebase
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.ValueEventListener
    import com.google.firebase.database.database


    class UserRepository {
        private val database = Firebase.database
        private val userRef = database.getReference("user")


        //Firebase 데이터베이스 관찰하고 라이브데이터에 전달
        fun observeUser(user:MutableLiveData<List<User>>){
            userRef.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList = mutableListOf<User>()
                    for (childSnapshot in snapshot.children){
                        val user = childSnapshot.getValue(User::class.java)
                        if(user != null){
                            user.name = childSnapshot.key ?: ""
                            userList.add(user)
                        }
                    }
                    // Firebase에서 가져온 데이터를 원본 순서대로 반영
                    user.postValue(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        fun postUser(user:User){
            userRef.child(user.name).setValue(user)
        }





    }