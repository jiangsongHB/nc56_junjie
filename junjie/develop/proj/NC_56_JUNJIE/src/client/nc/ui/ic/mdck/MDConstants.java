package nc.ui.ic.mdck;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �뵥�̶���������
 * 
 * @author heyq
 * 
 */
public class MDConstants {

	public static final int ZS_XSW = 0; // ֧����С��λ

	public static final int ZL_XSW = 4;// ����С��λ

	public static String getCurrentDateTime() {
		String currentTime = null;
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentTime = df.format(date);
		return currentTime;
	}

	/**
	 * ��ȡ��ǰ����
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
	 * ��ȡ��ǰʱ��
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