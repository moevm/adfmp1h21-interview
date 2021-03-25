package com.pjs.itinterviewtrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pjs.itinterviewtrainer.R
import com.pjs.itinterviewtrainer.data.QuestionCategory

class CategoryListAdapter(var dataSet: List<QuestionCategory>, private val onItemClickListener: OnItemClickListener) :
        RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView)

        init {
            textView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(adapterPosition)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view, onItemClickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
