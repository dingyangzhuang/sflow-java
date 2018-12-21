package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SflowIpv4Data {
	private int tag;//当值为3的时候，flow类型的sample包的IPV4 Data类型
	private int length;//该字段总的字节数，不包含tag和length
	private int len_ip_packet;//原始报文三层头以及后续报文的总字节长度
	private String ip_proto;//协议TCP:6 UDP:17 ICMP(1) IGMP（2）4字节
	private String src_ip;//源IP 4字节
	private String des_ip;//目的IP 4字节
	private int src_port;//源端口
	private int des_port;//目的端口
	private int tcp_flag;//（TCP包才有）
	private int tos;//
	/************如果是TCP才是32字节，UDP到目的端口的时候需要截取
	 * LengthIpPacket-24字节******************/
	@Override
	public String toString() {
		return "\"len_ip_packet\":\"" + len_ip_packet
				+ "\", \"ip_proto\":\"" + ip_proto + "\", \"src_ip\":\"" + src_ip + "\", \"des_ip\":\"" + des_ip
				+ "\", \"src_port\":\"" + src_port + "\", \"des_port\":\"" + des_port + "\", \"tcp_flag\":\"" + tcp_flag
				+ "\", \"tos\":\"" + tos + "\"";
	}
	

	
}
