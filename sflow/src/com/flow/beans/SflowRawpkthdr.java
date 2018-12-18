package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
/**
 * 截取原始报文全部或者一部分报文头（报文大于128字节截取128字节，否则截取全部）
 * @author thinkpad T440S
 *
 */
public class SflowRawpkthdr {
	private int tag;//当值为1的时候，flow类型的sample包的Raw Packet Header类型
	private int length;//该字段总的字节数，不包含tag和length
	private int header_protocol;//原始数据mac协议类型（1代表以太网类型报文）
	private int frame_length;//原始报文总字节数
	private int payload_removed;//截取报文时被忽略的字节数
	private int original_packet_length;//截取的原始报文总字节数
	@Override
	public String toString() {
		return "SflowRawpkthdr [tag=" + tag + ", length=" + length + ", header_protocol=" + header_protocol
				+ ", frame_length=" + frame_length + ", payload_removed=" + payload_removed
				+ ", original_packet_length=" + original_packet_length + "]";
	}
	
	/*********该字段最后还包含一个原始包的数据，这里不进行解析，所有读取数据的时候，要
	 * 跳过OriginalRacketlength长度的字节数*******************/

	
	
}
