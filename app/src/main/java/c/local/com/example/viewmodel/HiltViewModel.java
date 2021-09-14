package c.local.com.example.viewmodel;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import c.local.com.example.di.Repository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class HiltViewModel extends ViewModel {
	public static final String TAG = HiltViewModel.class.getSimpleName();

	// Repositoryクラス
	private final Repository mRepository;
	// まとめて Dispose するクラス
	private CompositeDisposable compositeDisposable = new CompositeDisposable();
	// LiveData
	private MutableLiveData<String> mLiveData = new MutableLiveData<>();

	@ViewModelInject
	public HiltViewModel(Repository repository) {
		// リポジトリ
		mRepository = repository;
		// LiveData を渡して、Observable を生成し、subscribe で LiveData を更新する
		Disposable disposable = mRepository.createObservableSubscribe(mLiveData);
		compositeDisposable.add(disposable);

	}

	public MutableLiveData<String> getLiveData() {
		return mLiveData;
	}

	public void fetch(int code) {
		mLiveData.postValue("fetch");
		mRepository.fetch(code);
	}

	public void fetchErr(int code) {
		mLiveData.postValue("fetch");
		mRepository.fetch(code);
	}


	@Override
	protected void onCleared() {
		super.onCleared();
		compositeDisposable.clear();
	}
}