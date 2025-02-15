package com.example.todofirebaseapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todofirebaseapp.R
import com.example.todofirebaseapp.databinding.FragmentSignINBinding
import com.example.todofirebaseapp.databinding.FragmentSignUPBinding
import com.google.firebase.auth.FirebaseAuth

class SignINFragment : Fragment() {
    private lateinit var binding : FragmentSignINBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var navControl : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignINBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }
    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun registerEvents() {
        binding.newUserText.setOnClickListener {
            navControl.navigate(R.id.action_signINFragment_to_signUPFragment)
        }



        binding.signINButton.setOnClickListener {
            val email = binding.etMailInput2.text.toString().trim()
            val pass = binding.etPasswordInput.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progress.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Sign In Successful", Toast.LENGTH_SHORT).show()
                        navControl.navigate(R.id.action_signINFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), "User Does Not Exist", Toast.LENGTH_SHORT).show()
                    }
                    binding.progress.visibility = View.INVISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }


        }
    }

}