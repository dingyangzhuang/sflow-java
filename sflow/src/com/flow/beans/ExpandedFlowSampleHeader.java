package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ExpandedFlowSampleHeader {
	private int tag;//当值为4的时候，flow类型的sample包
	private int length;//该字段总的字节数，不包含tag和length
	private int seq_num;//flow sample 字段序列号，依次递增（分接口记录序列号）
	private int src_type;//数据源类型（只支持接口类型，数据类型默认为0）
	private int src_index;//数据源索引(只支持接口索引)
	private int sampling_rate;//采样率
	private int sample_pool;//采样池的报文数，
	private int dropped_packets;//sflow缓存队列已满，无法入队被丢弃未处理的报文个数，
								//如果丢弃过多，建议增大采样率
	private int input_interface_format;//入接口类型 （默认为0）
	private int input_interface_value;//入接口索引值
	private int output_interface_format;//出接口类型(默认为0)
	private int output_interface_value;//出接口索引值
	private int flow_record;//包含的记录数
	@Override
	public String toString() {
		return "ExpandedFlowSampleHeader [tag=" + tag + ", length=" + length + ", seq_num=" + seq_num + ", src_type="
				+ src_type + ", src_index=" + src_index + ", sampling_rate=" + sampling_rate + ", sample_pool="
				+ sample_pool + ", dropped_packets=" + dropped_packets + ", input_interface_format="
				+ input_interface_format + ", input_interface_value=" + input_interface_value
				+ ", output_interface_format=" + output_interface_format + ", output_interface_value="
				+ output_interface_value + ", flow_record=" + flow_record + "]";
	}

	
	
}
