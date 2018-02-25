package com.unnecessary.summa.gamecontroller;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPClient implements Runnable {
    private int PORT = 9156;
    private InetAddress address;
    private DatagramSocket udpSocket;
    ConcurrentLinkedQueue<String> sensorParamQueue;

    UDPClient(ConcurrentLinkedQueue<String> sensorParamQueue, String IP) {

        this.sensorParamQueue = sensorParamQueue;

        try {
            udpSocket = new DatagramSocket(PORT);
            address = InetAddress.getByName(IP);
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                String message = "";
                if ((message = sensorParamQueue.poll()) != null) {
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), address, PORT);
                    udpSocket.send(packet);
                    //System.out.println(address + ":" + PORT + " => " + message);
                }
            }
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (UnknownHostException e) {
            Log.e("Udp Send:", "Host error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }
}