package com.example.batterypro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(){

    private val batteryViewModel: BatteryViewModel by viewModels {
        BatteryStatsViewModelFactory((application as BatteryStatsApplication).repository)
    }
    var dataList:List<batteryStats> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = BatteryStatsListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        batteryViewModel.allBatteryStats.observe(this, Observer { batteryStatus ->
            // Update the cached copy of the words in the adapter.
            batteryStatus?.let { adapter.submitList(it)
            dataList = it}
        })

        val chargeCycleRecyclerView = findViewById<RecyclerView>(R.id.chargeCycleListt)
        val chargeCycleAdapter =  ChargeCycleListAdapter(batteryViewModel.chargeCycleList)
        chargeCycleRecyclerView.adapter = chargeCycleAdapter
        chargeCycleRecyclerView.layoutManager = LinearLayoutManager(this)

        val badText: TextView =  findViewById(R.id.badCount)
        badText.text = batteryViewModel.badCount

        val optimalText: TextView =  findViewById(R.id.optimalCount)
        optimalText.text = batteryViewModel.optimalCount

        val spotText: TextView =  findViewById(R.id.spotCount)
        spotText.text = batteryViewModel.spotCount

        val serviceIntent = Intent(this, MyBroadcastReceiverService::class.java)
        startService(serviceIntent)

        val timeFrameBox = findViewById<TextView>(R.id.timeFrame)
        timeFrameBox.visibility = batteryViewModel.timeFrameBox
        val timeBox = findViewById<TextView>(R.id.time)
        timeBox.visibility = batteryViewModel.timeBox
        val chargeBox = findViewById<TextView>(R.id.dischargeLevel)
        chargeBox.visibility = batteryViewModel.chargeBox

        chargeCycleRecyclerView.visibility = batteryViewModel.recyclerView
        spotText.visibility = batteryViewModel.spotBox
        optimalText.visibility = batteryViewModel.optBox
        badText.visibility = batteryViewModel.bad
    }


    fun show( view: View) {
        val timeFrameBox = findViewById<TextView>(R.id.timeFrame)
        timeFrameBox.visibility = View.INVISIBLE
        val timeBox = findViewById<TextView>(R.id.time)
        timeBox.visibility = View.INVISIBLE
        val chargeBox = findViewById<TextView>(R.id.dischargeLevel)
        chargeBox.visibility = View.INVISIBLE
        val recyclerView = findViewById<RecyclerView>(R.id.chargeCycleListt)
        recyclerView.visibility = View.INVISIBLE
        val spotBox = findViewById<TextView>(R.id.spotCount)
        spotBox.visibility = View.VISIBLE
        val optBox = findViewById<TextView>(R.id.optimalCount)
        optBox.visibility = View.VISIBLE
        val bad = findViewById<TextView>(R.id.badCount)
        bad.visibility = View.VISIBLE

        batteryViewModel.timeFrameBox = View.INVISIBLE
        batteryViewModel.timeBox = View.INVISIBLE
        batteryViewModel.chargeBox = View.INVISIBLE
        batteryViewModel.recyclerView = View.INVISIBLE
        batteryViewModel.spotBox = View.VISIBLE
        batteryViewModel.optBox = View.VISIBLE
        batteryViewModel.bad = View.VISIBLE

        var optimalCount = 0
        var badCount = 0
        var spotCount = 0
        var fullyCharged = false
        var temp =dataList[0]

        var elapsedTime: Long = 0
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var timeOfFullCharge: Long = 0
        for(i in dataList) {
            if(!fullyCharged) {
                if(i.batteryLevel!! == 100f && i.charging && temp.batteryLevel!! < 100f) {
                    fullyCharged = true
                    timeOfFullCharge = dateFormat.parse(i.timestamp).time
                }
            }
            if(i.charging && i.batteryLevel!! == 100f) {
                elapsedTime +=  (dateFormat.parse(i.timestamp).time - timeOfFullCharge)/1000

            }
            else if(fullyCharged){
                if(elapsedTime<=2 )
                    spotCount += 1
                else if(elapsedTime<=1800)
                    optimalCount+=1
                else
                    badCount += 1
                fullyCharged = false

            }

            if(i.batteryLevel!! != 100f) {
                fullyCharged = false
                elapsedTime = 0
            }
            temp = i
        }

        val badText: TextView =  findViewById(R.id.badCount)
        batteryViewModel.badCount = "Bad Count: $badCount"
        badText.text = batteryViewModel.badCount

        val optimalText: TextView =  findViewById(R.id.optimalCount)
        batteryViewModel.optimalCount = "Optimal Count: $optimalCount"
        optimalText.text = batteryViewModel.optimalCount

        val spotText: TextView =  findViewById(R.id.spotCount)
        batteryViewModel.spotCount = "Spot Count: $spotCount"
        spotText.text = batteryViewModel.spotCount

    }

    class ChargeCycle(discharge:Int, time:Int, timeFrame:String){
         var totDischarge:Int = discharge
         var totTime:Int = time
         var frame = timeFrame
    }

    fun viewHistory(view:View) {
        val timeFrameBox = findViewById<TextView>(R.id.timeFrame)
        timeFrameBox.visibility = View.VISIBLE
        val timeBox = findViewById<TextView>(R.id.time)
        timeBox.visibility = View.VISIBLE
        val chargeBox = findViewById<TextView>(R.id.dischargeLevel)
        chargeBox.visibility = View.VISIBLE
        val recyclerView = findViewById<RecyclerView>(R.id.chargeCycleListt)
        recyclerView.visibility = View.VISIBLE
        val spotBox = findViewById<TextView>(R.id.spotCount)
        spotBox.visibility = View.INVISIBLE
        val optBox = findViewById<TextView>(R.id.optimalCount)
        optBox.visibility = View.INVISIBLE
        val bad = findViewById<TextView>(R.id.badCount)
        bad.visibility = View.INVISIBLE

        batteryViewModel.timeFrameBox = View.VISIBLE
        batteryViewModel.timeBox = View.VISIBLE
        batteryViewModel.chargeBox = View.VISIBLE
        batteryViewModel.recyclerView = View.VISIBLE
        batteryViewModel.spotBox = View.INVISIBLE
        batteryViewModel.optBox = View.INVISIBLE
        batteryViewModel.bad = View.INVISIBLE
        var initialBatterylevel = dataList[0].batteryLevel
        var initialTime = dataList[0].timestamp
        var dischargeLevel = 0f
        var timeForDischarge = 0f
        var temp = dataList[0]
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var timeFrameStart = dateFormat.parse(initialTime).time
        val chargeCycleList: ArrayList<ChargeCycle> = arrayListOf()

        for(i in dataList) {
            var elapsedTime =  dateFormat.parse(i.timestamp).time - timeFrameStart
            val startTime = dateFormat.parse(initialTime)
            val endTime = dateFormat.parse(i.timestamp)

            if(elapsedTime/1000 <= 3600) {
                val diff: Long = endTime.time - startTime.time
                val seconds = diff/1000

                if(!i.charging) {
                    dischargeLevel += initialBatterylevel!! - i.batteryLevel!!
                    timeForDischarge += seconds
                }
            }
            else{
                if(!temp.charging) {
                    val remainingTimeInTimeFrame =3600 - ( dateFormat.parse(temp.timestamp).time - timeFrameStart)/1000
                    timeForDischarge += remainingTimeInTimeFrame
                }

                val frame = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeFrameStart) + " - " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeFrameStart + 3600000)
                val chargeCycle = ChargeCycle(dischargeLevel.toInt(), (timeForDischarge/60).toInt(), frame)
                chargeCycleList.add(chargeCycle)

                timeFrameStart += 3600000  //incremented an hour
                if(!temp.charging) {
                    dischargeLevel = temp.batteryLevel!!.minus(i.batteryLevel!!)
                    timeForDischarge = ((endTime.time - timeFrameStart)/1000).toFloat()
                }
                else {
                    dischargeLevel = 0f
                    timeForDischarge = 0f
                }

            }

            initialTime = i.timestamp
            initialBatterylevel = i.batteryLevel!!
            temp = i
        }


        batteryViewModel.chargeCycleList = chargeCycleList

        val adapter =  ChargeCycleListAdapter(batteryViewModel.chargeCycleList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}