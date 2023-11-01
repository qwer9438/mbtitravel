package com.example.mbti_travel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_travel.Model.Board

class BoardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val board = intent.getSerializableExtra("selectedBoard") as Board

        // UI 요소를 findViewById를 사용하여 참조합니다.
        val tvTitle: TextView = findViewById(R.id.tv_board_detail_Title)
        val tvContent: TextView = findViewById(R.id.tv_board_detail_Content)
        val tvAuthor: TextView = findViewById(R.id.tv_board_detail_Author)

        // 게시물 정보를 UI에 설정합니다.
        tvTitle.text = board.title
        tvContent.text = board.content
        tvAuthor.text = board.author
        // ... 기타 필요한 정보 설정
    }
}
