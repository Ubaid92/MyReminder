package com.ubaid.myreminder.fragments

import androidx.fragment.app.Fragment
import com.ubaid.myreminder.MainActivity

open class BaseFragment(layoutId:Int):Fragment(layoutId) {

    fun addFragment(fragment: BaseFragment){
        (requireActivity() as MainActivity).add(fragment)
    }

    fun replaceFragment(fragment: BaseFragment){
        (requireActivity() as MainActivity).replace(fragment)
    }
    fun replaceFragmentAndAddToBackStack(fragment: BaseFragment){
        (requireActivity() as MainActivity).replaceAndAdd(fragment)
    }

    fun popFragment() {
        requireActivity().supportFragmentManager.popBackStack()

    }

}