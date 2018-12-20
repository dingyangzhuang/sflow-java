package com.flow.analyze;

import java.io.DataInputStream;
import java.io.IOException;

import com.flow.beans.SflowHeader;

import Utils.IpOperation;
import Utils.TimeUtils;

public class SflowSampleMethod {
	/**
	 * 获取sflow的包头
	 * 
	 * @return
	 * @throws IOException
	 */
	public static SflowHeader getSflowHeader(DataInputStream dis) throws IOException {
		SflowHeader sflowHeader = new SflowHeader();
		// 分析数据
		int version = dis.readInt();// 版本
		int agent_address_type = dis.readInt();// agent地址类型 1:ipv4
		long ip = dis.readInt();// agent ip
		int sub_agent_id = dis.readInt();// sub_agent_id
		int sequence_number = dis.readInt();// sequence_number
		long sysUptime = dis.readInt();// 系统更新时间
		int numSamples = dis.readInt();// 样本的数量

		// 填充到头信息中

		// sflow头部信息
		sflowHeader.setVersion(version);
		if (agent_address_type == 1) {
			sflowHeader.setAgent_address_type("ipv4");
		} else {
			sflowHeader.setAgent_address_type("unknow");
		}
		sflowHeader.setIp(IpOperation.getIpFromLong(ip));
		sflowHeader.setSub_agent_id(sub_agent_id);
		sflowHeader.setSequence_number(sequence_number);
		sflowHeader.setSysUptime(TimeUtils.getTimeFromSeconds(sysUptime));
		sflowHeader.setNumSamples(numSamples);
		return sflowHeader;
	}
}
