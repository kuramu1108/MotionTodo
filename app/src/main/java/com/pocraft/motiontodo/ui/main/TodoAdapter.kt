package com.pocraft.motiontodo.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pocraft.motiontodo.R
import com.pocraft.motiontodo.databinding.TodoItemBinding
import com.pocraft.motiontodo.model.Item

class TodoAdapter(
    private val onItemRemoveClicked: ((Item) -> Unit)? = null
) : ListAdapter<Item, TodoViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) =
        holder.bind(currentList[position], onItemRemoveClicked)

    companion object {
        object DiffCallBack : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
        }
    }
}

class TodoViewHolder(private val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Item,
        onItemRemoveClicked: ((Item) -> Unit)? = null
    ) {
        with(binding) {
            todoTitle.text = item.title
            todoRemove.setOnClickListener {
                onItemRemoveClicked?.invoke(item)
                Log.d("POTEST", todoItem.progress.toString())
                todoItem.progress = 0f
                todoItem.setTransition(R.id.start, R.id.end)
            }
        }
    }
}
