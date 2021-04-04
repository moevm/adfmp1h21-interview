package com.pjs.itinterviewtrainer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "question")
@TypeConverters(MapConverter::class)
data class Question(
    @PrimaryKey
    val questionId: Long,
    val levelId: Long,
    val categoryId: Long,
    val codePic: String?,
    val codeText: String?,
    val answers: Map<String, String>,
    val correctAnswer: String,
    val questionText: String,
)

data class GsonQuestion(
    val level_id: Long,
    val category_id: Long,
    val code_pic: String?,
    val code_text: String?,
    val answers: Map<String, String>,
    val correct_answer: String,
    val question_text: String,
)

object MapConverter {
    @TypeConverter
    @JvmStatic
    fun toMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<String, String>): String {
        return Gson().toJson(map)
    }
}


