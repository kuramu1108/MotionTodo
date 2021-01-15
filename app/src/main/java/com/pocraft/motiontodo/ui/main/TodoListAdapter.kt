package com.pocraft.motiontodo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pocraft.motiontodo.R
import com.pocraft.motiontodo.databinding.TodoItemBinding
import com.pocraft.motiontodo.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class TodoListAdapter(
    private val onItemRemove: ((Item) -> Unit)? = null
) : ListAdapter<Item, TodoListViewHolder>(DiffCallBack) {
    private val awaitListChanged: suspend () -> Unit = {
        suspendCancellableCoroutine<Int> { cont ->
            val observer = object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    unregisterAdapterDataObserver(this)
                    cont.resume(positionStart)
                }
            }
            cont.invokeOnCancellation { unregisterAdapterDataObserver(observer) }
            registerAdapterDataObserver(observer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoListViewHolder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) =
        holder.bind(currentList[position], onItemRemove, awaitListChanged)

    companion object {
        object DiffCallBack : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }
    }
}

class TodoListViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Item,
        onItemRemove: ((Item) -> Unit)? = null,
        awaitListChanged: suspend () -> Unit
    ) {
        with(binding) {
            todoTitle.text = item.title
            todoTitle.setOnClickListener {
                onItemRemove?.invoke(item)
            }
            todoRemove.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    todoItem.progress = 0f
                    todoItem.setTransition(R.id.start, R.id.end)
                    onItemRemove?.invoke(item)
                    awaitListChanged()

                }
            }
        }
    }
}
