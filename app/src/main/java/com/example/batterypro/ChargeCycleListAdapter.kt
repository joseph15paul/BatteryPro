package com.example.batterypro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChargeCycleListAdapter(private val mChargeCycles: List<MainActivity.ChargeCycle>):  RecyclerView.Adapter<ChargeCycleListAdapter.ChargeCycleViewHolder>()  {


    inner class ChargeCycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        public val dischargeTextView: TextView = itemView.findViewById<TextView>(R.id.textViewww)
        val timeTextView: TextView = itemView.findViewById<TextView>(R.id.chargingStatee)
        val timeFrameTextView: TextView = itemView.findViewById<TextView>(R.id.timestampp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargeCycleListAdapter.ChargeCycleViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.charg_cycle_item, parent, false)
        // Return a new holder instance
        return ChargeCycleViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ChargeCycleListAdapter.ChargeCycleViewHolder, position: Int) {
        // Get the data model based on position
        val chageCycle: MainActivity.ChargeCycle = mChargeCycles[position]
        // Set item views based on your views and data model
        val textView = viewHolder.dischargeTextView
        textView.text = chageCycle.totDischarge.toString()
        val time = viewHolder.timeTextView
        time.text = chageCycle.totTime.toString()
        val frame = viewHolder.timeFrameTextView
        frame.text = chageCycle.frame

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mChargeCycles.size
    }
}