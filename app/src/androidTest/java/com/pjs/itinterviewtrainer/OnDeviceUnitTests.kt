package com.pjs.itinterviewtrainer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.Question
import com.pjs.itinterviewtrainer.data.entities.QuestionCategory
import com.pjs.itinterviewtrainer.data.entities.QuestionLevel
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before


val fakeQuestionsForDevice = listOf(
    Question(0, 1, 1, null, null, mapOf(), "a", "?"),
    Question(1, 1, 1, null, null, mapOf(), "a", "?"),
    Question(2, 1, 2, null, null, mapOf(), "a", "?"),
    Question(3, 1, 2, null, null, mapOf(), "a", "?"),
    Question(4, 1, 3, null, null, mapOf(), "a", "?"),
    Question(5, 1, 3, null, null, mapOf(), "a", "?"),
    Question(6, 2, 1, null, null, mapOf(), "a", "?"),
    Question(7, 2, 1, null, null, mapOf(), "a", "?"),
    Question(8, 2, 2, null, null, mapOf(), "a", "?"),
    Question(9, 2, 2, null, null, mapOf(), "a", "?"),
    Question(10, 2, 3, null, null, mapOf(), "a", "?"),
    Question(11, 2, 3, null, null, mapOf(), "a", "?"),
    Question(12, 3, 1, null, null, mapOf(), "a", "?"),
    Question(13, 3, 1, null, null, mapOf(), "a", "?"),
    Question(14, 3, 2, null, null, mapOf(), "a", "?"),
    Question(15, 3, 3, null, null, mapOf(), "a", "?"),
    Question(16, 3, 3, null, null, mapOf(), "a", "?"),
)

@RunWith(AndroidJUnit4::class)
class OnDeviceUnitTests {
    private lateinit var quizRepository: QuizRepository

    @Before
    fun onStartup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        quizRepository = QuizRepository(context, inMemory=true, recreate=true)
        quizRepository.getDb().clearAllTables()
        quizRepository.getDb().questionDao().insertAll(*fakeQuestionsForDevice.toTypedArray())
    }

    @After
    fun onShutdown(){
        quizRepository.getDb().close()
    }

    @Test
    fun testQuestionsLoaded() {
        assertEquals(fakeQuestionsForDevice.size, quizRepository.getQuestions().size)
    }

    @Test
    fun testCreateBasicsQuiz1() {
        quizRepository.createBasicsQuiz(
            1)

        assert(quizRepository.getQuizes().isNotEmpty())
        assert(quizRepository.getQuizById(1).questions.all { it.categoryId == 1.toLong() })
    }

    @Test
    fun testCreateTwoBasicsQuizes() {
        quizRepository.createBasicsQuiz(
            1)
        quizRepository.createBasicsQuiz(
            2)

        assertEquals(2, quizRepository.getQuizes().size)
        assert(quizRepository.getQuizById(1).questions.all { it.categoryId == 1.toLong() })
        assert(quizRepository.getQuizById(2).questions.all { it.categoryId == 2.toLong() })
    }

    @Test
    fun testCreateRandomQuiz1() {
        val quizId = quizRepository.createRandomQuiz(
            QuestionLevel(1, "fake"),
            listOf(QuestionCategory(1, "a")),
            6,
            60
        )
        assert(quizRepository.getQuizes().isNotEmpty())
        assertEquals(2, quizRepository.getQuizById(quizId).questions.size)
        assert(quizRepository.getQuizById(quizId).questions.all { it.categoryId == 1.toLong()  && it.levelId == 1.toLong()})
    }

    @Test
    fun testCreateRandomQuiz2() {
        val quizId = quizRepository.createRandomQuiz(
            QuestionLevel(2, "fake"),
            listOf(QuestionCategory(1, "a"), QuestionCategory(2, "a")),
            8,
            60
        )
        assert(quizRepository.getQuizes().isNotEmpty())
        assertEquals(8, quizRepository.getQuizById(quizId).questions.size)
        assert(quizRepository.getQuizById(quizId).questions.all { listOf(1.toLong(), 2.toLong()).contains(it.categoryId) && it.levelId <= 2.toLong()})
    }

    @Test
    fun testCreateRandomQuiz3() {
        val quizId = quizRepository.createRandomQuiz(
            QuestionLevel(3, "fake"),
            listOf(QuestionCategory(1, "a"), QuestionCategory(2, "a"), QuestionCategory(3, "a")),
            16,
            60
        )
        assert(quizRepository.getQuizes().isNotEmpty())
        assertEquals(16, quizRepository.getQuizById(quizId).questions.size)
        assert(quizRepository.getQuizById(quizId).questions.all { listOf(1.toLong(), 2.toLong(), 3.toLong()).contains(it.categoryId) && it.levelId <= 3.toLong()})
    }
}
