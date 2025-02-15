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
import com.example.todofirebaseapp.databinding.FragmentSignUPBinding
import com.google.firebase.auth.FirebaseAuth

class SignUPFragment : Fragment() {
    private lateinit var binding : FragmentSignUPBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var navControl : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUPBinding.inflate(layoutInflater, container, false)
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
        binding.alreadyRegisteredSignInText.setOnClickListener {
            navControl.navigate(R.id.action_signUPFragment_to_signINFragment)
        }



        binding.signUPButton.setOnClickListener {
            val email = binding.etMailInput.text.toString().trim()
            val pass = binding.etPasswordInput1.text.toString().trim()
            val confirmPass = binding.etPasswordInput2.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    binding.progress.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()
                            navControl.navigate(R.id.action_signUPFragment_to_homeFragment)
                        }
                        else{
                            Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                        binding.progress.visibility = View.INVISIBLE
                    }
                } else {
                    Toast.makeText(requireContext(), "Password don't Match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }

        }
    }

}