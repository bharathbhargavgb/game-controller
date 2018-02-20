package com.unnecessary.summa.gamecontroller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 != null) {
            val values =  "" + p0.values[1] + " " + p0.values[0]
            sensorTv.text = values
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        startBtn.setOnClickListener { mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME) }
        stopBtn.setOnClickListener { mSensorManager!!.unregisterListener(this) }

        var server = Thread(UDPServer())
        server.start()

//        thread(start = true) {
//            val local : InetAddress = InetAddress.getByName("192.168.1.10")
//            val msg : String = "Skadoosh"
//            val msg_len : Int = msg.length
//            val port : Int = 9156
//
//            val socket : DatagramSocket = DatagramSocket(port)
//
//            val packet : DatagramPacket = DatagramPacket(msg.toByteArray(), msg_len, local, port)
//
//            println ("Sending data packet")
//            socket.send(packet)
//        }


    }

}
