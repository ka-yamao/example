package c.local.com.example.viewmodel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import c.local.com.example.data.Pokemon;

public class HiltTestViewModel extends ViewModel {
	public static final String TAG = HiltTestViewModel.class.getSimpleName();


	private MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();

	public HiltTestViewModel() {

	}

	public MutableLiveData<List<Pokemon>> getPokemonList() {
		return pokemonList;
	}

	public void getPokemons() {

	}


}