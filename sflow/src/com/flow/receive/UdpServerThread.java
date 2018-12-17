package com.flow.receive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServerThread extends Thread {
	private String info = null; 
	private DatagramSocket socket = null; 
	private int port = 0; 
	private InetAddress address = null;
	public UdpServerThread(DatagramPacket packet2, DatagramSocket socket2, byte[] infoBytes2) {
	    // TODO Auto-generated constructor stub
	    socket = socket2;
	    info = new String(infoBytes2, 0, packet2.getLength());
	    port = packet2.getPort();
	    address = packet2.getAddress();
	}

	@Override
	public void run() {
	    // TODO Auto-generated method stub
	    super.run();
	    System.out.println("客户端说：" + info);
	    byte[] infoBytes = "你好！我是服务器~".getBytes();
	    DatagramPacket packet = new DatagramPacket(infoBytes, infoBytes.length, address, port);
	    try {
	        socket.send(packet);
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
