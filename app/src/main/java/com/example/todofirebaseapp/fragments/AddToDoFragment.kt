package com.example.todofirebaseapp.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todofirebaseapp.databinding.FragmentAddToDoBinding
import com.google.android.material.textfield.TextInputEditText

class AddToDoFragment : DialogFragment() {
    private lateinit var binding: FragmentAddToDoBinding
    private var listener: DialogAddToDoButtonListener? = null

    fun setListener(listener: DialogAddToDoButtonListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val existingTask = arguments?.getString("taskText")
        if (!existingTask.isNullOrEmpty()) {
            binding.etAddTaskInput.setText(existingTask)
        }

        registerEvents()
    }

    private fun registerEvents() {
        binding.addToDoButton.setOnClickListener {
            val task = binding.etAddTaskInput.text.toString().trim()
            if (task.isNotEmpty()) {
                listener?.onSaveTask(task, binding.etAddTaskInput)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawableResource(android.R.color.transparent)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    interface DialogAddToDoButtonListener {
        fun onSaveTask(task: String, todoEt: TextInputEditText)
    }
}
