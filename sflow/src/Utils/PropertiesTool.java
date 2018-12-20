package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件工具类
 * @author thinkpad T440S
 *
 */
public class PropertiesTool {
	public static FileInputStream fis;
	public static Properties pro;
	static{
		String path = System.getProperty("user.dir")+"/conf/sflow.properties";
		//创建properties对象
		pro = new Properties();
		try {
			//读取文件输入流
			fis = new FileInputStream(new File(path));
			//从输入流中载入配置
			pro.load(fis);
			//关闭输入流
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
