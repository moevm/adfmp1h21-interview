package com.pjs.itinterviewtrainer.data

data class Quiz(val id: Int, val name: String, val category: QuestionCategory, val level: QuestionLevel, var quiestions: List<Question>)