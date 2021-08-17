package c.local.com.example;


import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import c.local.com.example.data.Pokemon;
import c.local.com.example.db.PokeDao;
import c.local.com.example.model.PokemonResponse;
import io.reactivex.rxjava3.core.Observable;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

public class Repository {

    private PokeDao pokeDao;
    private PokeAPIService apiService;

    @Inject
    public Repository(PokeDao pokeDao, PokeAPIService apiService) {
        this.pokeDao = pokeDao;
        this.apiService = apiService;
    }


    public Observable<PokemonResponse> getPokemons(){
        return apiService.getPokemons(new HashMap<>());
    }

    public void insertPokemon(Pokemon pokemon){
        pokeDao.insertPokemon(pokemon);
    }

    public void deletePokemon(String pokemonName){
        pokeDao.deletePokemon(pokemonName);
    }

    public void deleteAll(){
        pokeDao.deleteAll();
    }

    public LiveData<List<Pokemon>> getFavoritePokemon(){
        return pokeDao.getFavoritePokemons();
    }
}
