package com.c.local.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private int REQUEST_PERMISSION_LOCATION = 100;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createWebView();
		webView.loadUrl("https://www.tour.ne.jp/j_rentacar/");
	}

	private void createWebView() {

		webView = findViewById(R.id.webView);

		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDatabaseEnabled(true);

		// javascript有効
		webView.getSettings().setJavaScriptEnabled(true);
		// Application Cacheを無効
		webView.getSettings().setAppCacheEnabled(true);
		// キャッシュ格納場所のパス
		webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
		// ファイルアクセスを許可
		webView.getSettings().setAllowFileAccess(true);
		// cacheあり：cacheを使用、cacheなし：ネットワーク経由でデータを取得
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// ユーザーエージェントへアプリのバージョンコード、バージョン名を追加
		// webView.getSettings().setUserAgentString(PrefUpdate.getCustomUserAgent(activity));
		// Local Storage の有効
		webView.getSettings().setDomStorageEnabled(true);
		// 位置情報
		webView.getSettings().setGeolocationEnabled(true);
		// ex) window.openのハンドリング
		webView.getSettings().setSupportMultipleWindows(true);
		// キャッシュクリア
		webView.clearCache(true);

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		// WebViewのDebugを有効
		WebView.setWebContentsDebuggingEnabled(true);
		// プログレスバーの進捗を更新するため「WebChromeClient」を設定
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
			}


			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
				super.onGeolocationPermissionsShowPrompt(origin, callback);

				if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
						|| checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					// 現在位置の権限があったらコールバック
					callback.invoke(origin, true, false);
				} else {
					// 現在位置の権限がないため、パーミッション確認アラートを表示する
					String[] permissions;
					if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
						// Android11（R：API30）以下は、正確のみ
						permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
					} else {
						// Android12（S：API31）以降は、正確 と おおよそ
						permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
					}
					requestPermissions(permissions, REQUEST_PERMISSION_LOCATION);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
				super.onJsAlert(view, url, message, result);
				return true;
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
				return true;
			}

			@Override
			public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
				return true;
			}
		});

		// WebViewクライアントを設定
		webView.setWebViewClient(new WebViewClient() {

			/**
			 * Basic認証
			 * @param view
			 * @param handler
			 * @param host
			 * @param realm
			 */
			@Override
			public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {

			}

			@Override
			public void onPageStarted(WebView view, String url,
									  Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend, Message resend) {
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			}
		});
	}

	/**
	 * 権限チェックの結果を受け取る
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// 位置情報の権限リクエスト結果  ※アラート表示中に画面回転すると「grantResults.length」が0になるらしい
		if (requestCode == REQUEST_PERMISSION_LOCATION && grantResults.length > 0) {

			boolean isAllow; // 権限リクエストの結果 許可：true、拒否：false
			boolean isShowDialog; // 権限ダイアログを今後表示可能か判定 表示可能:true, 表示不可能：false

			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
				// Android11（R：API30）以下
				// 配列 0: 正確 の権限が許可されか 許可：true、拒否：false
				isAllow = grantResults[0] == PackageManager.PERMISSION_GRANTED;
				isShowDialog = shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION);
			} else {
				// Android12（S：API31）以上
				// 配列 0: 正確、配列 1: おおよそ  どちらかの権限が許可されたか 許可：true、拒否：false
				isAllow = grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED;
				// 正確、おおよそ  どちらかのダイアログが表示可能か確認
				isShowDialog = shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
						|| shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION);
			}

			if (isAllow) {
				Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "NG", Toast.LENGTH_LONG).show();
			}
		}
	}
}