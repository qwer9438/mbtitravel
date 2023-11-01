package com.example.mbti_travel.Dialog

import android.app.Dialog
import android.view.Window
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_travel.databinding.MbtiSelectDialogBinding
import android.widget.Button
import android.widget.Spinner
import com.example.mbti_travel.R
import android.content.Intent
import com.example.mbti_travel.*

class Mbti_dialog(private val context: AppCompatActivity) {
    private lateinit var spinner: Spinner
    private lateinit var btn_ok: Button
    private lateinit var btn_test: Button

    private lateinit var binding: MbtiSelectDialogBinding
    private val dlg = Dialog(context) // 부모 액티비티의 context 가 들어감

    fun show(content: String) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
        dlg.setContentView(R.layout.mbti_select_dialog) // 다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false) // 다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        // 스피너와 버튼 초기화
        spinner = dlg.findViewById(R.id.sp_select_mbti_dialog)
        btn_ok = dlg.findViewById(R.id.btn_submit_mbti_dialog)
        btn_test = dlg.findViewById(R.id.btn_mbti_test_mbti_dialog)

        // 스피너 어댑터 설정
        val mbtiTypes = listOf(
            "ESTP", "ESFP", "ENTP", "ENFP",
            "ESTJ", "ESFJ", "ENTJ", "ENFJ",
            "ISTP", "ISFP", "INTP", "INFP",
            "ISTJ", "ISFJ", "INTJ", "INFJ"
        )
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item, // 드롭다운 목록에서 각 항목의 레이아웃
            mbtiTypes
        )


        spinner.adapter = adapter

        spinner.setSelection(1) // 시작 위치를 지정


        btn_ok.setOnClickListener {
            dlg.dismiss()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        btn_test.setOnClickListener {
            dlg.dismiss()
            val intent = Intent(context, MbtiTestActivity::class.java)
            context.startActivity(intent)

        }

        dlg.show()
    }
}
