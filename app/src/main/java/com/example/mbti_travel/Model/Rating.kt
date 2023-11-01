package com.example.mbti_travel.Model

data class Rating(
    val ratingScore: String,        // 평가점수
    val targetTrip: String,         // 평가타겟 (Trip의 ID)
    val review: String,             // 평가내용
    val reviewerName: String,      // 평가자명
    val mbti :String,               // 평가자 mbti
    val userId: String              // 평가자 아이디
) {
    // 파이어스토어에서 객체를 생성할 때 기본 생성자가 필요하므로 비어있는 기본 생성자를 추가
    constructor() : this("", "", "", "", "", "")
}
