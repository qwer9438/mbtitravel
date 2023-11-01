package com.example.mbti_travel

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import android.util.Log

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "5b90686d8f96870856aed56213c805ae")
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
    }
}