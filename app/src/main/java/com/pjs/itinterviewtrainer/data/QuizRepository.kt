package com.pjs.itinterviewtrainer.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pjs.itinterviewtrainer.data.entities.*
import java.io.InputStream

class QuizRepository(context: Context, inMemory: Boolean = false, recreate: Boolean = false) {
    private val db = QuizDatabase.getInstance(context, inMemory, recreate)!!

    fun getDb() = db

    fun getResults() = db.resultsDao().getAll()

    fun getCategories() = db.categoryDao().getAll()

    fun getLevels() = db.levelDao().getAll()

    fun getQuestions() = db.questionDao().getAll()

    fun getCategoryLevelQuestions(levelId: Long, categoryId: Long) = db.questionDao()
        .getQuestionByCategoryAndLevel(levels = listOf(levelId), categories = listOf(categoryId))

    fun getQuizes() = db.quizDao().getAll()

    fun getQuizById(id: Long) = db.quizDao().getById(id)

    fun getCategoryById(id: Long) = db.categoryDao().getById(id)

    fun getLevelById(id: Long) = db.levelDao().getById(id)

    fun getLevelByName(name: String): QuestionLevel = db.levelDao().getByName(name)

    fun getCategoryByName(name: String) = db.categoryDao().getByName(name)

    fun loadQuestions(stream: InputStream) {
        val jsonFileString = stream.bufferedReader().readText()
        val listQuestionType = object : TypeToken<List<GsonQuestion>>() {}.type
        val loadedQuestions: List<GsonQuestion> = Gson().fromJson(jsonFileString, listQuestionType)
        val questions = loadedQuestions.mapIndexed { idx, q ->
            Question(
                idx.toLong() + 1,
                q.level_id,
                q.category_id,
                q.code_pic,
                q.code_text,
                q.answers,
                q.correct_answer,
                q.question_text
            )
        }
        db.questionDao().insertAll(*questions.toTypedArray())
    }

    fun createBasicsQuiz(categoryId: Long) {
        val basicQuestions = getCategoryLevelQuestions(1, categoryId)

        val (picQuestions, noPicQuestions) = basicQuestions.partition { it.codePic != null }

        val questions = picQuestions.take(5).toMutableList()
        questions.addAll(noPicQuestions.take(10 - questions.size))

        val quiz = Quiz(getQuizes().size.toLong() + 1, "Basics", 60)
        db.quizDao().insertAll(quiz)
        db.quizDao().insertAllQuizQuestionRef(*questions.map {
            QuizQuestionCrossRef(
                quiz.quizId,
                it.questionId
            )
        }.toTypedArray())
    }

    fun createRandomQuiz(
        level: QuestionLevel,
        categoriesList: Collection<QuestionCategory>,
        amount: Int,
        timer: Int,
    ): Long {
        val questions = getQuestions().shuffled().toList().filter { q ->
            categoriesList.map { it.categoryId }
                .contains(q.categoryId) && q.levelId <= level.levelId
        }.take(amount)

        val quiz = Quiz(getQuizes().size.toLong() + 1, "Misc", timer)
        db.quizDao().insertAll(quiz)
        db.quizDao().insertAllQuizQuestionRef(*questions.map {
            QuizQuestionCrossRef(
                quiz.quizId,
                it.questionId
            )
        }.toTypedArray())
        return quiz.quizId
    }
}