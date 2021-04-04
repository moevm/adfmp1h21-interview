package com.pjs.itinterviewtrainer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.QuestionCategory
import com.pjs.itinterviewtrainer.data.entities.QuestionLevel

class MainActivity : AppCompatActivity() {
    private lateinit var repository: QuizRepository
    companion object{
        const val FIRST_TIME_TAG = "firstTime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VolleyWebService.setupRequestQueue(baseContext)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        val isFirstStart = sharedPref.getBoolean(FIRST_TIME_TAG, true)
        if(isFirstStart) {

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

            repository.createBasicsQuiz(1)
            repository.createBasicsQuiz(2)
            repository.createBasicsQuiz(3)

            with(sharedPref.edit()){
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