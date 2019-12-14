package com.example.mypagination.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypagination.R
import com.example.mypagination.db.Person
import kotlinx.android.synthetic.main.item.view.*

class MyAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<Person, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyHolder).bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        (holder as MyHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyHolder(view)
    }

    companion object {
        private val PAYLOAD_SCORE = Any()
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: Person, newItem: Person): Any? {
                return if (sameExceptScore(oldItem, newItem)) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }

        private fun sameExceptScore(oldItem: Person, newItem: Person): Boolean {
            return oldItem.copy(score = newItem.score) == newItem
        }
    }


    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(post: Person?) {
            itemView.name.setText(post?.login)
        }
    }
}