package com.flow.beans;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
/**
 *针对二层转发的Ethernet报文，记录报文的vlan转换以及vlan优先级的转换。 
 * @author thinkpad T440S
 *
 */
public class SflowExtendedSwitchData {
	private int tag;//当值为1001的时候，flow类型的sample包的Extended Switch Data类型
	private int length;//该字段总的字节数，不包含tag和length
	private int src_vlan;//入向的vlan id
	private int src_priority;//入向的优先级
	private int dst_vlan;//出向的vlan id
	private int dst_priority;//出向的优先级
	@Override
	public String toString() {
		return "\"src_vlan\":\"" + src_vlan + "\", \"dst_vlan\":\"" + dst_vlan + "\"";
	}
	

}
