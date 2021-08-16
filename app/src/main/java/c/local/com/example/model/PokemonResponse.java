package c.local.com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import c.local.com.example.data.Pokemon;


public class PokemonResponse {
	public Integer count;
	public String next, previous;
	public List<Poke> results;

	public List<Pokemon> toPokemonList() {
		List<Pokemon> list = new ArrayList<>();
		if (results == null) return list;
		for (Poke poke : results) {
			Pattern pattern = Pattern.compile("/(\\d+)/");
			Matcher matcher = pattern.matcher(poke.url);
			if (matcher.find()) {
				int id = Integer.parseInt(matcher.group(1));
				// pokemon.id = Integer.parseInt(id);
				String url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + id + ".png";
				Pokemon p = new Pokemon(id, poke.name, url);
				list.add(p);
			}
		}
		return list;
	}
}

class Poke {
	public String name;
	public String url;
}