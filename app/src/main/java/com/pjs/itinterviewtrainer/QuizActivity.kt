package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.Question
import com.pjs.itinterviewtrainer.data.entities.QuizResults
import com.pjs.itinterviewtrainer.data.entities.QuizWithQuestions
import kotlinx.android.synthetic.main.activity_quiz.*

enum class SubmitState {
    SUBMIT,
    NEXT
}

class QuizActivity : AppCompatActivity() {
    private lateinit var repository: QuizRepository
    private var isRandomQuiz = false
    private var isCompleted = false
    private var quizTime = 60
    private var currentQuestionNumber = 0
    private val btnAnswersMap = mapOf(
        R.id.answer_a to "a",
        R.id.answer_b to "b",
        R.id.answer_c to "c",
        R.id.answer_d to "d"
    )
    private var submitState: SubmitState = SubmitState.SUBMIT
    private lateinit var quizResults: QuizResults
    private var passedTime: Long = 0
    private lateinit var currentQuiz: QuizWithQuestions
    private lateinit var currentQuestion: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)

        repository = QuizRepository(applicationContext)
        with(intent) {
            val quizId = getLongExtra("quizId", -1)
            if (quizId == (-1).toLong()) {
                onBackPressed()
            }
            currentQuiz = repository.getQuizById(quizId)
            isRandomQuiz = getBooleanExtra("isRandom", false)
        }

        quizTime = currentQuiz.quiz.quizTime
        quizResults = QuizResults(
            repository.getResults().size.toLong() + 1,
            currentQuiz.quiz.quizId,
            currentQuiz.quiz.quizName
        )
        val title = "${
            currentQuiz.questions.map { it.categoryId }.toSet()
                .map { repository.getCategoryById(it) }.joinToString { it.categoryName }
        }: ${currentQuiz.quiz.quizName}"

        quizResults.quizTitle = title
        quizTitle.text = title

        val context = this

        val time = quizTime * 60 * 1000

        object : CountDownTimer((time).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                runOnUiThread {
                    passedTime = (time - millisUntilFinished) / 1000
                    timer.text = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                isCompleted = true
                AlertDialog.Builder(context)
                    .setTitle("Times up | Quiz completed")
                    .setMessage("Result: ${quizResults.correct}/${currentQuiz.questions.size} correct answers")
                    .setPositiveButton("Ok") { _, _ ->
                        saveResults()
                        onBackPressed()
                    }
                    .create()
                    .show()
                return
            }
        }.start()

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
        answer_a.buttonTintList =
            ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_b.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_b.buttonTintList =
            ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_c.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_c.buttonTintList =
            ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
        answer_d.setTextColor(resources.getColor(R.color.quiz_btn_default_color, theme))
        answer_d.buttonTintList =
            ContextCompat.getColorStateList(this, R.color.quiz_btn_default_color)
    }

    private fun setupNextQuestion() {
        if (currentQuestionNumber >= currentQuiz.questions.size) {
            isCompleted = true
            AlertDialog.Builder(this)
                .setTitle("Quiz completed")
                .setMessage("Result: ${quizResults.correct}/${currentQuiz.questions.size} correct answers")
                .setPositiveButton("Ok") { _, _ ->
                    saveResults()
                    onBackPressed()
                }
                .create()
                .show()
            return
        }
        resetViewColors()
        currentQuestionNumber += 1
        currentQuestion = currentQuiz.questions[currentQuestionNumber - 1]
        questionNumber.text = "Question ${currentQuestionNumber}/${currentQuiz.questions.size}"
        questionText.text = currentQuestion.questionText
        answers.clearCheck()
        if (currentQuestion.codePic != null) {
            imageContainer.visibility = View.VISIBLE
            question_pic.setImageUrl(currentQuestion.codePic, VolleyWebService.imageLoader)
        } else {
            imageContainer.visibility = View.GONE
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
                val answer = btnAnswersMap[btnId]
                val checkedBtn = findViewById<RadioButton>(btnId)
                if (answer == currentQuestion.correctAnswer) {
                    quizResults.correct += 1
                    checkedBtn.setTextColor(
                        resources.getColor(
                            R.color.quiz_btn_correct_color,
                            theme
                        )
                    )
                    checkedBtn.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.quiz_btn_correct_color)
                } else {
                    quizResults.wrong += 1

                    checkedBtn.setTextColor(resources.getColor(R.color.quiz_btn_wrong_color, theme))
                    checkedBtn.buttonTintList =
                        ContextCompat.getColorStateList(this, R.color.quiz_btn_wrong_color)

                    val correctBtnId =
                        btnAnswersMap.entries.associate { (k, v) -> v to k }[currentQuestion.correctAnswer]
                            ?: -1
                    findViewById<RadioButton>(correctBtnId)?.also {
                        it.setTextColor(resources.getColor(R.color.quiz_btn_correct_color, theme))
                        it.buttonTintList =
                            ContextCompat.getColorStateList(this, R.color.quiz_btn_correct_color)
                    }
                }
                true
            }
        }
    }

    private fun saveResults() {
        quizResults.passedTimeInSeconds = passedTime.toInt()
        repository.getDb().resultsDao().insertAll(quizResults)
    }

    override fun onBackPressed() {
        if (isRandomQuiz && isCompleted) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }
}