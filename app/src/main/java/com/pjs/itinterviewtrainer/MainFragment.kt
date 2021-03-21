package com.pjs.itinterviewtrainer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                Toast.makeText(activity, data?.getStringExtra("test")
                        ?: "smth bad", Toast.LENGTH_SHORT).show()
            }
        }

        rootView.startTestBtn.setOnClickListener {
            val intent = Intent(activity, QuizActivity::class.java)
            resultLauncher.launch(intent)
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