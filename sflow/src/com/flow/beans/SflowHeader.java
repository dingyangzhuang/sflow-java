package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SflowHeader {
	private int version;//sflow的版本，只支持版本5
	private String  agent_address_type;//agent地址类型 1:ipv4
	private String ip;//agent ip 采样设备的IP
	private int sub_agent_id;//sub_agent_id 子设备的ID 默认为0
	private int sequence_number;//已经发送sflow报文序列号 （sflow报文序列编号依次加1）
	private String sysUptime;//自设备启动时刻当现在经过的毫秒数（不是当前时间）
	private int numSamples;//包含的记录数目
	@Override
	public String toString() {
		return "SflowHeader [version=" + version + ", agent_address_type=" + agent_address_type + ", ip=" + ip
				+ ", sub_agent_id=" + sub_agent_id + ", sequence_number=" + sequence_number + ", sysUptime=" + sysUptime
				+ ", numSamples=" + numSamples + "]";
	}
}
