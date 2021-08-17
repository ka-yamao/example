package c.local.com.example.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import c.local.com.example.data.Pokemon;

/**
 * Created by Abhinav Singh on 17,June,2020
 */

@Dao
public interface PokeDao {

    @Insert
    void insertPokemon(Pokemon pokemon);

    @Query("DELETE FROM favorite_table WHERE name = :pokemonName")
    void deletePokemon(String pokemonName);

    @Query("DELETE FROM favorite_table")
    void deleteAll();

    @Query("SELECT * FROM favorite_table")
    LiveData<List<Pokemon>> getFavoritePokemons();
}
