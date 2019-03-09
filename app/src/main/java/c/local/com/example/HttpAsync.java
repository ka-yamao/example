package c.local.com.example;

import android.os.AsyncTask;

import java.io.IOException;

public class HttpAsync extends AsyncTask<String, Void, String> {

	private Listener listener;

	// 非同期処理
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		try {
			result = HttpConnection.getQiita();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	// 非同期処理が終了後、結果をメインスレッドに返す
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (listener != null) {
			listener.onSuccess(result);
		}
	}

	void setListener(Listener listener) {
		this.listener = listener;
	}

	interface Listener {
		void onSuccess(String result);
	}
}