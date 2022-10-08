package c.local.com.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	static final String URL = "https://www.tour.ne.jp/j_hotel/";
	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Web Viewの初期設定
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient()); // WebViewを設定する
		webView.getSettings().setJavaScriptEnabled(true); // JavaScriptを有効にする
		webView.loadUrl(URL); // URLを読み込む
		WebSettings webViewSettings = webView.getSettings();

		webViewSettings.setJavaScriptEnabled(true);
		webViewSettings.setGeolocationEnabled(true);
		webViewSettings.setAllowFileAccess(false);
		webViewSettings.setAllowContentAccess(false);
		webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		// JavascriptInterfaceを追加！
		webView.addJavascriptInterface(new JSObject(webView), "Android");

		webView.setWebChromeClient(new WebChromeClient(){
			public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
				super.onGeolocationPermissionsShowPrompt(origin, callback);

			}

		});
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				webView.loadUrl("javascript:let button = document.getElementById('Act_clearCondition'); button.addEventListener('click', function (event) { Android.nativeFunc('hoge') })");
			}
		});
	}
	class JSObject {
		private WebView mWebView;

		public JSObject(WebView webView) {
			this.mWebView = webView;
		}

		@JavascriptInterface
		public String nativeFunc(String str) {
			Log.d("xxx", "hook " + str);
			return "false";
		}
	}
}