package c.local.com.example.model;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import retrofit2.Response;

public class RetrofitException extends RuntimeException {

	private String message;
	private String url;
	private Response response;
	private Kind kind;
	private Throwable exception;

	public enum Kind {
		NETWORK,          // オフライン
		TIMEOUT,          // タイムアウト
		AUTH,             // 認証エラー
		CLIENT,           // クライアントエラー 400系
		SERVER,           // サーバーエラー 500系
		UNEXPECTED,       // 予期しない
	}

	RetrofitException(String message, String url, Response response, Kind kind, Throwable exception) {
		this.message = message;
		this.url = url;
		this.response = response;
		this.kind = kind;
		this.exception = exception;
	}

	public Kind getKind() {
		return kind;
	}

	public static RetrofitException asRetrofitException(Throwable throwable) {
		if (throwable instanceof RetrofitException) {
			return (RetrofitException) throwable;
		}
		// We had non-200 http error
		if (throwable instanceof HttpException) {

			String msg = throwable.getMessage();
			Response response = ((HttpException) throwable).response();
			String url = response.raw().request().url().toString();
			int code = response.code();

			Kind kind;
			if (code == 401) {
				kind = Kind.AUTH;
			} else if (code >= 400 && code < 500) {
				kind = Kind.CLIENT;
			} else if (code >= 500 && code < 600) {
				kind = Kind.SERVER;
			} else {
				kind = Kind.UNEXPECTED;
			}
			return new RetrofitException(msg, url, response, kind, throwable);
		}

		if (throwable instanceof NetworkOfflineException) {
			return new RetrofitException(throwable.getMessage(), null, null, Kind.NETWORK, throwable);
		} else if (throwable instanceof SocketTimeoutException) {
			return new RetrofitException(throwable.getMessage(), null, null, Kind.TIMEOUT, throwable);
		} else {
			return new RetrofitException(throwable.getMessage(), null, null, Kind.UNEXPECTED, throwable);
		}
	}

}
