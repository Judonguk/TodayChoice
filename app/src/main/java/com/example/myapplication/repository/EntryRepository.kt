package com.example.myapplication.repository

import com.google.firebase.database.FirebaseDatabase
import com.example.myapplication.data.Entry

class EntryRepository {

    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("entries") // Firebase 경로

    /**
     * 데이터를 Firebase에 저장
     */
    fun postEntry(question: String, option1: String, option2: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val entry = Entry(question, option1, option2)  // EntryData 생성
        val key = reference.push().key // Firebase에서 새로운 key 생성
        if (key != null) {
            reference.child(key).setValue(entry) // Firebase에 저장
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure() }
        } else {
            onFailure()
        }
    }

    /**
     * Firebase 데이터 관찰 (옵션)
     */
    fun observeEntryData(callback: (Entry) -> Unit) {
        reference.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                snapshot.children.forEach { data ->
                    val entry = data.getValue(Entry::class.java)
                    entry?.let { callback(it) }
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                println("Database Error: ${error.message}")
            }
        })
    }
}
