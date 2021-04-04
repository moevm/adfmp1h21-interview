package com.pjs.itinterviewtrainer.data.entities

import androidx.room.*

//@Serializable
//data class Quiz(val id: Long, val name: String, val categories: List<QuestionCategory>, val level: QuestionLevel, var quiestions: List<Question>)


@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey val quizId: Long,
    val quizName: String,
    val quizTime: Int,
)

@Entity(tableName = "quiz_questions", primaryKeys = ["quizId", "questionId"])
data class QuizQuestionCrossRef(
    val quizId: Long,
    val questionId: Long
)

data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "quizId",
        entityColumn = "questionId",
        associateBy = Junction(QuizQuestionCrossRef::class)
    )
    val questions: List<Question>
)