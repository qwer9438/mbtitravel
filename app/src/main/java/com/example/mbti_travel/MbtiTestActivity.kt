package com.example.mbti_travel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import com.example.mbti_travel.Util.Util
import com.google.firebase.firestore.FirebaseFirestore


class MbtiTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbtiexam)

        if (savedInstanceState == null) {
            val fragment = QuestionFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("알림")
            .setMessage("본 MBTI 테스트는 \n 다소 낮은 정확도를 가지고 있습니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id->

                })
        builder.show()
    }

    fun showResult(mbtiType: String, email: String, plat: String) {
        val resultFragment = ResultFragment.newInstance(mbtiType, email, plat)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultFragment)
            .addToBackStack(null)
            .commit()
    }

}


class QuestionFragment : Fragment() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: ImageView

    private val questions = listOf(
        "1. 내가 주변 사람들과 함께 시간을 보내는 것을 좋아한다.",
        "2. 계획을 세우는 것을 좋아한다.",
        "3. 새로운 사람들과 어울리는 것을 즐긴다.",
        "4. 감정적인 상황에 민감하게 반응한다.",
        "5. 주기적으로 새로운 친구를 만든다..",
        "6. 일이 잘못될 때를 대비해 여러 대비책을 세우는 편이다.",
        "7. 자유 시간 중 상당 부분을 다양한 관심사를 탐구하는 데 할애한다.",
        "8. 다른 사람이 울고 있는 모습을 보면 자신도 울고 싶어질 때가 많다."
    )

    private val options = listOf(
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다"),
        listOf("a. 그렇다", "b. 아니다")
    )

    private var currentQuestionIndex = 0
    private val answers = mutableListOf<Int>()
    lateinit var questionTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_questionfragment, container, false)

        questionTextView = view.findViewById(R.id.textView2)
        radioGroup = view.findViewById(R.id.radioGroup)
        nextButton = view.findViewById(R.id.imageView2)

        nextButton.setOnClickListener {
            val selectedOptionIndex = radioGroup.indexOfChild(
                view.findViewById<RadioButton>(
                    radioGroup.checkedRadioButtonId
                )
            )
            answers.add(selectedOptionIndex)

            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                showQuestion()
            } else {
                val activity = activity as MbtiTestActivity
                val util = Util()
                val email = util.loadFromSharedPreferences(requireContext(),"email")
                val plat = util.loadFromSharedPreferences(requireContext(),"plat")
                if (email != null) {
                    if (plat != null) {
                        activity.showResult(calculateMbtiType(),email,plat)
                    }
                }
            }
        }

        showQuestion()

        return view
    }

    private fun showQuestion() {
        questionTextView.text = questions[currentQuestionIndex]
        radioGroup.removeAllViews()

        for (option in options[currentQuestionIndex]) {
            val radioButton = RadioButton(requireContext())
            radioButton.text = option
            radioGroup.addView(radioButton)
        }
    }

    private fun calculateMbtiType(): String {
        val mbtiTypes = listOf(
            "ESTP", "ESFP", "ENTP", "ENFP",
            "ESTJ", "ESFJ", "ENTJ", "ENFJ",
            "ISTP", "ISFP", "INTP", "INFP",
            "ISTJ", "ISFJ", "INTJ", "INFJ"
        )

        val mbtiTypeCount = answers.size / 4
        val typeCountMap = mutableMapOf<String, Int>()

        for (i in 0 until mbtiTypeCount) {
            val index = i * 4
            val typeIndex = answers.subList(index, index + 4).joinToString("").toInt(2)
            val type = mbtiTypes[typeIndex]
            typeCountMap[type] = typeCountMap.getOrDefault(type, 0) + 1
        }

        val mostCommonType = typeCountMap.maxByOrNull { it.value }?.key ?: ""
        return mostCommonType
    }
}

class ResultFragment : Fragment() {

    companion object {
        private const val ARG_MBTI_TYPE = "mbti_type"
        private const val ARG_EMAIL = "email"
        private const val ARG_PLAT = "plat"

        fun newInstance(mbtiType: String, email: String, plat: String): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(ARG_MBTI_TYPE, mbtiType)
            args.putString(ARG_EMAIL, email)
            args.putString(ARG_PLAT, plat)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mbtiType: String
    private lateinit var email: String
    private lateinit var plat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mbtiType = it.getString(ARG_MBTI_TYPE, "")
            email = it.getString(ARG_EMAIL, "")
            plat = it.getString(ARG_PLAT, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resultfragment, container, false)
        val resultTextView: TextView = view.findViewById(R.id.tv_sub)
        val home: ImageView = view.findViewById(R.id.btn_home)
        resultTextView.text = "당신의 MBTI 유형은 $mbtiType 입니다."
        home.setOnClickListener {
            // Firebase에 MBTI 유형 업데이트
            val db = FirebaseFirestore.getInstance()
            db.collection("User")
                .whereEqualTo("email", email)
                .whereEqualTo("plat", plat)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("User").document(document.id)
                            .update("mbti", mbtiType)
                    }
                    var util = Util()
                    context?.let { it1 -> util.saveToSharedPreferences(it1, "mbti", mbtiType) }
                }
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }




        return view
    }
}