package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ExpandedCountersSampleHeader {
	int tag;//类型
	int lenght;//该字段总的字节数（不包含：tag和length字段)
	int sequence_number1;//flow sample字段序列号 依次递增(分接口记录序列号) 
	int ds_class;//数据源类型 （只支持 接口类型 数据源类型默认为0）
	int ds_index;//数据源索引（接口索引值）
	int num_elements;//包含的记录数
	@Override
	public String toString() {
		return "\"ds_class\":\"" + ds_class + "\", \"ds_index\":\"" + ds_index + "\"";
	}
	

}
