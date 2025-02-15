package com.example.todofirebaseapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todofirebaseapp.databinding.EachItemBinding

class ToDoAdapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var listener: ToDoAdapterClicksInterface? = null

    fun setListener(listener: ToDoAdapterClicksInterface) {
        this.listener = listener
    }

    inner class ToDoViewHolder(val binding: EachItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.itemTextView.text = currentItem.task

        holder.binding.deleteTask.setOnClickListener {
            listener?.onDeleteTaskBtnClicked(currentItem, position)
        }

        holder.binding.editTask.setOnClickListener {
            listener?.onEditTaskBtnClicked(currentItem)
        }


        holder.binding.markCompleteTask.setOnClickListener {
            listener?.onCompleteTaskBtnClicked(currentItem, position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeTask(position: Int) {
        if (position in list.indices) {
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    interface ToDoAdapterClicksInterface {
        fun onDeleteTaskBtnClicked(toDoData: ToDoData, position: Int)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
        fun onCompleteTaskBtnClicked(toDoData: ToDoData, position: Int)
    }
}

