package c.local.com.example;


import com.squareup.moshi.Moshi;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class NetworkModule {

	public static PokeAPIService providePokemonApiService() {

		Moshi moshi = new Moshi.Builder().build();
		return new Retrofit.Builder()
				.baseUrl("https://pokeapi.co/api/v2/")
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
				.build()
				.create(PokeAPIService.class);
	}

	public static PokeAPIService providePokemonApiServiceGson() {

		return new Retrofit.Builder()
				.baseUrl("https://pokeapi.co/api/v2/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
				.build()
				.create(PokeAPIService.class);
	}
}