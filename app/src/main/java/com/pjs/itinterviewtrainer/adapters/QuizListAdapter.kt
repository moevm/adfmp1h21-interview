package com.pjs.itinterviewtrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pjs.itinterviewtrainer.R
import com.pjs.itinterviewtrainer.data.entities.Quiz
import com.pjs.itinterviewtrainer.data.entities.QuizWithQuestions
import kotlinx.android.synthetic.main.quiz_item.view.*

class QuizListAdapter(var dataSet: List<QuizWithQuestions>, private val onItemClickListener: OnQuizClickListener) :
        RecyclerView.Adapter<QuizListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val itemClickListener: OnQuizClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleView: TextView = view.findViewById(R.id.titleView)
        val questionsAmountView: TextView = view.findViewById(R.id.questionsAmountView)
        val passedTimeView: TextView = view.findViewById(R.id.timeView)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.quiz_item, viewGroup, false)

        return ViewHolder(view, onItemClickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataSet[position]
        viewHolder.titleView.text = data.quiz.quizName
        viewHolder.questionsAmountView.text = "${data.questions.size} questions"
        viewHolder.passedTimeView.text = "60 min" // add real test time
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    interface OnQuizClickListener {
        fun onItemClick(position: Int)
    }
}
