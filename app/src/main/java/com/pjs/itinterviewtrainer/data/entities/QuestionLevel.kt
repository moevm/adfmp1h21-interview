package com.pjs.itinterviewtrainer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "question_level")
data class QuestionLevel(
        @PrimaryKey
        val levelId: Long,
        val levelName: String
)