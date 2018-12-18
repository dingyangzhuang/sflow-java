package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
/**
 * 针对路由转发的报文，记录报文的路由转发信息
 * @author thinkpad T440S
 *
 */
public class SflowExtendedRouterData {
	private int tag;//当值为1002的时候，flow类型的sample包的extended router data类型
	private int length;//该字段总的字节数，不包含tag和length
	private String nextHop;//下一跳IP	8字节
	private int nextHop_source_mask;//源IP匹配转发表中的掩码
	private int nextHop_destination_mask;//目的IP匹配转发表中的掩码
	@Override
	public String toString() {
		return "SflowExtendedRouterData [tag=" + tag + ", length=" + length + ", nextHop=" + nextHop
				+ ", nextHop_source_mask=" + nextHop_source_mask + ", nextHop_destination_mask="
				+ nextHop_destination_mask + "]";
	}

	
}
