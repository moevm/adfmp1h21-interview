package com.pjs.itinterviewtrainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pjs.itinterviewtrainer.data.QuizRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init fake db
        QuizRepository.questionsList = QuizRepository.loadQuestions(assets.open("questions_data.json"))
        QuizRepository.quizList = listOf(
                QuizRepository.createBasicsQuiz(0),
                QuizRepository.createBasicsQuiz(1),
                QuizRepository.createBasicsQuiz(2),
        )

        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
            commit()
        }
    }
}