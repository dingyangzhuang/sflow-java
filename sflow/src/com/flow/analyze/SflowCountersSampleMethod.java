package com.flow.analyze;

import java.io.DataInputStream;
import java.io.IOException;

import com.flow.beans.ExpandedCountersSampleHeader;
import com.flow.beans.SflowExtendedCounterGeneralIfData;
import com.flow.beans.SflowExtendedCountersEthernetIfData;
import com.flow.beans.SflowHeader;
import com.flow.beans.SflowProcessorCounterData;
import com.flow.output.OutPutRedis;

public class SflowCountersSampleMethod {
	/**
	 * 获取counters sample的具体信息，包括头和体
	 * 
	 * @param sflowHeader
	 * @throws IOException
	 */
	public static void getCountersSampleData(DataInputStream dis, SflowHeader sflowHeader) throws IOException {
		StringBuffer str = new StringBuffer();
		/************** expanded counters sample *********************/
		ExpandedCountersSampleHeader countersSampleHeader = getExpandedCountersSampleHeader(dis);
		int i = countersSampleHeader.getNum_elements();
		str.append(sflowHeader + " "+countersSampleHeader + " ");
		// 根据元素的个数进行处理
		while (i > 0) {
			/** 处理元素的方法 **/
			/** sflow_extendedcounter_generalif_data **/
			Object counter = getExpandedCountersSampleDate(dis);
			if (null != counter) {
				str.append(counter.toString());
			}
			i--;
		}
		OutPutRedis.writeRedis(str.toString());
	}
	
	/**
	 * 获取expanded counters sample头部信息
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static ExpandedCountersSampleHeader getExpandedCountersSampleHeader(DataInputStream dis) throws IOException {
		/************** expanded counters sample *********************/
		int lenght = dis.readInt();// 该字段总的字节数（不包含：tag和length字段)
		int sequence_number1 = dis.readInt();// flow sample字段序列号 依次递增(分接口记录序列号)
		int ds_class = dis.readInt();// 数据源类型 （只支持 接口类型 数据源类型默认为0）
		int ds_index = dis.readInt();// 数据源索引（接口索引值）
		int num_elements = dis.readInt();// 包含的记录数
		ExpandedCountersSampleHeader countersSampleHeader = new ExpandedCountersSampleHeader();
		countersSampleHeader.setTag(4);
		countersSampleHeader.setLenght(lenght);
		countersSampleHeader.setSequence_number1(sequence_number1);
		countersSampleHeader.setDs_class(ds_class);
		countersSampleHeader.setDs_index(ds_index);
		countersSampleHeader.setNum_elements(num_elements);
		return countersSampleHeader;
	}

	/**
	 * 分析counters 子信息 1、代表Generic Interface Counters字段 2、代表Ethernet Interface
	 * Counters字段 1101、代表Processor Counters字段
	 * 
	 * @author thinkpad T440S header counter类型数据的包头，用于与子信息进拼接
	 * @return
	 * @throws IOException
	 */
	public static Object getExpandedCountersSampleDate(DataInputStream dis) throws IOException {
		int tag = dis.readInt();// 读取类型tag，1、2、1101
		// 1、代表Generic Interface Counters字段
		if (tag == 1) {
			int length = dis.readInt();// 总字节长度（不包含tag和length）
			int ifIndex = dis.readInt();// 接口的索引值
			int ifType = dis.readInt();// 接口的类型，6表示ethernet接口
			long ifSpeed = dis.readLong();// 接口的速率，单位bps
			int ifDirection = dis.readInt();// 双工模式0=unknow ，1全双工，2半双工
			int ifStatus = dis.readInt();// 接口状态第0位=ifAdminStatus(0=down,1=up)?
											// 第1位=ifOperStatus(0=down,1=up)?
											// 相当于1：管理down，协议up
											// 2：协议up，管理down 3：两个都up
			long ifInOctets = dis.readLong();// 进方向统计报文字节数
			int ifInUcastPkts = dis.readInt();// 进方向统计报文数
			int ifInMulticastPkts = dis.readInt();// 进方向统计组播报文数
			int ifInBroadcastPkts = dis.readInt();// 进方向统计广播报文数
			int ifInDards = dis.readInt();// 进方向统计丢弃的报文数
			int ifInErrors = dis.readInt();// 进方向统计错误的报文数
			int ifInUnknowProtos = dis.readInt();// 进方向统计未知协议的报文数，默认填0

			long ifOutOctets = dis.readLong();// 出方向统计报文字节数
			int ifOutUcastPkts = dis.readInt();// 出方向统计报文数
			int ifOutMulticastPkts = dis.readInt();// 出方向统计组播报文数
			int ifOutBroadcastPkts = dis.readInt();// 出方向统计广播报文数
			int ifOutDards = dis.readInt();// 出方向统计丢弃的报文数
			int ifOutErrors = dis.readInt();// 出方向统计错误的报文数

			int ifPromiscuousMode = dis.readInt();// 混杂模式（非0代表混杂模式）

			SflowExtendedCounterGeneralIfData generalIfData = new SflowExtendedCounterGeneralIfData();
			generalIfData.setTag(tag);
			generalIfData.setLength(length);
			generalIfData.setIfDirection(ifDirection);
			generalIfData.setIfInBroadcastPkts(ifInBroadcastPkts);
			generalIfData.setIfInDards(ifInDards);
			generalIfData.setIfIndex(ifIndex);
			generalIfData.setIfType(ifType);
			generalIfData.setIfInErrors(ifInErrors);
			generalIfData.setIfInMulticastPkts(ifInMulticastPkts);
			generalIfData.setIfInOctets(ifInOctets);
			generalIfData.setIfInUcastPkts(ifInUcastPkts);
			generalIfData.setIfInUnknowProtos(ifInUnknowProtos);
			generalIfData.setIfOutBroadcastPkts(ifOutBroadcastPkts);
			generalIfData.setIfOutDards(ifOutDards);
			generalIfData.setIfOutErrors(ifOutErrors);
			generalIfData.setIfOutMulticastPkts(ifOutMulticastPkts);
			generalIfData.setIfOutOctets(ifOutOctets);
			generalIfData.setIfOutUcastPkts(ifOutUcastPkts);
			generalIfData.setIfPromiscuousMode(ifPromiscuousMode);
			generalIfData.setIfSpeed(ifSpeed);
			generalIfData.setIfStatus(ifStatus);
			return generalIfData;
		}
		// 2、代表Ethernet Interface Counters字段
		if (tag == 2) {
			int length = dis.readInt();// 总字节长度（不包含tag和length）
			int AlignmentErrors = dis.readInt();// 报文对齐方式错误个数（4字节对齐）
			int FCSErrors = dis.readInt();// 循环冗余效验码错误个数
			int SingleCollisionFrames = dis.readInt();// 在特殊接口成功传送信息帧总数。该传输被规定只可有一个碰撞.
			int MultipleCollisionFrames = dis.readInt();// 在特殊接口成功传送信息帧总数。
			int SQETestErrors = dis.readInt();// SQE测试错误
			int DeferredTransmissions = dis.readInt();// 延迟传输
			int LateCollisions = dis.readInt();// 晚碰撞
			int ExcessiveCollisions = dis.readInt();// 过度的碰撞
			int InternalMacTransmitErrors = dis.readInt();// 内部Mac传输错误
			int CarrierSenseErrors = dis.readInt();// 载波监听错误
			int FrameTooLongs = dis.readInt();// 帧太长
			int InternalMacReceiveErrors = dis.readInt();// 内部MAC接收错误
			int SymbolErrors = dis.readInt();// 符号错误

			SflowExtendedCountersEthernetIfData ethernetIfData = new SflowExtendedCountersEthernetIfData();
			ethernetIfData.setTag(tag);
			ethernetIfData.setLength(length);
			ethernetIfData.setAlignmentErrors(AlignmentErrors);
			ethernetIfData.setFCSErrors(FCSErrors);
			ethernetIfData.setSingleCollisionFrames(SingleCollisionFrames);
			ethernetIfData.setMultipleCollisionFrames(MultipleCollisionFrames);
			ethernetIfData.setSQETestErrors(SQETestErrors);
			ethernetIfData.setDeferredTransmissions(DeferredTransmissions);
			ethernetIfData.setLateCollisions(LateCollisions);
			ethernetIfData.setExcessiveCollisions(ExcessiveCollisions);
			ethernetIfData.setInternalMacTransmitErrors(InternalMacTransmitErrors);
			ethernetIfData.setCarrierSenseErrors(CarrierSenseErrors);
			ethernetIfData.setFrameTooLongs(FrameTooLongs);
			ethernetIfData.setInternalMacReceiveErrors(InternalMacReceiveErrors);
			ethernetIfData.setSymbolErrors(SymbolErrors);
			return ethernetIfData;
		}
		// 1101、代表Processor Counters字段
		if (tag == 1001) {
			int length = dis.readInt();// 总字节长度（不包含tag和length）
			int FiveSecCPULoad = dis.readInt();// 5秒内的CPU负载
			int OneMinCPULoad = dis.readInt();// 1分钟内的CPU负载
			int FiveMinCPULoad = dis.readInt();// 5分钟内的CPU负载
			long TotalMemory = dis.readLong();// 总的内存数
			long FreeMemory = dis.readLong();// 可用的内存数
			SflowProcessorCounterData processorCounterData = new SflowProcessorCounterData();
			processorCounterData.setTag(tag);
			processorCounterData.setLength(length);
			processorCounterData.setFiveSecCPULoad(FiveSecCPULoad);
			processorCounterData.setOneMinCPULoad(OneMinCPULoad);
			processorCounterData.setFiveMinCPULoad(FiveMinCPULoad);
			processorCounterData.setTotalMemory(TotalMemory);
			processorCounterData.setFreeMemory(FreeMemory);
			return processorCounterData;
		}
		return null;

	}

}
