package c.local.com.example;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import c.local.com.example.data.Pokemon;
import c.local.com.example.data.PokemonListInfo;
import c.local.com.example.db.AppDatabase;
import c.local.com.example.db.entity.CommentEntity;
import c.local.com.example.db.entity.ProductEntity;
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
	// DB
	private final AppDatabase mDatabase;
	// API
	private PokeAPIService mApiService;
	// 検索のサブジェクト
	private PublishSubject<PokemonListInfo> mPublishSubject;
	// 検索実行のプロセル管理
	public PublishProcessor<PokemonListInfo> mPublishProcessor;

	private MediatorLiveData<List<ProductEntity>> mObservableProducts;

	public static DataRepository getInstance(final AppDatabase database,
											 final PokeAPIService api) {
		if (sInstance == null) {
			synchronized (DataRepository.class) {
				if (sInstance == null) {
					sInstance = new DataRepository(database, api);
				}
			}
		}
		return sInstance;
	}

	private DataRepository(final AppDatabase database, final PokeAPIService apiService) {
		// Product
		mDatabase = database;
		mObservableProducts = new MediatorLiveData<>();
		mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
				productEntities -> {
					if (mDatabase.getDatabaseCreated().getValue() != null) {
						mObservableProducts.postValue(productEntities);
					}
				});

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
							DLog.d(TAG,"subscribe limit: " + info.limit + ", offset: " + info.offset);
							pokemonListInfo.setValue(info);
							List<Pokemon> list = pokemonList.getValue();
							if (result.previous == null) {
								list = result.toPokemonList();
							} else {
								list.addAll(result.toPokemonList());
							}
							DLog.d(TAG,"list size: " + list.size());
							pokemonList.setValue(list);
					},
						error -> {
							// System.out.println("★ onSuccess");
						}, () -> {
							// System.out.println("★ onComplete");
						});
	}

	public void fetch(PokemonListInfo pokemonListInfo) {
		DLog.d(TAG,"onNext limit: " + pokemonListInfo.limit + ", offset: " + pokemonListInfo.offset);
		mPublishSubject.onNext(pokemonListInfo);
		// mPublishProcessor.onNext(page);
	}

	/**
	 * Get the list of products from the database and get notified when the data changes.
	 */
	public LiveData<List<ProductEntity>> getProducts() {
		return mObservableProducts;
	}

	public LiveData<ProductEntity> loadProduct(final int productId) {
		return mDatabase.productDao().loadProduct(productId);
	}

	public LiveData<List<CommentEntity>> loadComments(final int productId) {
		return mDatabase.commentDao().loadComments(productId);
	}

	public LiveData<List<ProductEntity>> searchProducts(String query) {
		return mDatabase.productDao().searchAllProducts(query);
	}
}
