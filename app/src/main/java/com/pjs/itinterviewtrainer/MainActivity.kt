package com.pjs.itinterviewtrainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.pjs.itinterviewtrainer.data.QuizRepository

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VolleyWebService.setupRequestQueue(baseContext)

        QuizRepository.questionsList =
            QuizRepository.loadQuestions(assets.open("questions_data.json"))
        QuizRepository.quizList = listOf(
            QuizRepository.createBasicsQuiz(1),
            QuizRepository.createBasicsQuiz(2),
            QuizRepository.createBasicsQuiz(3),
        )

        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
            commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            moveTaskToBack(true)
        }
    }
}