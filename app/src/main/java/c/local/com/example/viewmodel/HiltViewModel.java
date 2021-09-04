package c.local.com.example.viewmodel;

import java.util.List;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import c.local.com.example.di.Repository;
import c.local.com.example.data.Pokemon;
import c.local.com.example.data.PokemonListInfo;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class HiltViewModel extends ViewModel {
	public static final String TAG = HiltViewModel.class.getSimpleName();

	// Repositoryクラス
	private final Repository mRepository;
	// まとめて Dispose するクラス
	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	// ポケモンリストのデータ
	private MediatorLiveData<List<Pokemon>> mPokemonListLiveData = new MediatorLiveData<>();
	// LiveData
	private MediatorLiveData<PokemonListInfo> mPokemonListInfoLiveData = new MediatorLiveData<>();

	private MutableLiveData<List<Pokemon>> pokemonList = new MutableLiveData<>();

	@ViewModelInject
	public HiltViewModel(Repository repository) {
		// リポジトリ
		mRepository = repository;
		// LiveData を渡して、Observable を生成し、subscribe で LiveData を更新する
		Disposable disposable = mRepository.createObservableSubscribe(mPokemonListLiveData, mPokemonListInfoLiveData);
		compositeDisposable.add(disposable);
	}

	/**
	 * ポケモンリストの取得、追加読み込み
	 */
	public void fetch() {
		mRepository.fetch(new PokemonListInfo());
	}

	public LiveData<List<Pokemon>> getPokemonListLiveData() {
		return mPokemonListLiveData;
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		compositeDisposable.clear();
	}
}