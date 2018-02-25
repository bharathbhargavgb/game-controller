package com.unnecessary.summa.gamecontroller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ConcurrentLinkedQueue

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null
    // ConcurrentLinkedQueue to pass accelerometer sensor information to the UDPClient
    private var sensorParamQueue : ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            val values =  "" + p0.values[0] + "," + p0.values[1] + "," + p0.values[2]
            sensorParamQueue.add(values)
            sensorTv.text = values
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorParamQueue.add("Game on!")

        startBtn.setOnClickListener { mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI) }
        stopBtn.setOnClickListener { mSensorManager!!.unregisterListener(this) }

        connectBtn.setOnClickListener {
            val client = Thread(UDPClient(sensorParamQueue, ipEditText.text.toString()))
            client.start()
            Toast.makeText(applicationContext, "Connected to Server", Toast.LENGTH_SHORT).show()
        }
    }
}
