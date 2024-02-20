package com.ubaid.myreminder.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.databinding.LoginPageFragmentBinding
import com.ubaid.myreminder.fragments.BaseFragment

class LogInPageFragment : BaseFragment(R.layout.login_page_fragment) {
    private lateinit var binding: LoginPageFragmentBinding
    val fireBaseAuth = FireBaseAuth()
    lateinit var reminderViewModel:ReminderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageFragmentBinding.bind(view)
        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]

        binding.backButton.setOnClickListener {
            popFragment()
        }
        binding.googleSignIn.setOnClickListener {
            fireBaseAuth.auth(requireActivity())

        }

        binding.googleSignOut.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(context, "Sign out successfully", Toast.LENGTH_SHORT).show()
            popFragment()

        }

    }
}