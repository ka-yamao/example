package c.local.com.example;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PokeAPIService {

	@GET("pokemon/")
	Observable<PokemonResponse> getPokemons(@QueryMap Map<String, String> query);
}
