package c.local.com.example;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

	public static String getItem(int count) throws IOException {
		// 使用するサーバーのURLに合わせる
		String urlSt = "https://qiita.com/api/v2/items?page=1&per_page=" + count + "&query=rxjava";
		return getHttp(urlSt);

	}

	public static String getStock(String userId) throws IOException {
		// 使用するサーバーのURLに合わせる
		String urlSt = "https://qiita.com /api/v2/users/" + userId + "/stocks";
		return getHttp(urlSt);
	}

	public static String getHttp(String urlSt) throws IOException {

		HttpURLConnection httpConn = null;
		StringBuffer result = new StringBuffer();

		// URL設定
		URL url = new URL(urlSt);

		Log.d("url:", url.toString());

		// HttpURLConnection
		httpConn = (HttpURLConnection) url.openConnection();

		// request POST
		httpConn.setRequestMethod("GET");

		// no Redirects
		httpConn.setInstanceFollowRedirects(false);

		// 時間制限
		httpConn.setReadTimeout(10000);
		httpConn.setConnectTimeout(20000);

		// 接続
		httpConn.connect();

		int resp = httpConn.getResponseCode();

		switch (resp) {
			case HttpURLConnection.HTTP_OK:
				//responseの読み込み
				final InputStream in = httpConn.getInputStream();
				String encoding = httpConn.getContentEncoding();
				if (encoding == null) {
					encoding = "UTF-8";
				}
				final InputStreamReader inReader = new InputStreamReader(in, encoding);
				final BufferedReader bufferedReader = new BufferedReader(inReader);
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					result.append(line);
				}
				bufferedReader.close();
				inReader.close();
				in.close();
				break;
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				break;
			default:
				break;
		}

		if (httpConn != null) {
			httpConn.disconnect();
		}

		return result.toString();

	}
}