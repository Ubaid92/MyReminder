package com.ubaid.myreminder.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.adapter.ReminderAdapter
import com.ubaid.myreminder.auth.LogInPageFragment
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.HomePageFragmentBinding
import com.ubaid.myreminder.util.DateUtils1
import java.util.Calendar
import kotlin.math.roundToInt

class HomePageFragment : BaseFragment(R.layout.home_page_fragment) {
    private lateinit var binding: HomePageFragmentBinding
    private lateinit var reminderViewModel: ReminderViewModel
    private var todayAdapter = ReminderAdapter()
    var todayDate = Calendar.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomePageFragmentBinding.bind(view)
        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]
        binding.recyclerView.adapter = todayAdapter
        setupListeners()

        if (Firebase.auth.currentUser != null) {
            reminderViewModel.getDataFromId()
            binding.userNameText.text = "Hi ${Firebase.auth.currentUser?.displayName}"
            Glide.with(this).load(Firebase.auth.currentUser?.photoUrl).into(binding.loginIcon)
        } else {
            binding.userNameText.text = "Welcome"
        }

    }

    private fun setupListeners() {
        todayAdapter.isSelectingListener = {
            binding.deleteReminderButton.visibility = if (it) View.VISIBLE else View.GONE
            binding.editReminderButton.visibility = if (
                todayAdapter.selectionList.size == 1
            ) View.VISIBLE else View.GONE
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
                .setPositiveButton("Yes") { _, _ ->
                    if (todayAdapter.isSelecting) {
                        todayAdapter.selectionList.map { reminder ->
                            reminderViewModel.delete(reminder)
                        }
                        todayAdapter.isSelecting = false
                    }
                }.setNegativeButton("No") { d, _ ->
                    d.dismiss()
                }.create().show()

        }
        todayAdapter.isCompletedListener = { reminderData, isChecked ->
            reminderData.isDone = isChecked
            reminderViewModel.update(reminderData)

        }
        todayAdapter.isAlarmListener = { reminderData, isChecked ->
            reminderData.isAlert = isChecked
            reminderViewModel.update(reminderData)
        }
        reminderViewModel.reminderTaskLiveData.observe(viewLifecycleOwner) { dataList ->

            var today = dataList.filter {
                DateUtils.isToday(it.time)
            }
            todayAdapter.todayReminderList = today as ArrayList<ReminderData>
            todayAdapter.notifyDataSetChanged()
            updateUserProgress(today)
        }

        binding.newReminderBtn.setOnClickListener {
            addFragment(NewTaskFragment())
        }

        binding.btnSeeAll.setOnClickListener {
            addFragment(AllTasksFragment())
        }
        binding.header.setOnClickListener {
            replaceFragmentAndAddToBackStack(LogInPageFragment())
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