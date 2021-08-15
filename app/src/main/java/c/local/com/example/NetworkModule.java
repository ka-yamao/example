package c.local.com.example;


import com.squareup.moshi.Moshi;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NetworkModule {
	public static final String TAG = NetworkModule.class.getSimpleName();

	public static PokeAPIService providePokemonApiService() {

		Moshi moshi = new Moshi.Builder().build();
		return new Retrofit.Builder()
				.baseUrl("https://pokeapi.co/api/v2/")
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
				.client(createHttpClient())
				.build()
				.create(PokeAPIService.class);
	}

	/**
	 * HTTPヘッダーを操作、URLのログを出力するため
	 *
	 * @return
	 */
	private static OkHttpClient createHttpClient() {

		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
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