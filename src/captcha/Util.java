package captcha;

public class Util {
	public static String upload() {
		String t = System.getProperty("os.name");
		String upload = "/pukyung16/upload/";
		if( t.indexOf("indows") != -1 ) {
			upload = "C:\\upload\\";
		}
		return upload;
	}
}
