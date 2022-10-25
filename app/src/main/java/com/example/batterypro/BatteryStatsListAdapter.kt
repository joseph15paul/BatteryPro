package com.example.batterypro

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BatteryStatsListAdapter : ListAdapter<batteryStats, BatteryStatsListAdapter.BatteryViewHolder>(BatteryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatteryViewHolder {
        return BatteryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BatteryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.batteryLevel.toString(), current.charging.toString(), current.timestamp)

    }

    class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val batteryStatsChargeItemView: TextView = itemView.findViewById(R.id.textVieww)
        private val batteryStatsStateItemView: TextView = itemView.findViewById(R.id.chargingState)
        private val batteryStatsTimeStampItemView: TextView = itemView.findViewById(R.id.timestamp)


        fun bind(charge: String?, state: String?, time: String?) {
            batteryStatsChargeItemView.text = charge
            batteryStatsStateItemView.text = state
            batteryStatsTimeStampItemView.text = time
        }

        companion object {
            fun create(parent: ViewGroup): BatteryViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return BatteryViewHolder(view)
            }
        }
    }

    class BatteryComparator : DiffUtil.ItemCallback<batteryStats>() {
        override fun areItemsTheSame(oldItem: batteryStats, newItem: batteryStats): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: batteryStats, newItem: batteryStats): Boolean {
            return oldItem.batteryLevel == newItem.batteryLevel
        }
    }
}