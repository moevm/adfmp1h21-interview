package com.pjs.itinterviewtrainer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_category")
class QuestionCategory(
    @PrimaryKey
    val categoryId: Long,
    val categoryName: String
)