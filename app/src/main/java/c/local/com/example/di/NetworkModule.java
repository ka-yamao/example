package c.local.com.example.di;


import com.squareup.moshi.Moshi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import c.local.com.example.DLog;
import c.local.com.example.HttpBinService;
import c.local.com.example.PokeAPIService;
import c.local.com.example.adapter.ErrorHandlingAdapter;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
	public static final String TAG = NetworkModule.class.getSimpleName();

	/**
	 * PokeAPIService の具象クラス
	 *
	 * @return PokeAPIService
	 */
	@Provides
	@Singleton
	public static PokeAPIService providePokemonApiService() {

		Moshi moshi = new Moshi.Builder().build();
		return new Retrofit.Builder()
				.baseUrl("https://pokeapi.co/api/v2/")                    // ベースURLの設定
				.addConverterFactory(MoshiConverterFactory.create(moshi)) // JSON パーサーに Moshi を設定
				.addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Rxjava を利用するので設定
				.client(createHttpClient())                               // HTTPクライアントを設定
				.build()
				.create(PokeAPIService.class);    // APIのインターフェースを設定
	}

	/**
	 * HttpBinService の具象クラス
	 *
	 * @return PokeAPIService
	 */
	@Provides
	@Singleton
	public static HttpBinService provideHttpBinService() {

		return new Retrofit.Builder()
				.baseUrl("https://httpbin.org/")                    // ベースURLの設定
				.addCallAdapterFactory(new ErrorHandlingAdapter.ErrorHandlingCallAdapterFactory())
				.addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder().build())) // JSON パーサーに Moshi を設定
				.addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Rxjava を利用するので設定
				.client(createHttpClient())                               // HTTPクライアントを設定
				.build()
				.create(HttpBinService.class);    // APIのインターフェースを設定
	}

	/**
	 * HTTPヘッダーを操作、URLのログを出力するため
	 *
	 * @return
	 */
	private static OkHttpClient createHttpClient() {

		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
		httpClient.connectionPool(new ConnectionPool(100, 30, TimeUnit.SECONDS));

		httpClient.addInterceptor(chain -> {
			Request original = chain.request();

			DLog.d(TAG, original.url().toString());

			//header設定
			Request request = original.newBuilder()
					.header("Accept", "application/json")
					.method(original.method(), original.body())
					.build();

			okhttp3.Response response = chain.proceed(request);

			return response;
		});

		return httpClient.build();
	}
}