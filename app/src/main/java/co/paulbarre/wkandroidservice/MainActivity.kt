package co.paulbarre.wkandroidservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var mService: MainService? = null

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d(">>>", "[MainActivity] onServiceConnected called")
            mService = (p1 as? MainService.LocalBinder)?.service

            (findViewById(R.id.value_text) as? TextView)?.text = mService?.elapsedTime.toString()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Log.d(">>>", "[MainActivity] onServiceDisconnected called")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.start_button)?.setOnClickListener { startMyService() }
        findViewById(R.id.stop_button)?.setOnClickListener { stopMyService() }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MainService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
    }

    fun startMyService() {
        Log.d(">>>", "[MainActivity] startMyService called")
        val intent = Intent(this, MainService::class.java)
        intent.action = MainService.START_COMMAND
        startService(intent)
    }

    fun stopMyService() {
        Log.d(">>>", "[MainActivity] stopMyService called")
        val intent = Intent(this, MainService::class.java)
        intent.action = MainService.STOP_COMMAND
        startService(intent)
    }
}
