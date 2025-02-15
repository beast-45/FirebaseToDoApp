package com.example.todofirebaseapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todofirebaseapp.R
import com.example.todofirebaseapp.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private val handler = Handler(Looper.getMainLooper())
    private val splashRunnable = Runnable {
        if (auth.currentUser != null) {
            navControl.navigate(R.id.action_splashScreenFragment_to_homeFragment)
        } else {
            navControl.navigate(R.id.action_splashScreenFragment_to_signINFragment2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        handler.postDelayed(splashRunnable, 2000)
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(splashRunnable)
    }
}
