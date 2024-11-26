// User.kt: User 클래스 정의 (캡슐화)
package com.example.myapplication

data class User(
    var name: String="",
    var first_Choice: String="",
    var second_Choice: String="",
    var viewCount: Int = 0
) {
    fun copyForUI(): User {
        return this.copy()
    }
}