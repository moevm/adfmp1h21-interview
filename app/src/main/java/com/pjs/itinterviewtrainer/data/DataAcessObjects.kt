package com.pjs.itinterviewtrainer.data

import androidx.room.*
import com.pjs.itinterviewtrainer.data.entities.*

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg questions: Question)

    @Query("SELECT * FROM question")
    fun getAll(): List<Question>

    @Query("SELECT * FROM question WHERE levelId IN (:levels) AND categoryId IN (:categories)")
    fun getQuestionByCategoryAndLevel(levels: List<Long>, categories: List<Long>): List<Question>
}

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg quizes: Quiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllQuizQuestionRef(vararg questionRefs: QuizQuestionCrossRef)

    @Query("SELECT * FROM quiz WHERE quizId = :id")
    fun getById(id: Long) : QuizWithQuestions

    @Transaction
    @Query("SELECT * FROM quiz")
    fun getAll(): List<QuizWithQuestions>
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg categories: QuestionCategory)

    @Query("SELECT * FROM question_category")
    fun getAll(): List<QuestionCategory>

    @Query("SELECT * FROM question_category WHERE categoryId = :id")
    fun getById(id: Long): QuestionCategory

    @Query("SELECT * FROM question_category WHERE categoryName = :name")
    fun getByName(name: String): QuestionCategory
}

@Dao
interface LevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg levels: QuestionLevel)

    @Query("SELECT * FROM question_level")
    fun getAll(): List<QuestionLevel>

    @Query("SELECT * FROM question_level WHERE levelId = :id")
    fun getById(id: Long): QuestionLevel

    @Query("SELECT * FROM question_level WHERE levelName = :name")
    fun getByName(name: String): QuestionLevel
}

@Dao
interface QuizResultsDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg levels: QuizResults)

    @Query("SELECT * FROM quiz_results")
    fun getAll(): List<QuizResults>
}