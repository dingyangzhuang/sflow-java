package com.flow.receive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 程序启动入口
 * @author dingyz
 *
 */
public class SflowMain {
	public static void main(String[] args) throws IOException {
		//创建监听
		DatagramSocket socket = new DatagramSocket(6343);
		byte[] buf = new byte[2048];
		int num = 1;
		while(true){
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			UdpServerThread thread = new UdpServerThread(packet, socket);
			thread.start();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("-------------------------------------------"+"\n");
			stringBuffer.append(thread+"\n");
			stringBuffer.append("receive udp packet num：" + (num++) +"\n");
			stringBuffer.append("current thread num：" + thread.activeCount()+"\n");
			stringBuffer.append("-------------------------------------------"+"\n");
			System.out.println(stringBuffer.toString());
		}
	}
}
