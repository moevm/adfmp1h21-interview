package com.pjs.itinterviewtrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        rootView.startTestBtn.setOnClickListener {
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                replace(R.id.container, QuizSetupFragment.newInstance(), QuizSetupFragment.TAG)
                addToBackStack(QuizSetupFragment.TAG)
                commit()
            }
        }

        rootView.categoriesBtn.setOnClickListener {
            with(requireActivity().supportFragmentManager.beginTransaction()) {
                replace(R.id.container, CategoriesChoiceFragment.newInstance(), CategoriesChoiceFragment.TAG)
                addToBackStack(CategoriesChoiceFragment.TAG)
                commit()
            }
        }

        return rootView
    }

    companion object {
        const val TAG = "mainFragment"

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}