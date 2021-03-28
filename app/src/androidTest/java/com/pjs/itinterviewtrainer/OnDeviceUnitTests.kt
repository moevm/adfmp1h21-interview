package com.pjs.itinterviewtrainer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pjs.itinterviewtrainer.data.Quiz
import com.pjs.itinterviewtrainer.data.QuizRepository

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class OnDeviceUnitTests {
    @Test
    fun testAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.pjs.itinterviewtrainer", appContext.packageName)
    }

    @Test
    fun testQuestionsLoaded() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))
        assert(QuizRepository.questionsList.isNotEmpty())
    }

    @Test
    fun testCreateBasicsPythonQuiz() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val pythonQuiz = QuizRepository.createBasicsQuiz(1) // 1 is python category id

        assert(pythonQuiz.quiestions.all { it.level_id == 1 && it.category_id == 1 }) // 1 is easy level id
    }

    @Test
    fun testCreateBasicsCppQuiz() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val cppQuiz = QuizRepository.createBasicsQuiz(2) // 1 is python category id

        assert(cppQuiz.quiestions.all { it.level_id == 1 && it.category_id == 2 }) // 1 is easy level id
    }

    @Test
    fun testCreateBasicsJavaScriptQuiz() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val javascriptQuiz = QuizRepository.createBasicsQuiz(3) // 1 is python category id

        assert(javascriptQuiz.quiestions.all { it.level_id == 1 && it.category_id == 3 }) // 1 is easy level id
    }

    @Test
    fun testCreateRandomQuestions1() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[0], QuizRepository.categoriesList.subList(0, 1), 10)
        assertEquals(10, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id == 1 && it.category_id == 1})
    }

    @Test
    fun testCreateRandomQuestions2() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[1], QuizRepository.categoriesList.subList(0, 2), 30)
        assertEquals(30, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id <= 2 && it.category_id <= 2})
    }

    @Test
    fun testCreateRandomQuestions3() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        QuizRepository.loadQuestions(appContext.assets.open("questions_data.json"))

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[2], QuizRepository.categoriesList.subList(0, 3), 50)
        assertEquals(50, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id <= 3 && it.category_id <= 3})
    }
}