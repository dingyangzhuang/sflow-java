package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
/**
 * 二层数据，	针Ethernet报文，解析报文中的Ethernet头信息
 * @author thinkpad T440S
 *
 */
public class SflowEthernetData {
	private int tag;//当值为2的时候，flow类型的sample包的Ethernet Frame Data类型
	private int length;//该字段总的字节数，不包含tag和length
	private int len_mac_packet;//原始报文总字节数
	private String src_mac;//源MAC地址8字节，最后2字节全为0 
	private String des_mac;//目的MAC地址8字节，最后2字节全为0
	private int ethernet_packet_type;//Ethernet包类型，2048：Internet IP (IPv4)
	@Override
	public String toString() {
		return "SflowEthernetData [tag=" + tag + ", length=" + length + ", len_mac_packet=" + len_mac_packet
				+ ", src_mac=" + src_mac + ", des_mac=" + des_mac + ", ethernet_packet_type=" + ethernet_packet_type
				+ "]";
	}

	
}
