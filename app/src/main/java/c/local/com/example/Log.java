package c.local.com.example;

public class Log {
	public static void d(String tag, String log) {
		Log.d("★" + tag, log);
	}

	public static void s(String tag, String log) {
		Log.d("★★★★★★" + tag + "★★★★★★", log);
	}
}
