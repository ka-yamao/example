package c.local.com.example.viewmodel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import c.local.com.example.DLog;
import c.local.com.example.Repository;
import c.local.com.example.data.Pokemon;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HiltTestViewModel extends ViewModel {
	public static final String TAG = HiltTestViewModel.class.getSimpleName();

	private Repository repository;
	private MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();

	public HiltTestViewModel(Repository repository) {
		this.repository = repository;

	}

	public MutableLiveData<List<Pokemon>> getPokemonList() {
		return pokemonList;
	}

	public void getPokemons() {
		repository.getPokemons()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
					pokemonList.setValue(result.toPokemonList());
				}, error -> {
					DLog.d(TAG, error.getMessage());
				});
	}


}