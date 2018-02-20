package com.unnecessary.summa.gamecontroller;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.sql.DriverManager.println;

public class UDPServer implements Runnable {
    private String message = "SKADOOOOOOOSH";
    private int PORT = 9156;
    private String IP = "192.168.1.11";
    private InetAddress address;
    DatagramSocket udpSocket = null;

    @Override
    public void run() {
        try {
            udpSocket = new DatagramSocket(PORT);
            address = InetAddress.getByName(IP);
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), address, PORT);
            udpSocket.send(packet);
            System.out.println("Packet send");
        } catch (SocketException e) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (UnknownHostException e) {
            Log.e("Udp Send:", "Host error:", e);
        } catch (IOException e) {
            Log.e("Udp Send:", "IO Error:", e);
        } finally {
            udpSocket.close();
        }
    }

    /*
     * TCPServer
     */

//    @Override
//    public void run() {
//        try {
//            // Creating new socket connection to the IP (first parameter) and its opened port (second parameter)
//            Socket s = new Socket("192.168.1.6", 9156);
//
//            // Initialize output stream to write message to the socket stream
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//
//            String outMsg = "";
//
//            outMsg = "This is Client";
//
//            // Write message to stream
//            out.write(outMsg);
//
//            // Flush the data from the stream to indicate end of message
//            out.flush();
//
//            // Close the output stream
//            out.close();
//
//            // Close the socket connection
//            s.close();
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//        }
//    }
}