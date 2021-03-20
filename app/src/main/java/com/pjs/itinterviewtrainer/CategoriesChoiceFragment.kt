package com.pjs.itinterviewtrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjs.itinterviewtrainer.models.QuizCategory
import kotlinx.android.synthetic.main.fragment_categories_choice.view.*
import java.util.*

class CategoriesChoiceFragment : Fragment(), CategoryAdapter.OnItemClickListener {
    companion object{
        const val TAG = "categoriesChoiceFragment"

        @JvmStatic
        fun newInstance(): CategoriesChoiceFragment {
            return CategoriesChoiceFragment()
        }
    }

    private val initCategoriesList: List<QuizCategory> = ('a'..'z').toList().mapIndexed{ id, c -> QuizCategory(id, c.toString())}
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView =  inflater.inflate(R.layout.fragment_categories_choice, container, false)
        adapter = CategoryAdapter(initCategoriesList, this)
        rootView.categoriesListView.adapter = adapter
        rootView.categoriesListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL ,false)

        rootView.searchCategory.addTextChangedListener {
            adapter.dataSet = initCategoriesList.filter { c -> c.name.contains(it.toString(), ignoreCase=true) }
            adapter.notifyDataSetChanged()
        }
        return rootView
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, adapter.dataSet[position].name, Toast.LENGTH_SHORT).show()
    }
}