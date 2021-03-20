package com.pjs.itinterviewtrainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.pjs.itinterviewtrainer.adapters.QuizListAdapter
import kotlinx.android.synthetic.main.fragment_quiz_choice.view.*

private const val ARG_CATEGORY_ID = "categoryId"

class QuizChoiceFragment : Fragment(), QuizListAdapter.OnQuizClickListener {
    // TODO: Rename and change types of parameters
    private var categoryId: Int? = null
    private val initQuizMap: Map<Int, List<String>> = mapOf(0 to listOf("a", "b"), 1 to listOf("c", "d"), 2 to listOf("e", "f"))
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_quiz_choice, container, false)

        adapter = QuizListAdapter(initQuizMap[0] ?: listOf(), this)
        rootView.quizListView.adapter = adapter
        rootView.quizListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)

        rootView.difficultyTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    Toast.makeText(activity, tab.position.toString(), Toast.LENGTH_SHORT).show()
                    adapter.dataSet = initQuizMap[it.position] ?: listOf()
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

    override fun onItemClick(position: Int) {}

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