package com.pocraft.motiontodo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pocraft.motiontodo.databinding.TodoItemBinding

class TodoAdapter(
    private var data: List<String> = emptyList()
) : RecyclerView.Adapter<TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder) {
            binding.todoTitle.text = data[position]
        }
    }

    fun update(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }
}

class TodoViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)