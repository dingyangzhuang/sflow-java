package com.flow.analyze;

import Utils.DataConvert;

public class Temp {
	public static void main(String[] args) {
		long a = 9747218102353920L;
		String mac = Long.toHexString(a);
		System.out.println(mac);
		StringBuffer head = new StringBuffer();
		for(int i=0;i<16-mac.length();i++){
			head.append("0");
		}
		head.append(mac);
		System.out.println(head.toString().substring(0,head.length()-4));
		
		System.out.println(DataConvert.getIpProtocol2Str(6));

	}
}
