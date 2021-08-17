package c.local.com.example.di;

import javax.inject.Singleton;

import c.local.com.example.PokeAPIService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

@Module
@InstallIn(ApplicationComponent.class)
public class NetModule {

    @Provides
    @Singleton
    public static PokeAPIService providePokemonApiService(){

        return  new Retrofit.Builder()
                .baseUrl(" https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(PokeAPIService.class);
    }
}
