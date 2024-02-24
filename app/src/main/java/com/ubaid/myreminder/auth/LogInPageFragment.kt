package com.ubaid.myreminder.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.databinding.LoginPageFragmentBinding
import com.ubaid.myreminder.fragments.BaseFragment

class LogInPageFragment : BaseFragment(R.layout.login_page_fragment) {
    private lateinit var binding: LoginPageFragmentBinding
    private val appAuthManager = AppAuthManager()
    private lateinit var reminderViewModel: ReminderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginPageFragmentBinding.bind(view)
        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]

        if (appAuthManager.isLoggedIn()) {
            binding.googleSignIn.visibility = View.GONE
            binding.googleSignOut.visibility = View.VISIBLE
        } else {
            binding.googleSignIn.visibility = View.VISIBLE
            binding.googleSignOut.visibility = View.GONE
        }

        binding.backButton.setOnClickListener {
            popFragment()
        }

        binding.googleSignIn.setOnClickListener {
            appAuthManager.googleSignIn(requireActivity())
        }

        binding.googleSignOut.setOnClickListener {
            appAuthManager.signOut()
            Toast.makeText(context, "Sign out successfully", Toast.LENGTH_SHORT).show()
            popFragment()
        }

    }
}