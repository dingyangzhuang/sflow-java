package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeUtils {
	public static String getTimeFromLong(long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(time);
		return dateString;
		
	}
	/**
	 * 通过秒数，获取小时总数
	 * @param time
	 * @return
	 */
	public static String getTimeFromSeconds(long time){
		int hour = (int) (time/3600000);
		int minute = (int) ((time%3600000)/60);
		int second = (int) ((time%3600000)%60);
		return hour+" hours,"+minute+" minutes,"+second+" seconds";
		
	}
}
