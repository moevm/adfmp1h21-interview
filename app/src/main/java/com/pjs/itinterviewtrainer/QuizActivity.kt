package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pjs.itinterviewtrainer.data.Question
import com.pjs.itinterviewtrainer.data.Quiz
import com.pjs.itinterviewtrainer.data.QuizResults
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

enum class SubmitState {
    SUBMIT,
    NEXT
}

class QuizActivity : AppCompatActivity() {
    private lateinit var quiz: Quiz
    private var quizTimer = 60
    private var currentQuestionNumber = 0
    private lateinit var currentQuestion: Question
    private val btnAnswersMap = mapOf(R.id.answer_a to "a", R.id.answer_b to "b", R.id.answer_c to "c", R.id.answer_d to "d")
    private var submitState: SubmitState = SubmitState.SUBMIT
    private var quizResults: QuizResults = QuizResults()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        with(intent) {
            quiz = Json.decodeFromString(getStringExtra("quiz")!!)
            quizTimer = getStringExtra("quizTimer")?.toInt() ?: 60
        }

        quizTitle.text = "${quiz.categories.joinToString { it.name }}: ${quiz.name}"

        setupNextQuestion()

        submit.setOnClickListener {
            when (submitState) {
                SubmitState.SUBMIT -> {
                    if (handleResult()) {
                        submit.text = "Next"
                        submitState = SubmitState.NEXT
                    }
                }
                SubmitState.NEXT -> {
                    setupNextQuestion()
                    submitState = SubmitState.SUBMIT
                    submit.text = "Submit"
                }
            }
        }
    }

    private fun resetViewColors() {
        answer_a.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_a.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_b.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_b.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_c.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_c.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_d.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_d.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
    }

    private fun setupNextQuestion() {
        if (currentQuestionNumber >= quiz.quiestions.size) {
            AlertDialog.Builder(this)
                    .setTitle("Quiz completed")
                    .setMessage("Result: ${quizResults.correct}/${quiz.quiestions.size} correct answers")
                    .setPositiveButton("Ok") { _, _ ->
                        saveResults()
                    }
                    .create()
                    .show()
            return
        }
        resetViewColors()
        currentQuestionNumber += 1
        currentQuestion = quiz.quiestions[currentQuestionNumber - 1]
        questionNumber.text = "Question ${currentQuestionNumber}/${quiz.quiestions.size}"
        questionText.text = currentQuestion.question_text
        answers.clearCheck()
        if (currentQuestion.code_pic != "null") {
            question_pic.visibility = View.VISIBLE
        } else {
            question_pic.visibility = View.GONE
        }

        setAnswer(answer_a, "a")
        setAnswer(answer_b, "b")
        setAnswer(answer_c, "c")
        setAnswer(answer_d, "d")
    }

    private fun setAnswer(b: RadioButton, answerId: String) {
        if (currentQuestion.answers[answerId] != null) {
            b.visibility = View.VISIBLE
            b.text = currentQuestion.answers[answerId]
        } else {
            b.visibility = View.GONE
        }
    }

    private fun handleResult(): Boolean {
        return when (val btnId = answers.checkedRadioButtonId) {
            -1 -> {
                AlertDialog.Builder(this)
                        .setTitle("No answer")
                        .setMessage("Select answer to submit")
                        .setPositiveButton("Ok") { _, _ -> }
                        .create()
                        .show()
                false
            }
            else -> {
                quizResults.questions.add(currentQuestion)
                val answer = btnAnswersMap[btnId]
                val checkedBtn = findViewById<RadioButton>(btnId)
                if (answer == currentQuestion.correct_answer) {
                    quizResults.correct += 1
                    checkedBtn.setTextColor(resources.getColor(R.color.quiz_btn_correct_color, theme))
                    checkedBtn.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_correct_color)
                } else {
                    quizResults.wrong += 1

                    checkedBtn.setTextColor(resources.getColor(R.color.quiz_btn_wrong_color, theme))
                    checkedBtn.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_wrong_color)

                    val correctBtnId = btnAnswersMap.entries.associate { (k, v) -> v to k }[currentQuestion.correct_answer]
                            ?: -1
                    findViewById<RadioButton>(correctBtnId)?.also {
                        it.setTextColor(resources.getColor(R.color.quiz_btn_correct_color, theme))
                        it.buttonTintList = ContextCompat.getColorStateList(this, R.color.quiz_btn_correct_color)
                    }
                }
                true
            }
        }
    }

    private fun saveResults() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        saveResults()
    }
}