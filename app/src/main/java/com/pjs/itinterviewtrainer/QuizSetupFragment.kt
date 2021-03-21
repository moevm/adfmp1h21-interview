package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pjs.itinterviewtrainer.data.QuestionLevel
import com.pjs.itinterviewtrainer.data.QuizRepository
import kotlinx.android.synthetic.main.fragment_setup_quiz.*
import kotlinx.android.synthetic.main.fragment_setup_quiz.view.*

class QuizSetupFragment : Fragment() {
    companion object {
        const val TAG = "setupQuizFragment"

        @JvmStatic
        fun newInstance(): QuizSetupFragment {
            return QuizSetupFragment()
        }
    }

    private lateinit var checkedCategories: BooleanArray
    private lateinit var levelsArray: Array<QuestionLevel>
    private var checkedLevelPosition: Int = 1
    private var questionsAmount = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setup_quiz, container, false)

        checkedCategories = BooleanArray(QuizRepository.categoriesList.size) { false }
        levelsArray = QuizRepository.levelsList.toTypedArray()
        checkedLevelPosition = levelsArray.size / 2

        rootView.selectLevel.setText(levelsArray[checkedLevelPosition].difficulty)
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

    private fun createCategoriesDialog(): androidx.appcompat.app.AlertDialog {
        var newCheckedCategories = checkedCategories.clone()

        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select categories")

                .setNegativeButton("Cancel") { _, _ ->

                }

                .setPositiveButton("OK") { _, _ ->
                    checkedCategories = newCheckedCategories
                    val chosenCategories = QuizRepository.categoriesList.zip(checkedCategories.toList()).filter { it.second }.map { it.first.name }
                    selectCategories.setText(chosenCategories.joinToString(separator = ","))
                }
                .setMultiChoiceItems(
                        QuizRepository.categoriesList.map { it.name }.toTypedArray(),
                        newCheckedCategories
                ) { _, which, checked ->
                    newCheckedCategories[which] = checked
                }
                .create()
    }

    private fun createLevelsDialog(): androidx.appcompat.app.AlertDialog {
        var newCheckedLevelPosition = 0
        return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Select level")
                .setNegativeButton("Cancel") { _, _ ->

                }
                .setPositiveButton("OK") { _, _ ->
                    checkedLevelPosition = newCheckedLevelPosition
                    selectLevel.setText(levelsArray[checkedLevelPosition].difficulty)
                }
                .setSingleChoiceItems(
                        levelsArray.map { it.difficulty }.toTypedArray(),
                        checkedLevelPosition // 2 - medium
                ) { _, which ->
                    newCheckedLevelPosition = which
                }
                .create()
    }

    private fun startTest() {
        val quizActivity = Intent(activity, QuizActivity::class.java)
        val chosenCategories = QuizRepository.categoriesList.zip(checkedCategories.toList()).filter { it.second }.map { it.first }
        val quizAmount = selectQAmount.text.toString().toIntOrNull() ?: 10
        val timer = 60

        quizActivity.putStringArrayListExtra("quizCategoriesId", ArrayList(chosenCategories.map { it.id.toString() }))
        quizActivity.putExtra("quizAmount", quizAmount.toString())
        quizActivity.putExtra("quizTimer", timer.toString())
        quizActivity.putExtra("quizLevelId", levelsArray[checkedLevelPosition].id)

        startActivity(quizActivity)
    }
}