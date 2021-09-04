package c.local.com.example.di;

import javax.inject.Singleton;

import c.local.com.example.PokeApiService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

@Module
@InstallIn(ApplicationComponent.class)
public class NetModule {

	@Provides
	@Singleton
	public static PokeApiService providePokemonApiService() {

		return new Retrofit.Builder()
				.baseUrl(" https://pokeapi.co/api/v2/")
				.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
				.build()
				.create(PokeApiService.class);
	}
}
