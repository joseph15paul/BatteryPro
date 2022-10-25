package com.example.batterypro

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class MyBroadcastReceiverService : Service(){

  private var Receiver: BroadcastReceiver? = null

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        registerReceiver()
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
            Calendar
                .getInstance().time
        )
    }



    private fun registerReceiver() {
            Receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.hasExtra(BatteryManager.EXTRA_STATUS)) {
                    var level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                    val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                    val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                            || status == BatteryManager.BATTERY_STATUS_FULL

                    val ts = getCurrentTimestamp()


                    runBlocking {  launch {
                        (application as BatteryStatsApplication).repository.insert(
                            batteryStats(

                                id = 0,
                                batteryLevel = level.toFloat(),
                                charging = isCharging,
                                timestamp = ts
                            )
                        )
                    }
                }}
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(Receiver,filter)

}
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }
    private val NOTIF_ID = 1
    private val NOTIF_CHANNEL_ID = "Channel_Id"
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        val chan = NotificationChannel(

            NOTIF_CHANNEL_ID,
            "My Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_SECRET

        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                NOTIF_CHANNEL_ID
            )
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("BatteryPro+")
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .setChannelId(NOTIF_CHANNEL_ID)
                .build()
        )
    }
}