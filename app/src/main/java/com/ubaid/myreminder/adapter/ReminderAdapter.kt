package com.ubaid.myreminder.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ubaid.myreminder.R
import com.ubaid.myreminder.data.ReminderData
import com.ubaid.myreminder.databinding.DailyListItemsBinding
import com.ubaid.myreminder.util.DateUtils1

class ReminderAdapter : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    var todayReminderList = arrayListOf<ReminderData>()
    var selectionList = arrayListOf<ReminderData>()
    lateinit var isCompletedListener: (ReminderData, Boolean) -> Unit
    lateinit var isAlarmListener: (ReminderData, Boolean) -> Unit
    lateinit var isSelectingListener: (Boolean) -> Unit
    var isSelecting: Boolean = false
        set(value) {
            field = value
            if (!field) {
                selectionList.clear()
                isSelectingListener(false)
                notifyDataSetChanged()
            }
        }

    inner class ViewHolder(private var binding: DailyListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun hold(reminderData: ReminderData, position: Int) {
            val context = binding.root.context
            binding.title.text = reminderData.title
            binding.time.text = DateUtils1.getFormattedTimeAndDate(reminderData.time)
            binding.root.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (selectionList.contains(reminderData))
                        R.color.lite_blue
                    else
                        R.color.white
                )
            )
            binding.categoryImg.isChecked = reminderData.isDone.apply {
                binding.status.setImageResource(
                    if (this) R.drawable.check_mark
                    else R.drawable
                        .time
                )
            }

            binding.notificationIcon.setOnCheckedChangeListener { _, isChecked ->
                binding.notificationIcon.setBackgroundResource(
                    if (isChecked) R.drawable.notification_icon
                    else R.drawable.disabled
                )
                isAlarmListener(reminderData, isChecked)
            }

            binding.categoryImg.setBackgroundResource(
                when (reminderData.priority) {
                    "High" -> R.drawable.bg_high
                    "Medium" -> R.drawable.bg_med
                    else -> R.drawable.bg_low
                }
            )

            binding.root.setOnClickListener {
                if (selectionList.any { it.id == reminderData.id }) {
                    selectionList.remove(reminderData)
                    checkIfSelecting(position)
                } else if (isSelecting) {
                    selectionList.add(reminderData)
                    checkIfSelecting(position)
                }
            }

            binding.root.setOnLongClickListener {
                selectionList.add(reminderData)
                checkIfSelecting(position)
                true
            }

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

        private fun checkIfSelecting(position: Int) {
            isSelecting = selectionList.isNotEmpty()
            isSelectingListener(isSelecting)
            notifyItemChanged(position)
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
        holder.hold(todayReminderList[position], position)
    }

}