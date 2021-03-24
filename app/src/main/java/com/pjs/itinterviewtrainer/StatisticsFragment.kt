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

    private lateinit var listAdapter: StatisticsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_statistics, container, false)
        listAdapter = StatisticsListAdapter(QuizRepository.categoriesList, this)
        rootView.categoriesListView.adapter = listAdapter
        rootView.categoriesListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        return rootView
    }

    override fun onItemClick(position: Int) {
        with(requireActivity().supportFragmentManager.beginTransaction()) {
            replace(R.id.container, QuizChoiceFragment.newInstance(listAdapter.dataSet[position].id), QuizChoiceFragment.TAG)
            addToBackStack(QuizChoiceFragment.TAG)
            commit()
        }
    }
}