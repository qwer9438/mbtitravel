package com.example.mbti_travel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mbti_travel.Model.Board
import com.example.mbti_travel.Util.Util
import com.example.mbti_travel.fragment.BoardFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BoardWriteActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        val editTitle = findViewById<EditText>(R.id.editTitle)
        val editContent = findViewById<EditText>(R.id.editContent)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        btnCancel.setOnClickListener {
            finish()
        }

        btnConfirm.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val content = editContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val util = Util()
            val author = util.loadFromSharedPreferences(this, "nick")
            val currentTime = Calendar.getInstance().timeInMillis

            val board = Board(title, content, author, currentTime)

            val db = FirebaseFirestore.getInstance()
            db.collection("board")
                .add(board)
                .addOnSuccessListener {
                    Toast.makeText(this, "게시글이 성공적으로 작성되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@BoardWriteActivity, MainActivity::class.java)
                    intent.putExtra("selectPage","board")
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "게시글 작성에 실패했습니다. ${e.message}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@BoardWriteActivity, MainActivity::class.java)
                    intent.putExtra("selectPage","board")
                    startActivity(intent)
                    finish()
                }
        }
    }
}