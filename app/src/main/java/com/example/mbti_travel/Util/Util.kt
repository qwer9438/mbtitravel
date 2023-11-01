package com.example.mbti_travel.Util

import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import com.example.mbti_travel.Model.Trip
import android.content.SharedPreferences

class Util {
    // assets 폴더에서 JSON 파일을 읽어오는 함수
    fun readJsonFromAssets(context: Context, fileName: String): String {
        val jsonString: String
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonString = String(buffer, Charsets.UTF_8)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }
        return jsonString
    }

    // JSON 파일을 파싱하는 함수
    fun parseJsonToTrip(jsonString: String): Trip {
        val gson = Gson()
        return gson.fromJson(jsonString, Trip::class.java)
    }

    // SharedPreferences에 데이터를 저장하는 함수
    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("mbti_travel_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    // SharedPreferences에서 데이터를 불러오는 함수
    fun loadFromSharedPreferences(context: Context, key: String): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("mbti_travel_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }
}