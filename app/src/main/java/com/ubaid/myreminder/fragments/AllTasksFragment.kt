package com.ubaid.myreminder.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.adapter.TodayReminderAdapter
import com.ubaid.myreminder.databinding.AllTasksBinding

class AllTasksFragment:BaseFragment(R.layout.all_tasks) {
    lateinit var binding: AllTasksBinding
    private lateinit var reminderViewModel: ReminderViewModel
    private var todayAdapter = TodayReminderAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AllTasksBinding.bind(view)

        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]
        binding.recyclerView.adapter = todayAdapter
        reminderViewModel.reminderTaskLiveData.observe(viewLifecycleOwner){
            todayAdapter.todayReminderList = it
            todayAdapter.notifyDataSetChanged()
        }
        todayAdapter.isCompletedListener = {reminderData, isChecked->
            reminderData.isDone = isChecked
            reminderViewModel.update(reminderData)
        }
        binding.backButton.setOnClickListener {
            popFragment()
        }

    }
}