package com.ubaid.myreminder.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaid.myreminder.R
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.DailyListItemsBinding
import com.ubaid.myreminder.util.DateUtils

class TodayReminderAdapter : RecyclerView.Adapter<TodayReminderAdapter.ViewHolder>() {

    var todayReminderList = arrayListOf<ReminderData>()
    var selectionList = arrayListOf<ReminderData>()
    lateinit var isCompletedListener: (ReminderData, Boolean) -> Unit

    inner class ViewHolder(private var binding: DailyListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun hold(reminderData: ReminderData) {
            binding.title.text = reminderData.title
            binding.time.text = DateUtils.getFormattedTime(reminderData.time)
            binding.categoryImg.isChecked = reminderData.isDone.apply {
                binding.status.setImageResource(
                    if (this) R.drawable.check_mark
                    else R.drawable
                        .time
                )
            }

            binding.categoryImg.setBackgroundResource(
                when (reminderData.priority) {
                    "High" -> R.drawable.bg_high
                    "Medium" -> R.drawable.bg_med
                    else -> R.drawable.bg_low
                }
            )
            binding.notificationIcon.visibility = if (reminderData.isAlert) View.VISIBLE else
                View.INVISIBLE
            binding.categoryImg.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.title.setTypeface(binding.title.typeface, Typeface.ITALIC)
                    binding.time.setTypeface(binding.time.typeface, Typeface.ITALIC)
                    binding.status.setImageResource(R.drawable.check_mark)
                } else {
                    binding.title.setTypeface(binding.title.typeface, Typeface.BOLD)
                    binding.time.typeface = null
                    binding.status.setImageResource(R.drawable.time)
                }
                isCompletedListener(reminderData, isChecked)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DailyListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todayReminderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hold(todayReminderList[position])
    }

}