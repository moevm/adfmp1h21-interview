package com.pjs.itinterviewtrainer.data

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val id: Long,
    val name: String,
    val categories: List<QuestionCategory>,
    val level: QuestionLevel,
    var quiestions: List<Question>,
    val minutes: Int = 60
)