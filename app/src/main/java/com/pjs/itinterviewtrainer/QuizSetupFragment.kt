package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pjs.itinterviewtrainer.data.entities.QuestionLevel
import com.pjs.itinterviewtrainer.data.entities.Quiz
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.QuestionCategory
import kotlinx.android.synthetic.main.fragment_setup_quiz.*
import kotlinx.android.synthetic.main.fragment_setup_quiz.view.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuizSetupFragment : Fragment() {
    companion object {
        const val TAG = "setupQuizFragment"

        @JvmStatic
        fun newInstance(): QuizSetupFragment {
            return QuizSetupFragment()
        }
    }

    private lateinit var checkedCategories: BooleanArray
    private var checkedLevelPosition: Int = 1
    private var questionsAmount = 10
    private lateinit var repository: QuizRepository

    private lateinit var levelsArray: Array<QuestionLevel>
    private lateinit var categoriesArray: Array<QuestionCategory>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setup_quiz, container, false)

        repository = QuizRepository(requireContext())
        levelsArray = repository.getLevels().toTypedArray()
        categoriesArray = repository.getCategories().toTypedArray()

        checkedCategories = BooleanArray(repository.getCategories().size) { false }
        checkedLevelPosition = levelsArray.size / 2

        rootView.selectLevel.setText(levelsArray[checkedLevelPosition].levelName)

        rootView.selectQAmount.setText(questionsAmount.toString())

        rootView.selectCategories.setOnClickListener {
            val dialog = createCategoriesDialog()
            dialog.show()
        }

        rootView.selectLevel.setOnClickListener {
            val dialog = createLevelsDialog()
            dialog.show()
        }

        rootView.startTest.setOnClickListener {
            this.startTest()
        }

        return rootView
    }

    private fun createCategoriesDialog(): AlertDialog {
        var newCheckedCategories = checkedCategories.clone()

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select categories")

                .setNegativeButton("Cancel") { _, _ ->

                }

                .setPositiveButton("OK") { _, _ ->
                    checkedCategories = newCheckedCategories
                    val chosenCategories = categoriesArray.zip(checkedCategories.toList()).filter { it.second }.map { it.first.categoryName }
                    selectCategories.setText(chosenCategories.joinToString(separator = ","))
                }
                .setMultiChoiceItems(
                        categoriesArray.map { it.categoryName }.toTypedArray(),
                        newCheckedCategories
                ) { _, which, checked ->
                    newCheckedCategories[which] = checked
                }
                .create()
    }

    private fun createLevelsDialog(): AlertDialog {
        var newCheckedLevelPosition = 0
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select level")
                .setNegativeButton("Cancel") { _, _ ->

                }
                .setPositiveButton("OK") { _, _ ->
                    checkedLevelPosition = newCheckedLevelPosition
                    selectLevel.setText(levelsArray[checkedLevelPosition].levelName)
                }
                .setSingleChoiceItems(
                        levelsArray.map { it.levelName }.toTypedArray(),
                        checkedLevelPosition // 2 - medium
                ) { _, which ->
                    newCheckedLevelPosition = which
                }
                .create()
    }

    private fun startTest() {
        val intent = Intent(activity, QuizActivity::class.java)
        val chosenCategories = categoriesArray.zip(checkedCategories.toList()).filter { it.second }.map { it.first }

        if(chosenCategories.isEmpty()){
            AlertDialog.Builder(requireContext())
                .setTitle("No selected categories")
                .setMessage("Select categories to start test")
                .setPositiveButton("Ok") { _, _ -> }
                .create()
                .show()
            return
        }

        val quizAmount = selectQAmount.text.toString().toIntOrNull() ?: 10
        val level = levelsArray[checkedLevelPosition]

        val randomQuizId = repository.createRandomQuiz(level, chosenCategories, quizAmount)

        intent.putExtra("quizId", randomQuizId)
        intent.putExtra("isRandom", true)
        startActivity(intent)
    }
}