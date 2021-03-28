package com.pjs.itinterviewtrainer.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import kotlin.math.min

object QuizRepository {
    var categoriesList: List<QuestionCategory> =
        listOf("Python", "C++", "JavaScript").mapIndexed { i, c -> QuestionCategory(i + 1, c) }
    var levelsList: List<QuestionLevel> =
        listOf("Easy", "Medium", "Hard").mapIndexed { i, d -> QuestionLevel(i + 1, d) }
    var statisticsList: MutableList<QuizResults> = mutableListOf()

    var questionsList: List<Question> = listOf()
    var quizList: List<Quiz> = listOf()

    fun loadQuestions(stream: InputStream): List<Question> {
        val jsonFileString = stream.bufferedReader().readText()
        val listQuestionType = object : TypeToken<List<Question>>() {}.type
        var questions: List<Question> =  Gson().fromJson(jsonFileString, listQuestionType)
        questions.forEachIndexed { idx, person -> person.id = idx + 1 }
        this.questionsList = questions
        return this.questionsList
    }

    fun createBasicsQuiz(categoryId: Int): Quiz {
        val categories =
            listOf(categoriesList.find { c -> c.id == categoryId } ?: categoriesList.first())

        val basicQuestions = questionsList.filter { q ->
                categories.map { it.id }.contains(q.category_id) && q.level_id == levelsList.first().id
            }

        val (picQuestions, noPicQuestions) = basicQuestions.partition { it.code_pic != null }

        val questions = picQuestions.take(5).toMutableList()
        questions.addAll(noPicQuestions.take(10 - questions.size))

        return Quiz(
            0,
            "Basics",
            categories,
            levelsList.first(),
            questions.shuffled()
        )
    }

    fun pickQuestions(
        level: QuestionLevel,
        categoriesList: Collection<QuestionCategory>,
        amount: Int
    ): List<Question> {
        val questions = questionsList.shuffled().toList().filter { q -> categoriesList.map { it.id }.contains(q.category_id) && q.level_id <= level.id}
        return questions.take(min(amount, questions.size))
    }
}