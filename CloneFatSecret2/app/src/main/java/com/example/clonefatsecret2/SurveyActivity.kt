package com.example.clonefatsecret2

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged

class SurveyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        val button: ImageButton = findViewById(R.id.imageButton6)
        val linkToWelcome: TextView = findViewById(R.id.textView46)
        var i: Int = 0
        val question: TextView = findViewById(R.id.textViewQuestionTitle)
        val questionAb: TextView = findViewById(R.id.textViewQuestionAbout)
        val ansViews = listOf(
            findViewById<TextView>(R.id.textViewAnswer1),
            findViewById<TextView>(R.id.textViewAnswer2),
            findViewById<TextView>(R.id.textViewAnswer3),
            findViewById<TextView>(R.id.textViewAnswer4),
            findViewById<TextView>(R.id.textViewAnswer5)
        )
        val layAns: LinearLayout = findViewById(R.id.layoutAnswer)
        val layParam: LinearLayout = findViewById(R.id.layoutEditParam)
        val un: TextView = findViewById(R.id.textViewUnit)
        val edit: EditText = findViewById(R.id.editTextTextParam)
        val ansK = IntArray(6) // Массив ответов фиксированного размера

        fun reset() {
            ansViews.forEach { it.background = null }
        }

        edit.doOnTextChanged { text, _, _, _ ->
            button.isVisible = !text.isNullOrEmpty()
        }

        fun handleButtonClick(delta: Int) {
            i += delta
            when {
                i < 0 -> startActivity(Intent(this, WelcomeActivity::class.java))
                i < 3 -> quest(i, question, questionAb, ansViews, layAns, layParam)
                i in 3..5 -> paramEdit(i, question, questionAb, layAns, layParam, un, edit)
                else -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("KEY", ansK)
                    }
                    startActivity(intent)
                }
            }
        }

        fun setAnswerClickListener(answerView: TextView, index: Int) {
            answerView.setOnClickListener {
                reset()
                click(answerView, button)
                button.setOnClickListener {
                    if (i in ansK.indices) {
                        ansK[i] = index
                    }
                    handleButtonClick(1)
                }
            }
        }

        ansViews.forEachIndexed { index, item ->
            setAnswerClickListener(item, index)
        }

        linkToWelcome.setOnClickListener { handleButtonClick(-1) }
    }

    data class Question(
        val text: String,
        val explanation: String,
        val answers: List<String>
    )

    private val questions = listOf(
        Question(
            text = "Какова ваша цель?",
            explanation = "Мы рассчитаем суточную калорийность в соответствии с вашей целью",
            answers = listOf("Похудеть", "Сохранить вес", "Набрать вес")
        ),
        Question(
            text = "Какого вы пола?",
            explanation = "Мужскому организму требуется больше калорий",
            answers = listOf("Мужчина", "Женщина")
        ),
        Question(
            text = "Опишите свой образ жизни?",
            explanation = "Малоподвижный человек сжигает меньше калорий, чем активный",
            answers = listOf("Сидячий", "Малоактивный", "Активный", "Крайне активный")
        )
    )

    private fun quest(
        i: Int,
        question: TextView,
        questionAb: TextView,
        ansViews: List<TextView>,
        layAns: LinearLayout,
        layParam: LinearLayout
    ) {
        val currentQuestion = questions.getOrNull(i)
        currentQuestion?.let {
            question.text = it.text
            questionAb.text = it.explanation
            ansViews.forEachIndexed { index, textView ->
                textView.text = it.answers.getOrNull(index) ?: ""
                textView.isVisible = index < it.answers.size
            }
            layAns.isVisible = true
            layParam.isGone = true
        }
    }

    data class Parameter(
        val questionText: String,
        val explanation: String,
        val unit: String
    )

    private val parameters = listOf(
        Parameter("Какой у тебя рост?", "Чем вы выше ростом, тем больше калорий требуется вашему организму", "см"),
        Parameter("Какой у тебя вес?", "Чем больше вы весите, тем больше калорий сжигает ваш организм", "кг"),
        Parameter("Сколько тебе лет?", "Необходимое количество калорий зависит от возраста", "лет")
    )

    private fun paramEdit(
        i: Int,
        question: TextView,
        questionAb: TextView,
        layAns: LinearLayout,
        layParam: LinearLayout,
        un: TextView,
        edit: EditText
    ) {
        edit.text = null
        val parameter = parameters.getOrNull(i - 3)
        if (parameter != null) {
            question.text = parameter.questionText
            questionAb.text = parameter.explanation
            un.text = parameter.unit
            layAns.isGone = true
            layParam.isVisible = true
        } else {
            question.text = ""
            questionAb.text = ""
            un.text = ""
            layAns.isGone = true
            layParam.isGone = true
        }
    }

    private fun click(str: TextView, button: ImageButton) {
        str.background = ContextCompat.getDrawable(this, R.drawable.shape_rounded_green)
        button.isVisible = true
    }
}
