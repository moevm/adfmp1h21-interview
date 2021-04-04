package com.pjs.itinterviewtrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pjs.itinterviewtrainer.R
import com.pjs.itinterviewtrainer.data.entities.QuizResults

class StatisticsListAdapter(
    var dataSet: List<QuizResults>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<StatisticsListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val itemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
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
        viewHolder.titleView.text = data.quizTitle
        viewHolder.questionsAmountView.text =
            "${data.correct}/${data.correct + data.wrong} questions"
        viewHolder.passedTimeView.text =
            "${String.format("%.1f", data.passedTimeInSeconds.toFloat() / 60)} min"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
