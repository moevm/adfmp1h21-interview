package com.pjs.itinterviewtrainer

import com.pjs.itinterviewtrainer.data.Question
import com.pjs.itinterviewtrainer.data.QuizRepository
import org.junit.Test

import org.junit.Assert.*


val fakeQuestionsList = listOf(
    Question(id=0, category_id = 1, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=1, category_id = 1, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=2, category_id = 1, level_id = 2, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=3, category_id = 1, level_id = 2, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=4, category_id = 1, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=5, category_id = 1, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=6, category_id = 2, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=7, category_id = 2, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=8, category_id = 2, level_id = 2, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=9, category_id = 2, level_id = 2, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=10, category_id = 2, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=11, category_id = 2, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=12, category_id = 3, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=13, category_id = 3, level_id = 1, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=14, category_id = 3, level_id = 2, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=15, category_id = 3, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
    Question(id=16, category_id = 3, level_id = 3, code_pic = null, code_text = null, answers = mapOf(), correct_answer = "a", question_text = "?"),
)

class ExampleUnitTest {
    @Test
    fun testCreateBasicsPythonQuiz() {
        QuizRepository.questionsList = fakeQuestionsList

        val pythonQuiz = QuizRepository.createBasicsQuiz(1) // 1 is python category id

        assert(pythonQuiz.quiestions.all { it.level_id == 1 && it.category_id == 1 }) // 1 is easy level id
    }

    @Test
    fun testCreateBasicsCppQuiz() {
        QuizRepository.questionsList = fakeQuestionsList

        val cppQuiz = QuizRepository.createBasicsQuiz(2) // 1 is python category id

        assert(cppQuiz.quiestions.all { it.level_id == 1 && it.category_id == 2 }) // 1 is easy level id
    }

    @Test
    fun testCreateBasicsJavaScriptQuiz() {
        QuizRepository.questionsList = fakeQuestionsList

        val javascriptQuiz = QuizRepository.createBasicsQuiz(3) // 1 is python category id

        assert(javascriptQuiz.quiestions.all { it.level_id == 1 && it.category_id == 3 }) // 1 is easy level id
    }

    @Test
    fun testCreateRandomQuestions1() {
        QuizRepository.questionsList = fakeQuestionsList

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[0], QuizRepository.categoriesList.subList(0, 1), 2)
        assertEquals(2, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id == 1 && it.category_id == 1})
    }

    @Test
    fun testCreateRandomQuestions2() {
        QuizRepository.questionsList = fakeQuestionsList

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[1], QuizRepository.categoriesList.subList(0, 2), 8)
        assertEquals(8, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id <= 2 && it.category_id <= 2})
    }

    @Test
    fun testCreateRandomQuestions3() {
        QuizRepository.questionsList = fakeQuestionsList

        val randomEasyQuestions = QuizRepository.pickQuestions(QuizRepository.levelsList[2], QuizRepository.categoriesList.subList(0, 3), 16)
        assertEquals(16, randomEasyQuestions.size)
        assert(randomEasyQuestions.all { it.level_id <= 3 && it.category_id <= 3})
    }
}