package com.pjs.itinterviewtrainer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResults(
    @PrimaryKey
    val resultId: Long,
    var quizId: Long,
    var quizTitle: String,
    var correct: Int = 0,
    var wrong: Int = 0,
    var passedTimeInSeconds: Int = 0,
)
