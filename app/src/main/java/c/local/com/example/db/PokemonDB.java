package c.local.com.example.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import c.local.com.example.data.Pokemon;


@Database(entities = {Pokemon.class},version = 2,exportSchema = false)
public abstract class PokemonDB extends RoomDatabase {
        public abstract PokeDao pokeDao();
}
