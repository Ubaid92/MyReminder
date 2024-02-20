package com.ubaid.myreminder.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
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




        binding.datePickerActions.setOnClickListener {
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

        binding.timePickerActions.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                reminderViewModel.selectedTime.set(Calendar.HOUR_OF_DAY, hour)
                reminderViewModel.selectedTime.set(Calendar.MINUTE, minute)
                binding.timePickerActions.text =
                    DateUtils.getFormattedTime(reminderViewModel.selectedTime.time.time)
            }
            TimePickerDialog(
                requireContext(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.btnCreateReminder.setOnClickListener {

            val reminderTitle = binding.reminderNameInput.text.toString()
            if (reminderTitle.isEmpty() || binding.datePickerActions.text == "Select date"
                || binding.timePickerActions.text == "Time"
            ) {
                Toast.makeText(requireContext(), "Fill all data first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val priority = when (binding.radioGroup.checkedRadioButtonId) {
                R.id.priorityHigh -> R.drawable.high
                R.id.priorityMedium -> R.drawable.medium
                R.id.priorityLow -> R.drawable.low
                else -> 0

            }

            if (priority == 0) {
                Toast.makeText(requireContext(), "Select priority first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val reminderData = ReminderData(
                isDone = false,
                title = reminderTitle,
                time = reminderViewModel.selectedTime.time.time,
                priority = priority
            )
            reminderViewModel.save(reminderData)
            if (binding.addReminderCheckbox.isChecked){
                val alarmScheduler = AndroidAlarmScheduler(requireContext())
                alarmScheduler.schedule(reminderData)
            }

            popFragment()

        }


    }
}
