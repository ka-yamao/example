package c.local.com.example;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface PokeAPIService {

	@GET("pokemon")
	Observable<PokemonResponse2> getPokemons();
}
