package com.example.batterypro

import android.app.Application
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class BatteryStatsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { batteryStatsRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BatteryStatsRepository(database.batteryStatsDao()) }
    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, MyBroadcastReceiverService::class.java))
    }
}