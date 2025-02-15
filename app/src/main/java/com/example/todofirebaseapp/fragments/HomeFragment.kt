package com.example.todofirebaseapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todofirebaseapp.databinding.FragmentHomeBinding
import com.example.todofirebaseapp.utils.ToDoAdapter
import com.example.todofirebaseapp.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(),
    AddToDoFragment.DialogAddToDoButtonListener,
    ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ToDoAdapter
    private var mList: MutableList<ToDoData> = mutableListOf()
    private var firebaseListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("tasks")
            .child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter

        getDataFromFirebase()
    }

    private fun registerEvents() {
        binding.addToDoBtn.setOnClickListener {
            val addToDoFragment = AddToDoFragment()
            addToDoFragment.setListener(this)
            addToDoFragment.show(childFragmentManager, "AddToDoFragment")
        }
    }

    private fun getDataFromFirebase() {
        firebaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newList = mutableListOf<ToDoData>()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        newList.add(todoTask)
                    }
                }

                mList.clear()
                mList.addAll(newList)
                adapter.notifyDataSetChanged()

                binding.noTasksTextView.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        databaseRef.addValueEventListener(firebaseListener!!)
    }

    override fun onSaveTask(task: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(task).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                todoEt.text = null
            } else {
                Toast.makeText(context, it.exception?.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData, position: Int) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                adapter.removeTask(position)
                binding.noTasksTextView.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
                Toast.makeText(context, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        val editDialog = AddToDoFragment()
        editDialog.setListener(object : AddToDoFragment.DialogAddToDoButtonListener {
            override fun onSaveTask(task: String, todoEt: TextInputEditText) {
                databaseRef.child(toDoData.taskId).setValue(task).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Update Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                    editDialog.dismiss()
                }
            }
        })

        val bundle = Bundle()
        bundle.putString("taskText", toDoData.task)
        editDialog.arguments = bundle
        editDialog.show(childFragmentManager, "EditToDoFragment")
    }


    override fun onCompleteTaskBtnClicked(toDoData: ToDoData, position: Int) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                adapter.removeTask(position)
                binding.noTasksTextView.visibility = if (mList.isEmpty()) View.VISIBLE else View.GONE
                Toast.makeText(context, "Task completed successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firebaseListener?.let { databaseRef.removeEventListener(it) }
    }
}
