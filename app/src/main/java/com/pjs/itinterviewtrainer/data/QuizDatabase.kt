package com.pjs.itinterviewtrainer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pjs.itinterviewtrainer.data.entities.*


@Database(
    entities = [
        Question::class,
        Quiz::class,
        QuizQuestionCrossRef::class,
        QuestionLevel::class,
        QuestionCategory::class,
        QuizResults::class
    ],
    version = 1
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun levelDao(): LevelDao
    abstract fun categoryDao(): CategoryDao
    abstract fun quizDao(): QuizDao
    abstract fun resultsDao(): QuizResultsDao

    companion object {
        private const val NAME = "quiz_db"
        private var instance: QuizDatabase? = null

        fun getInstance(
            context: Context,
            inMemory: Boolean = false,
            recreate: Boolean = false
        ): QuizDatabase? {
            if (instance == null || recreate) {
                instance = if (!inMemory) {
                    Room.databaseBuilder(
                        context,
                        QuizDatabase::class.java,
                        NAME
                    ).allowMainThreadQueries().build()
                } else {
                    Room.inMemoryDatabaseBuilder(
                        context,
                        QuizDatabase::class.java,
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}