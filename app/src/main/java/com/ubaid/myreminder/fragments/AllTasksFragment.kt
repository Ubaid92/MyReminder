package com.ubaid.myreminder.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.adapter.ReminderAdapter
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.AllTasksBinding
import kotlin.math.roundToInt

class AllTasksFragment : BaseFragment(R.layout.all_tasks) {
    lateinit var binding: AllTasksBinding
    private lateinit var reminderViewModel: ReminderViewModel
    private var todayAdapter = ReminderAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AllTasksBinding.bind(view)
        setupListeners()

        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]
        binding.recyclerView.adapter = todayAdapter
        reminderViewModel.reminderTaskLiveData.observe(viewLifecycleOwner) {
            todayAdapter.todayReminderList = it
            todayAdapter.notifyDataSetChanged()
            updateUserProgress(it)
        }
        todayAdapter.isCompletedListener = { reminderData, isChecked ->
            reminderData.isDone = isChecked
            reminderViewModel.update(reminderData)
        }
        todayAdapter.isAlarmListener = { reminderData, isChecked ->
            reminderData.isAlert = isChecked
            reminderViewModel.update(reminderData)
        }
        binding.backButton.setOnClickListener {
            popFragment()
        }

        binding.editReminderButton.setOnClickListener {
            if (todayAdapter.isSelecting) {
                todayAdapter.selectionList.map { reminder ->
                    reminderViewModel.editReminder.postValue(reminder)
                    addFragment(NewTaskFragment())
                }
                todayAdapter.isSelecting = false
            }
        }

        binding.deleteReminderButton.setOnClickListener {

            AlertDialog.Builder(requireContext()).setTitle("Delete Reminder").setMessage("Are you sure want to delete this reminder")
                .setPositiveButton("Yes"){_,_ ->
                    if (todayAdapter.isSelecting) {
                        todayAdapter.selectionList.map { reminder ->
                            reminderViewModel.delete(reminder)
                        }
                        todayAdapter.isSelecting = false
                    }
                }.setNegativeButton("No"){d,_ ->
                    d.dismiss()
                }.create().show()

        }


    }

    private fun setupListeners() {
        todayAdapter.isSelectingListener = {
            binding.deleteReminderButton.visibility = if (it) View.VISIBLE else View.GONE
            binding.editReminderButton.visibility = if (
                todayAdapter.selectionList.size == 1
            ) View.VISIBLE else View.GONE
        }
    }

    private fun updateUserProgress(dataList: ArrayList<ReminderData>) {
        val taskCount = "${dataList.size} Tasks"
        binding.taskCount.text = taskCount
        val completed = dataList.count { it.isDone }
        if (dataList.isEmpty()) {
            binding.taskProgress.progress = 0
            binding.taskProgressText.text = "0%"
        } else {
            val updatedProgress = ((completed.toFloat() / dataList.size) * 100).roundToInt()
            val animator = ValueAnimator.ofInt(binding.taskProgress.progress, updatedProgress)
            animator.duration = 300
            animator.addUpdateListener {
                binding.taskProgress.progress = it.animatedValue as Int
                binding.taskProgressText.text = String.format("%2d%%", it.animatedValue as Int)
            }
            animator.start()
        }

    }
}
