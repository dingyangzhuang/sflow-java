package com.flow.receive;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.w3c.dom.css.Counter;

import com.flow.beans.ExpandedCountersSampleHeader;
import com.flow.beans.ExpandedFlowSampleHeader;
import com.flow.beans.SflowEthernetData;
import com.flow.beans.SflowExtendedCounterGeneralIfData;
import com.flow.beans.SflowExtendedCountersEthernetIfData;
import com.flow.beans.SflowExtendedRouterData;
import com.flow.beans.SflowExtendedSwitchData;
import com.flow.beans.SflowHeader;
import com.flow.beans.SflowIpv4Data;
import com.flow.beans.SflowProcessorCounterData;
import com.flow.beans.SflowRawpkthdr;

import Utils.DataConvert;
import Utils.IpOperation;
import Utils.TimeUtils;

/**
 * 用于前期功能测试
 * 
 * @author thinkpad T440S
 *
 */
public class Test {
	public static void main(String[] args) throws IOException {
		// 创建监听
		DatagramSocket udpSocket = new DatagramSocket(6343);
		byte[] buf = new byte[2048];
		while (true) {

			DatagramPacket udpPacket = new DatagramPacket(buf, buf.length);
			// 接收数据
			udpSocket.receive(udpPacket);
			// 将数据包的数据解析成字节流，把字节流传给数据输入流
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(udpPacket.getData()));

			// sflow 头部信息
			SflowHeader sflowHeader = getSflowHeader(dis);

			// sflow具体信息
			int NumSamples = sflowHeader.getNumSamples();
			// 循环遍历sflow包中包含的所有元素
			while (NumSamples > 0) {
				// 此处为3代表 expanded flow sample 字段
				// 此处为4代表expanded counters sample字段
				int tag = dis.readInt();
				if (tag == 4) {
					getCountersSampleData(dis, sflowHeader);
				}

				if (tag == 3) {
					/***** 获取flow sample头信息 ******/
					getFlowSampleData(dis, sflowHeader);
				}
				NumSamples--;
			}

		}

	}

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

	/**
	 * 获取counters sample的具体信息，包括头和体
	 * 
	 * @param sflowHeader
	 * @throws IOException
	 */
	public static void getCountersSampleData(DataInputStream dis, SflowHeader sflowHeader) throws IOException {
		/************** expanded counters sample *********************/
		ExpandedCountersSampleHeader countersSampleHeader = getExpandedCountersSampleHeader(dis);
		int i = countersSampleHeader.getNum_elements();
		// 根据元素的个数进行处理
		while (i > 0) {
			/** 处理元素的方法 **/
			/** sflow_extendedcounter_generalif_data **/
			Object counter = getExpandedCountersSampleDate(dis);
			if (null != counter) {
				System.out.print(sflowHeader + " ");
				System.out.print(countersSampleHeader + " ");
				System.out.println(counter.toString());
			}
			i--;
		}
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

	/**
	 * 获取flow sample header信息 ExpandedFlowSampleHeader的信息 当值为3的时候，flow类型的sample包
	 * 
	 * @throws IOException
	 */
	public static ExpandedFlowSampleHeader getExpandedFlowSampleHeader(DataInputStream dis) throws IOException {
		int length = dis.readInt();// 该字段总的字节数，不包含tag和length
		int seq_num = dis.readInt();// flow sample 字段序列号，依次递增（分接口记录序列号）
		int src_type = dis.readInt();// 数据源类型（只支持接口类型，数据类型默认为0）
		int src_index = dis.readInt();// 数据源索引(只支持接口索引)
		int sampling_rate = dis.readInt();// 采样率
		int sample_pool = dis.readInt();// 采样池的报文数，
		int dropped_packets = dis.readInt();// sflow缓存队列已满，无法入队被丢弃未处理的报文个数，
		// 如果丢弃过多，建议增大采样率
		int input_interface_format = dis.readInt();// 入接口类型?（默认为0）
		int input_interface_value = dis.readInt();// 入接口索引值
		int output_interface_format = dis.readInt();// 出接口类型(默认为0)
		int output_interface_value = dis.readInt();// 出接口索引值
		int flow_record = dis.readInt();// 包含的记录数
		ExpandedFlowSampleHeader fsHeader = new ExpandedFlowSampleHeader();
		fsHeader.setTag(3);
		fsHeader.setLength(length);
		fsHeader.setSeq_num(seq_num);
		fsHeader.setSrc_type(src_type);
		fsHeader.setSrc_index(src_index);
		fsHeader.setSampling_rate(sampling_rate);
		fsHeader.setSample_pool(sample_pool);
		fsHeader.setDropped_packets(dropped_packets);
		fsHeader.setInput_interface_format(input_interface_format);
		fsHeader.setInput_interface_value(input_interface_value);
		fsHeader.setOutput_interface_format(output_interface_format);
		fsHeader.setOutput_interface_value(output_interface_value);
		fsHeader.setFlow_record(flow_record);
		return fsHeader;
	}

	/**
	 * 获取flow sample的具体信息，包括头和体
	 * 
	 * @param sflowHeader
	 * @throws IOException
	 */
	public static void getFlowSampleData(DataInputStream dis, SflowHeader sflowHeader) throws IOException {
		/************** expanded counters sample *********************/
		ExpandedFlowSampleHeader FlowSampleHeader = getExpandedFlowSampleHeader(dis);
		int i = FlowSampleHeader.getFlow_record();
		System.out.print(sflowHeader + " ");
		System.out.print(FlowSampleHeader + " ");
		// 根据元素的个数进行处理
		while (i > 0) {
			/** 处理元素的方法 **/
			Object counter = getExpandedFlowSampleDate(dis);
			if (null != counter) {								
				System.out.print(counter.toString());
			}
			i--;
		}
		System.out.println();
	}

	/**
	 * 当值为1的时候，flow类型的sample包的Raw Packet Header类型
	 * 当值为2的时候，flow类型的sample包的Ethernet Frame Data类型 当值为3的时候，flow类型的sample包的IPV4
	 * Data类型 当值为1002的时候，flow类型的sample包的extended router data类型
	 * 当值为1001的时候，flow类型的sample包的Extended Switch Data类型
	 * 
	 * @author thinkpad T440S header counter类型数据的包头，用于与子信息进拼接
	 * @return
	 * @throws IOException
	 */
	public static Object getExpandedFlowSampleDate(DataInputStream dis) throws IOException {
		int tag = dis.readInt();// 读取类型tag，1、2、1101
		// 当值为1的时候，flow类型的sample包的Raw Packet Header类型
		if (tag == 1) {
			int length = dis.readInt();// 该字段总的字节数，不包含tag和length
			int header_protocol = dis.readInt();// 原始数据mac协议类型（1代表以太网类型报文）
			int frame_length = dis.readInt();// 原始报文总字节数
			int payload_removed = dis.readInt();// 截取报文时被忽略的字节数
			int original_packet_length = dis.readInt();// 截取的原始报文总字节数
			/******** 截取了数据包，这里不做分析，所以跳过 ***********/
			byte[] buff = new byte[original_packet_length];
			dis.read(buff);

			SflowRawpkthdr rawpkthdr = new SflowRawpkthdr();
			rawpkthdr.setTag(1);
			rawpkthdr.setLength(length);
			rawpkthdr.setHeader_protocol(header_protocol);
			rawpkthdr.setFrame_length(frame_length);
			rawpkthdr.setPayload_removed(payload_removed);
			rawpkthdr.setOriginal_packet_length(original_packet_length);
			return rawpkthdr;
		}
		// 当值为2的时候，flow类型的sample包的Ethernet Frame Data类型
		if (tag == 2) {
			int length = dis.readInt();// 该字段总的字节数，不包含tag和length
			int len_mac_packet = dis.readInt();// 原始报文总字节数
			long src_mac = dis.readLong();// 源MAC地址8字节，最后2字节全为0
			long des_mac = dis.readLong();// 目的MAC地址8字节，最后2字节全为0
			int ethernet_packet_type = dis.readInt();// Ethernet包类型，2048：Internet
														// IP (IPv4)
			SflowEthernetData ethernetIfData = new SflowEthernetData();
			ethernetIfData.setTag(2);
			ethernetIfData.setLength(length);
			ethernetIfData.setLen_mac_packet(len_mac_packet);
			ethernetIfData.setSrc_mac(DataConvert.getMacFromLong(src_mac));
			ethernetIfData.setDes_mac(DataConvert.getMacFromLong(des_mac));
			ethernetIfData.setEthernet_packet_type(ethernet_packet_type);

			return ethernetIfData;
		}
		// 当值为3的时候，flow类型的sample包的IPV4 Data类型
		if (tag == 3) {
			int length = dis.readInt();// 该字段总的字节数，不包含tag和length
			int len_ip_packet = dis.readInt();// 原始报文三层头以及后续报文的总字节长度
			int ip_proto = dis.readInt();// 协议TCP:6 UDP:17 ICMP(1) IGMP（2）
			int src_ip = dis.readInt();// 源IP
			int des_ip = dis.readInt();// 目的IP
			int src_port = dis.readInt();// 源端口
			int des_port = dis.readInt();// 目的端口
			int tcp_flag = dis.readInt();// （TCP包才有）
			int tos = dis.readInt();//
			SflowIpv4Data ipv4Data = new SflowIpv4Data();
			ipv4Data.setTag(3);
			ipv4Data.setLength(length);
			ipv4Data.setLen_ip_packet(len_ip_packet);
			ipv4Data.setIp_proto(DataConvert.getIpProtocol2Str(ip_proto));
			ipv4Data.setSrc_ip(IpOperation.getIpFromLong((long)src_ip));
			ipv4Data.setDes_ip(IpOperation.getIpFromLong((long)des_ip));
			ipv4Data.setSrc_port(src_port);
			ipv4Data.setDes_port(des_port);
			ipv4Data.setTcp_flag(tcp_flag);
			ipv4Data.setTos(tos);
			return ipv4Data;
		}
		// 当值为1001的时候，flow类型的sample包的Extended Switch Data类型
		if (tag == 1001) {
			int length=dis.readInt();//该字段总的字节数，不包含tag和length
			int src_vlan=dis.readInt();//入向的vlan id
			int src_priority=dis.readInt();//入向的优先级
			int dst_vlan=dis.readInt();//出向的vlan id
			int dst_priority=dis.readInt();//出向的优先级
			SflowExtendedSwitchData extendedSwitchData = new SflowExtendedSwitchData();
			extendedSwitchData.setTag(1001);
			extendedSwitchData.setLength(length);
			extendedSwitchData.setSrc_vlan(src_vlan);
			extendedSwitchData.setSrc_priority(src_priority);
			extendedSwitchData.setDst_vlan(dst_vlan);
			extendedSwitchData.setDst_priority(dst_priority);
			return extendedSwitchData;
		}
		// 当值为1002的时候，flow类型的sample包的extended router data类型
		if (tag == 1002) {
			int length=dis.readInt();//该字段总的字节数，不包含tag和length
			long nextHop=dis.readLong();//下一跳IP
			int nextHop_source_mask=dis.readInt();//源IP匹配转发表中的掩码
			int nextHop_destination_mask=dis.readInt();//目的IP匹配转发表中的掩码
			SflowExtendedRouterData extendedRouterData = new SflowExtendedRouterData();
			extendedRouterData.setTag(1002);
			extendedRouterData.setLength(length);
			extendedRouterData.setNextHop(IpOperation.getIpFromLong(nextHop));
			extendedRouterData.setNextHop_source_mask(nextHop_source_mask);
			extendedRouterData.setNextHop_destination_mask(nextHop_destination_mask);
			
			return extendedRouterData;
		}
		return null;

	}
}
