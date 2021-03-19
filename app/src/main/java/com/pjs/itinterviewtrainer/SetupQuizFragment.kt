package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pjs.itinterviewtrainer.models.QuizDataModel
import kotlinx.android.synthetic.main.fragment_setup_quiz.*
import kotlinx.android.synthetic.main.fragment_setup_quiz.view.*

class SetupQuizFragment : Fragment() {
    companion object{
        const val TAG = "setupTestFragment"

        @JvmStatic
        fun newInstance(): SetupQuizFragment {
            return SetupQuizFragment()
        }
    }

    private val quizDataModel: QuizDataModel by activityViewModels()
    private lateinit var categoriesArray: Array<String>
    private lateinit var checkedCategories: BooleanArray
    private lateinit var levelsArray: Array<String>
    private var checkedLevel: Int = 0
    private var questionsAmount = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setup_quiz, container, false)

        categoriesArray = quizDataModel.getCategories().value!!.toTypedArray()
        checkedCategories = BooleanArray(categoriesArray.size) {false}
        levelsArray = quizDataModel.getLevels().value!!.toTypedArray()
        checkedLevel = levelsArray.size / 2

        rootView.selectLevel.setText(levelsArray[checkedLevel])
        rootView.selectQAmount.setText(questionsAmount.toString())

        rootView.selectCategories.setOnClickListener{
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
                val chosenCategories = categoriesArray.zip(checkedCategories.toList()).filter { it.second }.map { it.first }
                selectCategories.setText(chosenCategories.joinToString(separator=","))
            }
            .setMultiChoiceItems(
                categoriesArray,
                newCheckedCategories
            ) { _, which, checked ->
                newCheckedCategories[which] = checked
            }
            .create()
    }

    private fun createLevelsDialog(): androidx.appcompat.app.AlertDialog {
        var newCheckedLevel = 0
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select level")
            .setNegativeButton("Cancel") { _, _ ->

            }
            .setPositiveButton("OK") { _, _ ->
                checkedLevel = newCheckedLevel
                selectLevel.setText(levelsArray[checkedLevel])
            }
            .setSingleChoiceItems(
                levelsArray,
                checkedLevel // 2 - medium
            ) { _, which ->
                newCheckedLevel = which
            }
            .create()
    }

    private fun startTest(){
        val quizActivity = Intent(activity, QuizActivity::class.java)
        val chosenCategories = categoriesArray.zip(checkedCategories.toList()).filter { it.second }.map { it.first }
        val quizAmount = selectQAmount.text.toString().toIntOrNull() ?: 10
        val timer = 60

        quizActivity.putStringArrayListExtra("quizCategories", ArrayList(chosenCategories))
        quizActivity.putExtra("quizAmount", quizAmount.toString())
        quizActivity.putExtra("quizTimer", timer.toString())
        quizActivity.putExtra("quizLevel", levelsArray[checkedLevel])

        startActivity(quizActivity)
    }
}