plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mbti_travel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mbti_travel"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures  {
        viewBinding  = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {


    
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // 구글 맵
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    // json 데이터 읽기
    implementation("com.google.code.gson:gson:2.8.8")

    // 카카오톡 모듈
    implementation("com.kakao.sdk:v2-all:2.17.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.17.0") // 카카오 로그인
    implementation("com.kakao.sdk:v2-friend:2.17.0")// 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation("com.kakao.sdk:v2-cert:2.17.0")
    
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // 파이어베이스
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")// 카카오 인증서비스
    
    // 글라이더 모듈
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // 이미지
    implementation ("com.squareup.picasso:picasso:2.71828")

    //네이버 로그인
    implementation ("com.navercorp.nid:oauth-jdk8:5.1.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
}