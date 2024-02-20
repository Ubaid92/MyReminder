package com.ubaid.myreminder.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaid.myreminder.R
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.DailyListItemsBinding
import com.ubaid.myreminder.util.DateUtils

class TodayReminderAdapter : RecyclerView.Adapter<TodayReminderAdapter.ViewHolder>() {

    var todayReminderList = arrayListOf<ReminderData>()
    lateinit var isCompletedListener:(ReminderData, Boolean)->Unit

    inner class ViewHolder(private var binding: DailyListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun hold(reminderData: ReminderData) {
            binding.title.text = reminderData.title
            binding.time.text = DateUtils.getFormattedTime(reminderData.time)
            binding.checkbox.isChecked = reminderData.isDone
            binding.categoryImg.setImageResource(reminderData.priority)
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                val context = binding.root.context
                if (isChecked){
                    binding.root.setCardBackgroundColor(context.getColor(R.color.gray))
                    binding.title.setTypeface(binding.title.typeface, Typeface.ITALIC)
                    binding.time.setTypeface(binding.time.typeface, Typeface.ITALIC)
                } else {
                    binding.root.setCardBackgroundColor(context.getColor(R.color.white))
                    binding.title.setTypeface(binding.title.typeface, Typeface.BOLD)
                    binding.time.typeface = null
                }
                isCompletedListener(reminderData,isChecked)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DailyListItemsBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todayReminderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hold(todayReminderList[position])
    }

}