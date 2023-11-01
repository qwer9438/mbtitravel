package com.example.mbti_travel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.kakao.sdk.user.UserApiClient
import android.util.Log
import android.widget.Toast
import com.example.mbti_travel.Dialog.Mbti_dialog
import com.example.mbti_travel.Util.Util
import com.google.firebase.firestore.FirebaseFirestore
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    private lateinit var ll_kakao_login : LinearLayout
    private lateinit var ll_naver_login : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret , naverClientName)



        ll_kakao_login = findViewById<LinearLayout>(R.id.ll_kakao_login)
        ll_naver_login = findViewById<LinearLayout>(R.id.ll_naver_login)

        ll_kakao_login.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필 링크: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                        val email = user.id.toString()

                        // Firebase Firestore 인스턴스를 가져옵니다.
                        val db = FirebaseFirestore.getInstance()

                        // User 컬렉션에서 로그인된 사용자의 이메일과 플랫폼 정보와 일치하는 문서를 찾습니다.
                        db.collection("User")
                            .whereEqualTo("email", email)
                            .whereEqualTo("plat", "kakao")
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    // 일치하는 문서가 없으면 새 문서를 생성합니다.
                                    val userMap = hashMapOf(
                                        "email" to email,
                                        "plat" to "kakao",
                                        "nick" to "",
                                        "mbti" to ""
                                    )

                                    db.collection("User")
                                        .add(userMap)
                                        .addOnSuccessListener { documentReference ->
                                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                                            // 로그인 값 저장.
                                            val util = Util()
                                            if (email != null) {
                                                util.saveToSharedPreferences(this, "email", email)
                                            }
                                            util.saveToSharedPreferences(this, "plat", "kakao")
                                            util.saveToSharedPreferences(this, "nick", "")

                                            /*val dlg = Mbti_dialog(this)
                                            dlg.show("MBTI TEST")*/

                                            val intent = Intent(this, MbtiTestActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error adding document", e)
                                        }
                                }else{
                                    val document = documents.first()
                                    val mbti = document.getString("mbti") ?: ""
                                    if (mbti.isBlank()) {
                                        val util = Util()
                                        val nick = document.getString("nick") ?: ""
                                        Log.d("nickLOG",mbti)

                                        util.saveToSharedPreferences(this, "email", email)
                                        util.saveToSharedPreferences(this, "plat", "kakao")
                                        util.saveToSharedPreferences(this, "nick", nick)
                                        val intent = Intent(this@LoginActivity, MbtiTestActivity::class.java)
                                        startActivity(intent)
                                        finish()


                                    } else {
                                        // MBTI 값이 이미 있는 경우, 여기에 필요한 동작을 추가
                                        // 예: MainActivity로 이동
                                        val util = Util()
                                        val nick = document.getString("nick") ?: ""
                                        util.saveToSharedPreferences(this, "email", email)
                                        util.saveToSharedPreferences(this, "plat", "kakao")
                                        util.saveToSharedPreferences(this, "nick", nick)
                                        util.saveToSharedPreferences(this, "mbti", mbti)
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting documents: ", exception)
                            }
                    }
                }
            }
        }


        ll_naver_login.setOnClickListener {
            startNaverLogin()
            //val intent = Intent(this,MainActivity::class.java)
            //startActivity(intent)
        }
    }

    /**
     * 로그인
     * authenticate() 메서드를 이용한 로그인 */
    private fun startNaverLogin(){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                //binding.tvResult.text = "id: ${userId} \ntoken: ${naverToken}"
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()

                // Firebase Firestore 인스턴스를 가져옵니다.
                val db = FirebaseFirestore.getInstance()

                // User 컬렉션에서 로그인된 사용자의 이메일과 플랫폼 정보와 일치하는 문서를 찾습니다.
                db.collection("User")
                    .whereEqualTo("email", userId)
                    .whereEqualTo("plat", "naver")
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            // 일치하는 문서가 없으면 새 문서를 생성합니다.
                            val userMap = hashMapOf(
                                "email" to userId,
                                "plat" to "naver",
                                "nick" to "",
                                "mbti" to ""
                            )

                            db.collection("User")
                                .add(userMap)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                                    // 로그인 값 저장.
                                    val util = Util()
                                    if (userId != null) {
                                        util.saveToSharedPreferences(applicationContext, "email", userId)
                                    }
                                    util.saveToSharedPreferences(applicationContext, "plat", "naver")
                                    util.saveToSharedPreferences(applicationContext, "nick", "")

                                    /*val dlg = Mbti_dialog(this)
                                    dlg.show("MBTI TEST")*/

                                    val intent = Intent(applicationContext, MbtiTestActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        }else{
                            val document = documents.first()
                            val mbti = document.getString("mbti") ?: ""
                            if (mbti.isBlank()) {
                                val util = Util()
                                val nick = document.getString("nick") ?: ""
                                Log.d("nickLOG",mbti)

                                if (userId != null) {
                                    util.saveToSharedPreferences(applicationContext, "email", userId)
                                }
                                util.saveToSharedPreferences(applicationContext, "plat", "naver")
                                util.saveToSharedPreferences(applicationContext, "nick", nick)
                                val intent = Intent(this@LoginActivity, MbtiTestActivity::class.java)
                                startActivity(intent)
                                finish()


                            } else {
                                // MBTI 값이 이미 있는 경우, 여기에 필요한 동작을 추가
                                // 예: MainActivity로 이동
                                val util = Util()
                                val nick = document.getString("nick") ?: ""
                                if (userId != null) {
                                    util.saveToSharedPreferences(applicationContext, "email", userId)
                                }
                                util.saveToSharedPreferences(applicationContext, "plat", "naver")
                                util.saveToSharedPreferences(applicationContext, "nick", nick)
                                util.saveToSharedPreferences(applicationContext, "mbti", mbti)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기

                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }
}
