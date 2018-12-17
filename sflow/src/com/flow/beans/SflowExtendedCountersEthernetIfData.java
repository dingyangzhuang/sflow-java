package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SflowExtendedCountersEthernetIfData {

	private int tag;//为2代表Ethernet Interface Counters字段
	private int length;//总字节长度（不包含tag和length）
	private int AlignmentErrors;//报文对齐方式错误个数（4字节对齐）
	private int FCSErrors;//循环冗余效验码错误个数
	private int SingleCollisionFrames;//在特殊接口成功传送信息帧总数。该传输被规定只可有一个碰撞. 
	private int MultipleCollisionFrames;//在特殊接口成功传送信息帧总数。
	private int SQETestErrors;//SQE测试错误
	private int DeferredTransmissions;//延迟传输
	private int LateCollisions;//晚碰撞
	private int ExcessiveCollisions;//过度的碰撞
	private int InternalMacTransmitErrors;//内部Mac传输错误
	private int CarrierSenseErrors;//载波监听错误
	private int FrameTooLongs;//帧太长
	private int InternalMacReceiveErrors;//内部MAC接收错误
	private int SymbolErrors;//符号错误
	
	@Override
	public String toString() {
		return "SflowExtendedCountersEthernetIfData [tag=" + tag + ", length=" + length + ", AlignmentErrors="
				+ AlignmentErrors + ", FCSErrors=" + FCSErrors + ", SingleCollisionFrames=" + SingleCollisionFrames
				+ ", MultipleCollisionFrames=" + MultipleCollisionFrames + ", SQETestErrors=" + SQETestErrors
				+ ", DeferredTransmissions=" + DeferredTransmissions + ", LateCollisions=" + LateCollisions
				+ ", ExcessiveCollisions=" + ExcessiveCollisions + ", InternalMacTransmitErrors="
				+ InternalMacTransmitErrors + ", CarrierSenseErrors=" + CarrierSenseErrors + ", FrameTooLongs="
				+ FrameTooLongs + ", InternalMacReceiveErrors=" + InternalMacReceiveErrors + ", SymbolErrors="
				+ SymbolErrors + "]";
	}
}
