package com.example.batterypro

import android.view.View
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BatteryViewModel (private val repository: BatteryStatsRepository) : ViewModel() {


        val allBatteryStats: LiveData<List<batteryStats>> = repository.allBatteryStats.asLiveData()
        var chargeCycleList = listOf<MainActivity.ChargeCycle>()
        var optimalCount:String = ""
        var badCount:String = ""
        var spotCount:String = ""
        var timeFrameBox  = View.INVISIBLE
        var timeBox = View.INVISIBLE
        var chargeBox = View.INVISIBLE
        var recyclerView = View.INVISIBLE
        var spotBox = View.INVISIBLE
        var optBox = View.INVISIBLE
        var bad = View.INVISIBLE
        /**
         * Launching a new coroutine to insert the data in a non-blocking way
         */
        fun insert(battery: batteryStats) = viewModelScope.launch {
            repository.insert(battery)
        }
    }

    class BatteryStatsViewModelFactory(private val repository: BatteryStatsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BatteryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BatteryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
