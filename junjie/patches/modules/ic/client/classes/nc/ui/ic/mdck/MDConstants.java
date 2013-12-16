package nc.ui.ic.mdck;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 码单固定参数设置
 * 
 * @author heyq
 * 
 */
public class MDConstants {

	public static final int ZS_XSW = 0; // 支数的小数位
	
	//add by ouyangzhb 2011-07-28 需求调整，小数位改为3位
	public static final int ZL_XSW = 3;// 重量小数位

//	public static final int ZL_XSW = 4;// 重量小数位

	public static final int DJ_XSW = 2;// 单价

	public static final int JE_XSW = 2;// 金额

	public static String getCurrentDateTime() {
		String currentTime = null;
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentTime = df.format(date);
		return currentTime;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		String currentDate = null;
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = df.format(date);
		return currentDate;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		String currentTime = null;
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		currentTime = df.format(date);
		return currentTime;
	}
}
