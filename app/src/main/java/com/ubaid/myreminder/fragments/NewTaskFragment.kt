package com.ubaid.myreminder.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel
import com.ubaid.myreminder.alarm.AndroidAlarmScheduler
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.NewTaskFragmentBinding
import com.ubaid.myreminder.util.DateUtils


class NewTaskFragment : BaseFragment(R.layout.new_task_fragment) {
    lateinit var reminderViewModel: ReminderViewModel
    private lateinit var binding: NewTaskFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewTaskFragmentBinding.bind(view)
        reminderViewModel = ViewModelProvider(requireActivity())[ReminderViewModel::class.java]
        binding.backButton.setOnClickListener {
            popFragment()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            hideKeyboard()
        }

        binding.datePickerActions.setOnClickListener {
            hideKeyboard()
            showDatePicker()
        }

        binding.timePickerActions.setOnClickListener {
            hideKeyboard()
            showTimePicker()
        }

        binding.btnCreateReminder.setOnClickListener {
            hideKeyboard()
            val reminderTitle = binding.reminderNameInput.text.toString()
            if (reminderTitle.isEmpty() || binding.datePickerActions.text == "Select date"
                || binding.timePickerActions.text == "Time"
            ) {
                Toast.makeText(requireContext(), "Fill all data first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val priority = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.priorityHigh -> "High"
                R.id.priorityMedium -> "Medium"
                R.id.priorityLow -> "Low"
                else -> ""

            }

            if (priority.isEmpty()) {
                Toast.makeText(requireContext(), "Select priority first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val reminderData = ReminderData(
                isDone = false,
                title = reminderTitle,
                time = reminderViewModel.selectedTime.time.time,
                priority = priority,
                isAlert = binding.addReminderCheckbox.isChecked
            )
            setupAlarmIfRequired(reminderData)
            reminderViewModel.save(reminderData)

            popFragment()

        }

    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            requireActivity(),
            { _, cYear, monthOfYear, dayOfMonth ->
                reminderViewModel.selectedTime.set(Calendar.YEAR, cYear)
                reminderViewModel.selectedTime.set(Calendar.MONTH, monthOfYear)
                reminderViewModel.selectedTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.datePickerActions.text =
                    DateUtils.getFormattedDate(reminderViewModel.selectedTime)
            },
            year,
            month,
            day
        )

        dpd.show()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            reminderViewModel.selectedTime.set(Calendar.HOUR_OF_DAY, hour)
            reminderViewModel.selectedTime.set(Calendar.MINUTE, minute)
            reminderViewModel.selectedTime.set(Calendar.SECOND, 0)
            binding.timePickerActions.text =
                DateUtils.getFormattedTime(reminderViewModel.selectedTime.time.time)
        }
        TimePickerDialog(
            requireContext(),
            timeSetListener,
            cal[Calendar.HOUR_OF_DAY],
            cal[Calendar.MINUTE],
            false
        ).show()
    }

    private fun setupAlarmIfRequired(reminderData: ReminderData) {
        if (binding.addReminderCheckbox.isChecked) {
            val alarmScheduler = AndroidAlarmScheduler(requireContext())
            alarmScheduler.schedule(reminderData)
        }
    }
    fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
