package com.pjs.itinterviewtrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjs.itinterviewtrainer.adapters.CategoryListAdapter
import com.pjs.itinterviewtrainer.models.QuizCategory
import kotlinx.android.synthetic.main.fragment_categories_choice.view.*

class CategoriesChoiceFragment : Fragment(), CategoryListAdapter.OnItemClickListener {
    companion object{
        const val TAG = "categoriesChoiceFragment"

        @JvmStatic
        fun newInstance(): CategoriesChoiceFragment {
            return CategoriesChoiceFragment()
        }
    }

    private val initCategoriesList: List<QuizCategory> = ('a'..'z').toList().mapIndexed{ id, c -> QuizCategory(id, c.toString())}
    private lateinit var listAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView =  inflater.inflate(R.layout.fragment_categories_choice, container, false)
        listAdapter = CategoryListAdapter(initCategoriesList, this)
        rootView.categoriesListView.adapter = listAdapter
        rootView.categoriesListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)

        rootView.searchCategory.addTextChangedListener {
            listAdapter.dataSet = initCategoriesList.filter { c -> c.name.contains(it.toString(), ignoreCase=true) }
            listAdapter.notifyDataSetChanged()
        }
        return rootView
    }

    override fun onItemClick(position: Int) {
        with(requireActivity().supportFragmentManager.beginTransaction()){
            replace(R.id.container, QuizChoiceFragment.newInstance(listAdapter.dataSet[position].id), QuizChoiceFragment.TAG)
            addToBackStack(QuizChoiceFragment.TAG)
            commit()
        }
    }
}