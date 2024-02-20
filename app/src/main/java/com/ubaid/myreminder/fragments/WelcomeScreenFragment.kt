package com.ubaid.myreminder.fragments

import android.os.Bundle
import android.view.View
import com.ubaid.myreminder.R
import com.ubaid.myreminder.databinding.WelcomeScreenFragmentBinding

class WelcomeScreenFragment:BaseFragment(R.layout.welcome_screen_fragment) {
    private lateinit var binding: WelcomeScreenFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WelcomeScreenFragmentBinding.bind(view)

        binding.btnStart.setOnClickListener {
            replaceFragment(HomePageFragment())
        }
    }
}