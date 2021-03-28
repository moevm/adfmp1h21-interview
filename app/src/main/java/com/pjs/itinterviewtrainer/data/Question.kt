package com.pjs.itinterviewtrainer.data

import kotlinx.serialization.Serializable


data class QuizResults(
        val id: Long = 0,
        var quiz_id: Long = -1,
        var quizTitle: String = "",
        var correct: Int = 0,
        var wrong: Int = 0,
        var passedTimeInSeconds: Int = 0,
)


@Serializable
data class Question(
        val level_id: Int,
        val category_id: Int,
        val code_pic: String?,
        val code_text: String?,
        val answers: Map<String, String>,
        val correct_answer: String,
        val question_text: String,
        var id: Int = 0,
)
