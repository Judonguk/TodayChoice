package com.example.myapplication

data class User (val name: String,
                 val first_Choice: String,
                 val second_Choice:String,
                 var viewCount: Int = 0 // 각 아이템별 조회수를 저장하는 필드
)