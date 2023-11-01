package com.example.mbti_travel.Model
class MBTI {
    var ESTP: Int = 0
    var ESFP: Int = 0
    var ENTP: Int = 0
    var ENFP: Int = 0
    var ESTJ: Int = 0
    var ESFJ: Int = 0
    var ENTJ: Int = 0
    var ENFJ: Int = 0
    var ISTP: Int = 0
    var ISFP: Int = 0
    var INTP: Int = 0
    var INFP: Int = 0
    var ISTJ: Int = 0
    var ISFJ: Int = 0
    var INTJ: Int = 0
    var INFJ: Int = 0

    fun getMaxMBTIValue(): String {
        val map = mapOf(
            "ESTP" to ESTP,
            "ESFP" to ESFP,
            "ENTP" to ENTP,
            "ENFP" to ENFP,
            "ESTJ" to ESTJ,
            "ESFJ" to ESFJ,
            "ENTJ" to ENTJ,
            "ENFJ" to ENFJ,
            "ISTP" to ISTP,
            "ISFP" to ISFP,
            "INTP" to INTP,
            "INFP" to INFP,
            "ISTJ" to ISTJ,
            "ISFJ" to ISFJ,
            "INTJ" to INTJ,
            "INFJ" to INFJ
        )

        return map.maxByOrNull { it.value }?.key ?: ""
    }

    fun setMBTIValue(key: String, value: Int) {
        when (key) {
            "ESTP" -> ESTP = value
            "ESFP" -> ESFP = value
            "ENTP" -> ENTP = value
            "ENFP" -> ENFP = value
            "ESTJ" -> ESTJ = value
            "ESFJ" -> ESFJ = value
            "ENTJ" -> ENTJ = value
            "ENFJ" -> ENFJ = value
            "ISTP" -> ISTP = value
            "ISFP" -> ISFP = value
            "INTP" -> INTP = value
            "INFP" -> INFP = value
            "ISTJ" -> ISTJ = value
            "ISFJ" -> ISFJ = value
            "INTJ" -> INTJ = value
            "INFJ" -> INFJ = value
            else -> throw IllegalArgumentException("Invalid key: $key")
        }
    }
}
