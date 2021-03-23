package com.pjs.itinterviewtrainer.data

import kotlinx.serialization.Serializable


data class QuizResults(
        val id: Int = 0,
        var correct: Int = 0,
        var wrong: Int = 0,
        var questions: MutableList<Question> = mutableListOf()
)


@Serializable
data class Question(
        val difficulty: String,
        val category: String,
        val code_pic: String?,
        val code_text: String?,
        val answers: Map<String, String>,
        val correct_answer: String,
        val question_text: String,
        var id: Int = 0,
)