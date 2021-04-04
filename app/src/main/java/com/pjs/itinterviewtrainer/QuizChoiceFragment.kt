package com.pjs.itinterviewtrainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.pjs.itinterviewtrainer.adapters.QuizListAdapter
import com.pjs.itinterviewtrainer.data.QuizRepository
import com.pjs.itinterviewtrainer.data.entities.QuizWithQuestions
import kotlinx.android.synthetic.main.fragment_quiz_choice.view.*

private const val ARG_CATEGORY_ID = "categoryId"

class QuizChoiceFragment : Fragment(), QuizListAdapter.OnQuizClickListener {
    private var categoryId: Long? = null
    private lateinit var adapter: QuizListAdapter
    private lateinit var quizesOfSelectedCategory: List<QuizWithQuestions>

    private lateinit var repository: QuizRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getLong(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_quiz_choice, container, false)
        repository = QuizRepository(requireContext().applicationContext)
        val category = repository.getCategoryById(categoryId!!)

        rootView.categoryName.text = category.categoryName

        quizesOfSelectedCategory = repository.getQuizes().filter { it.questions.all { q -> q.categoryId == category.categoryId } }

        // first selected tab - easy
        adapter = QuizListAdapter(quizesOfSelectedCategory.filter { it.questions.all { q -> q.levelId == 1.toLong() } }, this)
        rootView.quizListView.adapter = adapter
        rootView.quizListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rootView.difficultyTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    adapter.dataSet = quizesOfSelectedCategory.filter { items -> items.questions.all { q -> q.levelId == it.position.toLong() + 1 } }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        return rootView
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, QuizActivity::class.java)
        intent.putExtra("quizId", quizesOfSelectedCategory[position].quiz.quizId)
        startActivity(intent)
    }

    companion object {
        const val TAG = "quizChoiceFragment"

        @JvmStatic
        fun newInstance(categoryId: Long) =
                QuizChoiceFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_CATEGORY_ID, categoryId)
                    }
                }
    }

}