package com.unnecessary.summa.gamecontroller

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ConcurrentLinkedQueue
import android.os.PowerManager
import android.view.View;
import android.view.Window

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager : SensorManager ?= null
    private var mPowerManager : PowerManager ?= null
    private var mAccelerometer : Sensor ?= null
    // ConcurrentLinkedQueue to pass accelerometer sensor information to the UDPClient
    private var sensorParamQueue : ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()

    private var mClient : UDPClient? = UDPClient()
    private var client : Thread ?= null

    protected var mWakeLock: PowerManager.WakeLock? = null

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            val values =  "" + p0.values[0] + "," + p0.values[1] + "," + p0.values[2]
            sensorParamQueue.add(values)
            sensorTv.text = values
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        mWakeLock = mPowerManager!!.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Controller Screen");
        mWakeLock!!.acquire();

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorParamQueue.add("Game on!")

        startBtn.setOnClickListener { mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL) }
        stopBtn.setOnClickListener { mSensorManager!!.unregisterListener(this) }

        connectBtn.setOnClickListener {
            mClient!!.setValues(sensorParamQueue, ipEditText.text.toString())
            client = Thread(mClient)
            client!!.start()
            Toast.makeText(applicationContext, "Connected to Server", Toast.LENGTH_SHORT).show()
        }

        disconnectBtn.setOnClickListener {
            mClient!!.stopListening()
            client!!.join()
            Toast.makeText(applicationContext, "Connection to server stopped", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        mWakeLock!!.release();
        super.onDestroy()
    }
}
