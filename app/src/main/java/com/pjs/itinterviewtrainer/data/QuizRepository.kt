package com.pjs.itinterviewtrainer.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

object QuizRepository {
    var categoriesList: List<QuestionCategory> = listOf("Python", "C++", "JavaScript").mapIndexed { i, c -> QuestionCategory(i, c) }
    var levelsList: List<QuestionLevel> = listOf("easy", "medium", "hard").mapIndexed { i, d -> QuestionLevel(i, d) }
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