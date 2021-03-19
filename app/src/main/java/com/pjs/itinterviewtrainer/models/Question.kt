package com.pjs.itinterviewtrainer.models

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.InputStream


data class QuizResults(
        val id: Int = 0,
        var correct: Int = 0,
        var wrong: Int = 0,
        var questions: MutableList<Question> = mutableListOf()
)


/**
 * data class Question(
val questionText: String,
val level: String,
val category: String,
val code: String?,
val codePic: String?,
val answerA: String?,
val answerB: String?,
val answerC: String?,
val answerD: String?,
val correctAnswer: String,
val id: Int = 0,
)
 *
 */

data class Question(
    val difficulty: String,
    val category: String,
    val code_pic: String?,
    val code_text: String?,
    val answers: Map<String, String>,
    val correct_answer: String,
    val question_text: String,
    var id: Int = 0,
){
    companion object{
        fun loadQuestions(stream: InputStream): List<Question>{
            val jsonFileString = stream.bufferedReader().readText()
            val gsonParser = Gson()
            val listQuestionType = object : TypeToken<List<Question>>() {}.type
            var persons: List<Question> = gsonParser.fromJson(jsonFileString, listQuestionType)
            persons.forEachIndexed { idx, person -> person.id = idx }
            return persons
        }
    }
}
