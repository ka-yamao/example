package c.local.com.example;


import java.util.HashMap;

import javax.inject.Inject;

import c.local.com.example.model.PokemonResponse;
import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

public class Repository {

    private PokeAPIService apiService;

    @Inject
    public Repository(PokeAPIService apiService) {
        this.apiService = apiService;
    }


    public Observable<PokemonResponse> getPokemons(){
        return apiService.getPokemons(new HashMap<>());
    }
}
