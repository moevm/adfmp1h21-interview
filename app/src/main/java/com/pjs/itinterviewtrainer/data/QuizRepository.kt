package com.pjs.itinterviewtrainer.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

object QuizRepository {
    var categoriesList: List<QuestionCategory> =
        listOf("Python", "C++", "JavaScript").mapIndexed { i, c -> QuestionCategory(i + 1, c) }
    var levelsList: List<QuestionLevel> =
        listOf("Easy", "Medium", "Hard").mapIndexed { i, d -> QuestionLevel(i + 1, d) }
    var statisticsList: List<QuizResults> = listOf(
        QuizResults(0, 23, 8, mutableListOf<Question>(), 2493, "Title"),
        QuizResults(0, 13, 5, mutableListOf<Question>(), 493, "Test's title c++ Long name"),
        QuizResults(0, 12, 9, mutableListOf<Question>(), 12345, "Long test"),
        QuizResults(0, 15, 0, mutableListOf<Question>(), 43, "Short test"),
        QuizResults(0, 23, 8, mutableListOf<Question>(), 300, "Traktorist"),
        QuizResults(0, 43, 12, mutableListOf<Question>(), 839, "Test 2"),
        QuizResults(0, 0, 0, mutableListOf<Question>(), 0, "Empty"),
        QuizResults(0, 13, 5, mutableListOf<Question>(), 493, "Test's title c++"),
        QuizResults(0, 12, 9, mutableListOf<Question>(), 12345, "Long test"),
        QuizResults(0, 15, 0, mutableListOf<Question>(), 43, "Short test"),
        QuizResults(0, 23, 8, mutableListOf<Question>(), 300, "Traktorist"),
        QuizResults(0, 43, 12, mutableListOf<Question>(), 839, "Test 2"),
        QuizResults(0, 0, 0, mutableListOf<Question>(), 0, "Empty"),
    )
    lateinit var questionsList: List<Question>
    lateinit var quizList: List<Quiz>

    fun loadQuestions(stream: InputStream): List<Question> {
        val jsonFileString = stream.bufferedReader().readText()
        val listQuestionType = object : TypeToken<List<Question>>() {}.type
        var questions: List<Question> =  Gson().fromJson(jsonFileString, listQuestionType)
        questions.forEachIndexed { idx, person -> person.id = idx + 1 }
        return questions
    }

    fun createBasicsQuiz(categoryId: Int): Quiz {
        val categories =
            listOf(categoriesList.find { c -> c.id == categoryId } ?: categoriesList.first())
        return Quiz(
            0,
            "Basics",
            categories,
            levelsList.first(),
            questionsList
                .filter { q ->
                    categories.map { it.id }.contains(q.category_id) && q.code_pic != null
//                        .contains(q.category_id) && q.level_id == levelsList.first().id
                }
                .take(10)
        )
    }

    fun pickQuestions(
        level: QuestionLevel,
        categoriesList: Collection<QuestionCategory>,
        amount: Int
    ): List<Question> {
        return questionsList.shuffled().toList().filter { q -> categoriesList.map { it.id }.contains(q.category_id) && q.level_id <= level.id}.take(amount)
    }

}