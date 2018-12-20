package com.flow.receive;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.flow.analyze.SflowCountersSampleMethod;
import com.flow.analyze.SflowFlowSampleMethod;
import com.flow.analyze.SflowSampleMethod;
import com.flow.beans.SflowHeader;

public class UdpServerThread extends Thread {
	private DatagramSocket socket = null;
	private DataInputStream dis = null;

	public UdpServerThread(DatagramPacket packet, DatagramSocket socket) {
		// TODO Auto-generated constructor stub
		socket = socket;
		dis = new DataInputStream(new ByteArrayInputStream(packet.getData()));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		// sflow 头部信息
		SflowHeader sflowHeader = null;
		try {
			sflowHeader = SflowSampleMethod.getSflowHeader(dis);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// sflow具体信息
		int NumSamples = sflowHeader.getNumSamples();
		// 循环遍历sflow包中包含的所有元素
		while (NumSamples > 0) {
			// 此处为3代表 expanded flow sample 字段
			// 此处为4代表expanded counters sample字段
			try {
				int tag = dis.readInt();
				if (tag == 4) {
					SflowCountersSampleMethod.getCountersSampleData(dis, sflowHeader);
				}

				if (tag == 3) {
					/***** 获取flow sample头信息 ******/
					SflowFlowSampleMethod.getFlowSampleData(dis, sflowHeader);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.getMessage();
			}
			NumSamples--;
		}
	}
}
