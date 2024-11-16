// User.kt: User 클래스 정의 (캡슐화)
package com.example.myapplication

data class User(
    val name: String,
    val first_Choice: String,
    val second_Choice: String,
    private var _viewCount: Int = 0 // 조회수는 내부에서만 관리
) {
    val viewCount: Int
        get() = _viewCount

    fun incrementViewCount() {
        _viewCount++
    }

    fun copyForUI(): User {
        return this.copy()
    }
}