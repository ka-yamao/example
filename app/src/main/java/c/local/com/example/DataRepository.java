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
			mPublishSubject.onNext(p);
		});
		RxJavaPlugins.setErrorHandler(e -> {
			DLog.d(TAG, e.toString());
		});
	}

	/**
	 * ポケモンのリストを取得
	 *
	 * @param pokemonList
	 * @param pokemonListInfo
	 * @return
	 */
	public Disposable createObservableSubscribe(MediatorLiveData<List<Pokemon>> pokemonList, MediatorLiveData<PokemonListInfo> pokemonListInfo) {
		return mPublishSubject
				// 1000ミリ秒間の間で最後の値を流す
				.throttleLast(1000, TimeUnit.MILLISECONDS)
				// 入出力用のスレッド
				.subscribeOn(Schedulers.io())
				// 非同期の解決順で並び、APIで Observable<PokemonResponse> を取得
				.concatMap(info -> mApiService.getPokemons(info.toQueryMap()), 1)
				// メインスレッド
				.observeOn(AndroidSchedulers.mainThread())
				// サブスクライブ リストを生成して、LiveData へ設定
				.subscribe(result -> {
							// ポケモンのリスト作成
							List<Pokemon> list = pokemonList.getValue();
							if (result.previous == null) {
								list = result.toPokemonList();
							} else {
								list.addAll(result.toPokemonList());
							}
							// ポケモンリストを LiveDataへ設定 ※メインスレッドなので setValue
							pokemonList.setValue(list);
							// リストのページ情報
							PokemonListInfo info = new PokemonListInfo(result.count, result.next);
							// リストのページ情報を LiveDataへ設定 ※メインスレッドなので setValue
							pokemonListInfo.setValue(info);
						},
						error -> {
							// ※省略
						}, () -> {
							// ※省略
						});
	}

	public void fetch(PokemonListInfo pokemonListInfo) {
		// onNext で ページ情報を流す
		mPublishSubject.onNext(pokemonListInfo);
		// Subject だけで大丈夫そう
		// mPublishProcessor.onNext(page);
	}
}
