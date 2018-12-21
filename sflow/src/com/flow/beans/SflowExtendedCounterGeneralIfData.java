package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SflowExtendedCounterGeneralIfData {
	private int tag;//为1代表Generic Interface Counters字段
	private int length;//总字节长度（不包含tag和length）
	private int ifIndex;//接口的索引值
	private int ifType;//接口的类型，6表示ethernet接口
	private long ifSpeed;//接口的速率，单位bps
	private int ifDirection;//双工模式0=unknow ，1全双工，2半双工
	private int ifStatus;//接口状态第0位 = ifAdminStatus (0 = down, 1 = up) 
						       //第1位 = ifOperStatus (0 = down, 1 = up) 
							   //相当于1：管理down，协议up
								//    2：协议up，管理down   3：两个都up
	private long ifInOctets;//进方向统计报文字节数 
	private int ifInUcastPkts;//进方向统计报文数
	private int ifInMulticastPkts;//进方向统计组播报文数
	private int ifInBroadcastPkts;//进方向统计广播报文数
	private int ifInDards;//进方向统计丢弃的报文数
	private int ifInErrors;//进方向统计错误的报文数
	private int ifInUnknowProtos;//进方向统计未知协议的报文数，默认填0
	
	private long ifOutOctets;//出方向统计报文字节数
	private int ifOutUcastPkts;//出方向统计报文数
	private int ifOutMulticastPkts;//出方向统计组播报文数
	private int ifOutBroadcastPkts;//出方向统计广播报文数
	private int ifOutDards;//出方向统计丢弃的报文数
	private int ifOutErrors;//出方向统计错误的报文数
	 
	private int ifPromiscuousMode;//混杂模式（非0代表混杂模式）

	@Override
	public String toString() {
		return "\"ifIndex\":\"" + ifIndex + "\", \"ifSpeed\":\"" + ifSpeed + "\", \"ifInOctets\":\"" + ifInOctets
				+ "\", \"ifInUcastPkts\":\"" + ifInUcastPkts + "\", \"ifInMulticastPkts\":\"" + ifInMulticastPkts
				+ "\", \"ifInBroadcastPkts\":\"" + ifInBroadcastPkts + "\", \"ifInDards\":\"" + ifInDards
				+ "\", \"ifInErrors\":\"" + ifInErrors + "\", \"ifInUnknowProtos\":\"" + ifInUnknowProtos
				+ "\", \"ifOutOctets\":\"" + ifOutOctets + "\", \"ifOutUcastPkts\":\"" + ifOutUcastPkts
				+ "\", \"ifOutMulticastPkts\":\"" + ifOutMulticastPkts + "\", \"ifOutBroadcastPkts\":\""
				+ ifOutBroadcastPkts + "\", \"ifOutDards\":\"" + ifOutDards + "\", \"ifOutErrors\":\"" + ifOutErrors
				+ "\", \"ifPromiscuousMode\":\"" + ifPromiscuousMode + "\"";
	}
	

	
	
	

}
