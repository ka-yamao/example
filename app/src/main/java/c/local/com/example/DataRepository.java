package c.local.com.example;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MediatorLiveData;
import c.local.com.example.data.Pokemon;
import c.local.com.example.data.PokemonListInfo;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

	public static final String TAG = DataRepository.class.getSimpleName();
	// リポジトリ
	private static DataRepository sInstance;
	// API
	private PokeAPIService mApiService;
	// 検索のサブジェクト
	private PublishSubject<PokemonListInfo> mPublishSubject;
	// 検索実行のプロセル管理
	public PublishProcessor<PokemonListInfo> mPublishProcessor;

	public static DataRepository getInstance(final PokeAPIService api) {
		if (sInstance == null) {
			synchronized (DataRepository.class) {
				if (sInstance == null) {
					sInstance = new DataRepository(api);
				}
			}
		}
		return sInstance;
	}

	private DataRepository(final PokeAPIService apiService) {

		mApiService = apiService;
		mPublishSubject = PublishSubject.create();
		mPublishProcessor = PublishProcessor.create();
		mPublishProcessor.onBackpressureLatest().observeOn(Schedulers.from(Executors.newCachedThreadPool()), false, 1).subscribe(p -> {
			// System.out.println("★ Processor：" + p);
			mPublishSubject.onNext(p);
		});
		RxJavaPlugins.setErrorHandler(e -> {
			DLog.d(TAG, e.toString());
		});
	}

	/**
	 * 検索
	 *
	 * @param pokemonList
	 * @param pokemonListInfo
	 * @return
	 */
	public Disposable createObservable(MediatorLiveData<List<Pokemon>> pokemonList, MediatorLiveData<PokemonListInfo> pokemonListInfo) {
		return mPublishSubject
				.throttleLast(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
				.concatMap(info -> {
					return mApiService.getPokemons(info.toQueryMap());
				}, 1)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
							PokemonListInfo info = new PokemonListInfo(result.count, result.next);
							DLog.d(TAG, "subscribe limit: " + info.limit + ", offset: " + info.offset);
							pokemonListInfo.setValue(info);
							List<Pokemon> list = pokemonList.getValue();
							if (result.previous == null) {
								list = result.toPokemonList();
							} else {
								list.addAll(result.toPokemonList());
							}
							DLog.d(TAG, "list size: " + list.size());
							pokemonList.setValue(list);
						},
						error -> {
							// System.out.println("★ onSuccess");
						}, () -> {
							// System.out.println("★ onComplete");
						});
	}

	public void fetch(PokemonListInfo pokemonListInfo) {
		DLog.d(TAG, "onNext limit: " + pokemonListInfo.limit + ", offset: " + pokemonListInfo.offset);
		mPublishSubject.onNext(pokemonListInfo);
		// Subject だけで大丈夫そう
		// mPublishProcessor.onNext(page);
	}
}
