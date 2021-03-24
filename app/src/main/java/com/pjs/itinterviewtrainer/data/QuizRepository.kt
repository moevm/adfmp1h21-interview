package com.pjs.itinterviewtrainer.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

object QuizRepository {
    var categoriesList: List<QuestionCategory> = listOf("Python", "C++", "JavaScript").mapIndexed { i, c -> QuestionCategory(i, c) }
    var levelsList: List<QuestionLevel> = listOf("easy", "medium", "hard").mapIndexed { i, d -> QuestionLevel(i, d) }
    var statisticsList: List<QuizResults> = listOf(
            QuizResults(0, 23, 8, mutableListOf<Question>(), 2493, "Title"),
            QuizResults(0, 13, 5, mutableListOf<Question>(), 493, "Test's title c++"),
            QuizResults(0, 12, 9, mutableListOf<Question>(), 12345, "Long test"),
            QuizResults(0, 15, 0, mutableListOf<Question>(), 43, "Short test"),
            QuizResults(0, 23, 8, mutableListOf<Question>(), 300, "Traktorist"),
            QuizResults(0, 43, 12, mutableListOf<Question>(), 839, "Test 2"),
            QuizResults(0, 0, 0, mutableListOf<Question>(), 0, "Empty")
    )
    lateinit var questionsList: List<Question>
    lateinit var quizList: List<Quiz>

    fun loadQuestions(stream: InputStream): List<Question> {
        val jsonFileString = stream.bufferedReader().readText()
        val gsonParser = Gson()
        val listQuestionType = object : TypeToken<List<Question>>() {}.type
        var persons: List<Question> = gsonParser.fromJson(jsonFileString, listQuestionType)
        persons.forEachIndexed { idx, person -> person.id = idx }
        return persons
    }

    fun createBasicsQuiz(categoryId: Int): Quiz {
        val categories = listOf(categoriesList.find { c -> c.id == categoryId } ?: categoriesList.first())
        return Quiz(
                0,
                "Basics",
            categories,
                levelsList[0],
                questionsList
                        .filter { q -> categories.map{it.name}.contains(q.category) && q.difficulty == levelsList[0].difficulty }
                        .take(10)
        )
    }

    fun pickQuestions(level: QuestionLevel, categoriesList: Collection<QuestionCategory>, amount: Int): List<Question>{
        return questionsList.filter { q -> categoriesList.map { it.name }.contains(q.category) }.shuffled().take(amount)
    }

}