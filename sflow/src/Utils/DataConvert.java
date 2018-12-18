package Utils;

import java.util.HashMap;
import java.util.Map;

public class DataConvert {
	public static Map<Integer, String> map = new HashMap<>();
	static{
		map.put(6, "TCP(6)");
		map.put(17,"UDP(17)");
		map.put(1, "ICMP(1)");
		map.put(2, "IGMP(2)");	 
	}
	
	/**
	 * 返回IP协议号对应的协议信息
	 * @param ip_proto
	 * @return
	 */
	public static String getIpProtocol2Str(int ip_proto){	
		return null!=map.get(ip_proto)?map.get(ip_proto):"unkonw";	
	}
	
	/**
	 * mac地址格式化
	 */
	public static String getMacFromLong(long mac_long){
		String mac_temp = Long.toHexString(mac_long);
		StringBuffer head = new StringBuffer();
		for(int i=0;i<16-mac_temp.length();i++){
			head.append("0");
		}
		head.append(mac_temp);
		return head.toString().substring(0,head.length()-4);
		
	}
}
