package com.example.mbti_travel.Model


import java.io.Serializable

data class Board(
    var title: String? = "",
    var content: String? = "",
    var author: String? = "",
    var time: Long = 0L,
    var id: String? = "" // Firestore document ID
): Serializable