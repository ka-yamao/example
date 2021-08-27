package c.local.com.example.viewmodel;

import java.util.List;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import c.local.com.example.Repository;
import c.local.com.example.data.Pokemon;

public class HiltTestViewModel extends ViewModel {
	public static final String TAG = HiltTestViewModel.class.getSimpleName();

	private Repository repository;

	private MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();

//	public HiltTestViewModel() {
//
//	}

	@ViewModelInject
	public HiltTestViewModel(Repository repository) {
		this.repository = repository;
	}
}