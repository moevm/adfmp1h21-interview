package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pjs.itinterviewtrainer.data.QuestionCategory
import com.pjs.itinterviewtrainer.data.Quiz
import com.pjs.itinterviewtrainer.data.QuizRepository
import kotlinx.android.synthetic.main.fragment_setup_quiz.*
import kotlinx.android.synthetic.main.fragment_setup_quiz.view.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.Delegates

class QuizSetupFragment : Fragment() {
    companion object {
        const val TAG = "setupQuizFragment"

        @JvmStatic
        fun newInstance(): QuizSetupFragment {
            return QuizSetupFragment()
        }
    }

    private var questionsLimit = 0
    private var chosenCategories: List<QuestionCategory> = listOf()
    private var questionsAmount = -1
    private var chosenLevel = QuizRepository.levelsList[1]
    private var checkedLevelPosition: Int = 0

    private var checkedCategories: BooleanArray by Delegates.observable(BooleanArray(QuizRepository.categoriesList.size) { false })
    { _, _, newValue ->
        chosenCategories =
            QuizRepository.categoriesList.zip(checkedCategories.toList()).filter { it.second }
                .map { it.first }
        selectCategories.setText(chosenCategories.joinToString(separator = ",") { it.name })
        updateAmountSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_setup_quiz, container, false)
        checkedLevelPosition = QuizRepository.levelsList.toTypedArray().size / 2

        rootView.selectLevel.setText(chosenLevel.levelName)

        rootView.selectQAmount.setLabelFormatter { value: Float ->
            "${value.toInt()} questions"
        }

        rootView.selectTimer.setLabelFormatter { value: Float ->
            "${value.toInt()} mins"
        }

        rootView.selectTimer.valueFrom = 5.0f
        rootView.selectTimer.valueTo = 100.0f
        rootView.selectTimer.stepSize = 5.0f
        rootView.selectTimer.value = 5.0f

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
            }
            .setMultiChoiceItems(
                QuizRepository.categoriesList.map { it.name }.toTypedArray(),
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
                chosenLevel = QuizRepository.levelsList[checkedLevelPosition]
                selectLevel.setText(chosenLevel.levelName)
                updateAmountSpinner()
            }
            .setSingleChoiceItems(
                QuizRepository.levelsList.map { it.levelName }.toTypedArray(),
                checkedLevelPosition
            ) { _, which ->
                newCheckedLevelPosition = which
            }
            .create()
    }

    private fun startTest() {
        val intent = Intent(activity, QuizActivity::class.java)

        if (chosenCategories.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("No selected categories")
                .setMessage("Select categories to start test")
                .setPositiveButton("Ok") { _, _ -> }
                .create()
                .show()
            return
        }

        val quizAmount = selectQAmount.value.toString().toFloatOrNull()?.toInt() ?: 10
        val quizTimer = selectTimer.value.toString().toFloatOrNull()?.toInt() ?: 60

        val randomQuiz = Quiz(
            11497110100111109, // word "random" to int number
            "Misc",
            chosenCategories,
            chosenLevel,
            QuizRepository.pickQuestions(chosenLevel, chosenCategories, quizAmount),
            quizTimer
        )

        intent.putExtra("quiz", Json.encodeToString(randomQuiz))
        intent.putExtra("isRandom", true)
        startActivity(intent)
    }

    private fun updateAmountSpinner() {
        questionsLimit = QuizRepository.questionsList.filter { q ->
            q.level_id <= chosenLevel.id && chosenCategories.map { it.id }.contains(q.category_id)
        }.size

        if (questionsLimit > 0) {
            selectQAmount.valueFrom = 5.0f
            selectQAmount.valueTo = questionsLimit.toFloat()
            selectQAmount.value =
                if (questionsAmount == -1) (questionsLimit / 2).toFloat() else questionsAmount.toFloat()
            selectQAmount.stepSize = 1.0f
        }
    }
}