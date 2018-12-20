package com.flow.receive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 用于接收sflow流量，并分析
 * @author thinkpad T440S
 *接受处理过程：
 *创建接受端Socket对象——DatagramSocket
 *创建包DatagramPacket对象（数据接收容器）
 *调用接受方法接受数据
 *解析数据包对象，取出接受到的信息
 *释放资源
 */
public class SflowReceive {
	public static void main(String[] args) throws IOException {
	    // TODO Auto-generated method stub
	    DatagramSocket socket = new DatagramSocket(6343);
	    byte[] infoBytes = new byte[1024];
	    int num = 0;
	    System.out.println("Sflow Server is started!·········");
	    while (true) {
	        DatagramPacket packet = new DatagramPacket(infoBytes, infoBytes.length);
	        socket.receive(packet);
	        UdpServerThread thread = new UdpServerThread(packet, socket);
	        thread.start();
	        System.out.println(thread);
	        System.out.println("receive udp packet num：" + (num++));
	    }
	}
}
