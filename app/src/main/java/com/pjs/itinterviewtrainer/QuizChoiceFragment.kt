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
import com.pjs.itinterviewtrainer.data.Quiz
import com.pjs.itinterviewtrainer.data.QuizRepository
import kotlinx.android.synthetic.main.fragment_quiz_choice.view.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val ARG_CATEGORY_ID = "categoryId"

class QuizChoiceFragment : Fragment(), QuizListAdapter.OnQuizClickListener {
    // TODO: Rename and change types of parameters
    private var categoryId: Int? = null
    private lateinit var adapter: QuizListAdapter
    private lateinit var quizOfSelectedCategory: List<Quiz>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_quiz_choice, container, false)

        rootView.categoryName.text = QuizRepository.categoriesList.find { c -> c.id == categoryId }?.name
                ?: ""

        quizOfSelectedCategory = QuizRepository.quizList.filter { q -> q.categories.size == 1 && q.categories.first().id == categoryId }

        // first selected tab - easy
        adapter = QuizListAdapter(quizOfSelectedCategory.filter { q -> q.level.id == 1 }, this)
        rootView.quizListView.adapter = adapter
        rootView.quizListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rootView.difficultyTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    adapter.dataSet = quizOfSelectedCategory.filter { q -> q.level.id == it.position + 1 }
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
        intent.putExtra("quiz", Json.encodeToString(quizOfSelectedCategory[position]))
        startActivity(intent)
    }

    companion object {
        const val TAG = "quizChoiceFragment"

        @JvmStatic
        fun newInstance(categoryId: Int) =
                QuizChoiceFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_CATEGORY_ID, categoryId)
                    }
                }
    }

}