package com.pjs.itinterviewtrainer.data

import kotlinx.serialization.Serializable

@Serializable
data class QuestionLevel(
        val id: Int,
        val difficulty: String
)