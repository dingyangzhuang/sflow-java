package com.flow.analyze;

import java.io.DataInputStream;
import java.io.IOException;

import com.flow.beans.ExpandedFlowSampleHeader;
import com.flow.beans.SflowEthernetData;
import com.flow.beans.SflowExtendedRouterData;
import com.flow.beans.SflowExtendedSwitchData;
import com.flow.beans.SflowHeader;
import com.flow.beans.SflowIpv4Data;
import com.flow.beans.SflowRawpkthdr;
import com.flow.output.OutPutRedis;

import Utils.DataConvert;
import Utils.IpOperation;

public class SflowFlowSampleMethod {
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
		StringBuffer str = new StringBuffer();
		/************** expanded counters sample *********************/
		ExpandedFlowSampleHeader FlowSampleHeader = getExpandedFlowSampleHeader(dis);
		int i = FlowSampleHeader.getFlow_record();
		str.append(sflowHeader + " "+FlowSampleHeader + " ");
		
		// 根据元素的个数进行处理
		while (i > 0) {
			/** 处理元素的方法 **/
			Object counter = getExpandedFlowSampleDate(dis);
			if (null != counter) {								
				str.append(counter.toString());
			}
			i--;
		}
		OutPutRedis.writeRedis(str.toString());
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
