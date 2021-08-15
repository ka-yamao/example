package c.local.com.example.viewmodel;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import c.local.com.example.BasicApp;
import c.local.com.example.PokeAPIService;
import c.local.com.example.data.PokemonListInfo;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxJavaViewModel extends AndroidViewModel {

	private MediatorLiveData<List<String>> mLogListLiveData = new MediatorLiveData<>();

	private CompositeDisposable compositeDisposable = new CompositeDisposable();

	// 検索のサブジェクト
	private PublishSubject<PokemonListInfo> mPublishSubject;
	// 検索実行のプロセル管理
	public PublishProcessor<PokemonListInfo> mPublishProcessor;
	// API
	private PokeAPIService mApiService;

	public enum ButtonType {
		PROCESS,
		SUBJECT,
		ERROR,
		BASH
	}

	public RxJavaViewModel(@NonNull Application application,
							 @NonNull SavedStateHandle savedStateHandle) {
		super(application);
		mLogListLiveData = new MediatorLiveData<>();
		mLogListLiveData.setValue(new ArrayList<>());
		mApiService = BasicApp.getApp().getApi();
		mPublishProcessor = PublishProcessor.create();
		mPublishProcessor.onBackpressureLatest().observeOn(Schedulers.from(Executors.newCachedThreadPool()), false, 1).subscribe(p -> {
			log("processor");
			mPublishSubject.onNext(p);
		});
		Disposable disposable = createObservable();
		compositeDisposable.add(disposable);
	}


	public Disposable createObservable() {
		mPublishSubject = PublishSubject.create();
		return mPublishSubject
				.throttleLast(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
				.concatMap(info -> {
					log("subject");
					return mApiService.getPokemons(info.toQueryMap());
				}, 1)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
							log("success");
						},
						error -> {
							log("error" + error.getMessage());
						}, () -> {
							log("complete");
						});
	}



	public void fetch(int index) {

		log("index :" + index);
		switch (index) {
			case 0:
				log("onNext");
				mPublishProcessor.onNext(new PokemonListInfo());
				break;
			case 1:
				log("onNext");
				mPublishSubject.onNext(new PokemonListInfo());
				break;
			case 2:
				log("onNext");
				mPublishSubject.onNext(new PokemonListInfo(0, "sdss"));
				break;
			case 3:
				for (int i = 0; i < 10 ; i++) {
					log("onNext " + i);
					mPublishProcessor.onNext(new PokemonListInfo());
				}
				break;
			case 4:
				for (int i = 0; i < 10 ; i++) {
					log("onNext " + i);
					mPublishSubject.onNext(new PokemonListInfo());
				}
				break;
			case 999:
				for (int i = 0; i < 10 ; i++) {
					mLogListLiveData.postValue(new ArrayList<>());
				}
				break;
		}
	}

	public MediatorLiveData<List<String>> getLogListLiveData() {
		return mLogListLiveData;
	}
	public void log(String log) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh時mm分ss秒SSSミリ秒 ");
		String l = sdf.format(calendar.getTime()) + " : " + log;
		List<String> logs = mLogListLiveData.getValue();
		logs.add(0, l);
		mLogListLiveData.postValue(logs);
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		compositeDisposable.clear();
	}
}