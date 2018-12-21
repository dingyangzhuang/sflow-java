package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于统计设备CPU占用率和内存使用情况
 * @author thinkpad T440S
 *
 */
@Getter@Setter
public class SflowProcessorCounterData {
	private int tag;//为1001代表Processor Counters字段
	private int length;//总字节长度（不包含tag和length）
	private int FiveSecCPULoad;//5秒内的CPU负载
	private int OneMinCPULoad;//1分钟内的CPU负载
	private int FiveMinCPULoad;//5分钟内的CPU负载
	private long TotalMemory;//总的内存数
	private long FreeMemory;//可用的内存数
	@Override
	public String toString() {
		return "\"FiveSecCPULoad\":\"" + FiveSecCPULoad + "\", \"OneMinCPULoad\":\"" + OneMinCPULoad
				+ "\", \"FiveMinCPULoad\":\"" + FiveMinCPULoad + "\", \"TotalMemory\":\"" + TotalMemory
				+ "\", \"FreeMemory\":\"" + FreeMemory + "\"";
	}


	
}
