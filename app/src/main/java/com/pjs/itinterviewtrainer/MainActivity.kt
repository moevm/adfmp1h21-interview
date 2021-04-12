package com.pjs.itinterviewtrainer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.QuestionCategory
import com.pjs.itinterviewtrainer.data.entities.QuestionLevel

class MainActivity : AppCompatActivity() {
    private lateinit var repository: QuizRepository

    companion object {
        const val FIRST_TIME_TAG = "firstTime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VolleyWebService.setupRequestQueue(baseContext)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        val isFirstStart = sharedPref.getBoolean(FIRST_TIME_TAG, true)
        if (isFirstStart) {

            repository = QuizRepository(applicationContext)
            val categories = listOf(
                "Python",
                "C++",
                "JavaScript"
            ).mapIndexed { i, c -> QuestionCategory(i.toLong() + 1, c) }

            val levels = listOf("Easy", "Medium", "Hard").mapIndexed { i, d ->
                QuestionLevel(
                    i.toLong() + 1,
                    d
                )
            }

            repository.loadQuestions(assets.open("questions_data.json"))
            repository.getDb().levelDao().insertAll(*levels.toTypedArray())
            repository.getDb().categoryDao().insertAll(*categories.toTypedArray())

            repository.createQuiz(1, 1, 10, "Basics")
            repository.createQuiz(2, 1, 15, "Basics 2")
            repository.createQuiz(3, 1, 20, "Basics 3")
            repository.createQuiz(1, 2, 10, "Basics")
            repository.createQuiz(2, 2, 15, "Basics 2")
            repository.createQuiz(3, 2, 20, "Basics 3")
            repository.createQuiz(1, 3, 10, "Basics")
            repository.createQuiz(2, 3, 15, "Basics 2")
            repository.createQuiz(3, 3, 20, "Basics 3")

            with(sharedPref.edit()) {
                putBoolean(FIRST_TIME_TAG, false)
                commit()
            }
        }

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