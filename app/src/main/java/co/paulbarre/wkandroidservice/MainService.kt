package co.paulbarre.wkandroidservice

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class MainService : Service() {

    var startCalendar: Calendar? = null

    val elapsedTime: Long
        get() {
            val now = Calendar.getInstance()
            return startCalendar?.let { (now.timeInMillis - it.timeInMillis) / 1000 } ?: 0
        }

    inner class LocalBinder : Binder() {
        val service: MainService
            get() = this@MainService
    }

    companion object {
        const val START_COMMAND = "START"
        const val STOP_COMMAND = "STOP"
    }

    private val mBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder? {
        Log.d(">>>", "[MainService] onBind")
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "START" -> start()
                "STOP" -> stop()
                else -> Log.d(">>>", "[MainService] onStartCommand received UNKNOWN")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        Log.d(">>>", "[MainService] onStartCommand received START")
        val notification = Notification.Builder(this)
                .setContentTitle("OKLM Title")
                .setContentText("OKLM msg")
                .build()
        startForeground(1, notification)

        startCalendar = Calendar.getInstance()
    }

    private fun stop() {
        Log.d(">>>", "[MainService] onStartCommand received STOP")
        startCalendar = null
        stopForeground(true)
        stopSelf()
    }
}
