package c.local.com.example;


import javax.inject.Inject;

import c.local.com.example.model.PokemonResponse;
import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

public class Repository {

    private PokeApiService apiService;

    @Inject
    public Repository(PokeApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<PokemonResponse> getPokemons(){
        return apiService.getPokemons();
    }

}
