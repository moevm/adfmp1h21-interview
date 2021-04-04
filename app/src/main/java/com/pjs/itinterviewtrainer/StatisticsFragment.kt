package com.pjs.itinterviewtrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjs.itinterviewtrainer.adapters.StatisticsListAdapter
import com.pjs.itinterviewtrainer.data.QuizRepository
import kotlinx.android.synthetic.main.fragment_statistics.view.*

class StatisticsFragment : Fragment(), StatisticsListAdapter.OnItemClickListener {
    companion object {
        const val TAG = "statisticsFragment"

        @JvmStatic
        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }

    private lateinit var repository: QuizRepository
    private lateinit var listAdapter: StatisticsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_statistics, container, false)
        repository = QuizRepository(requireContext().applicationContext)
        listAdapter = StatisticsListAdapter(repository.getResults(), this)
        rootView.quizResultsListView.adapter = listAdapter
        rootView.quizResultsListView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return rootView
    }

    override fun onItemClick(position: Int) {}
}