package c.local.com.example;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import c.local.com.example.data.Pokemon;
import c.local.com.example.db.AppDatabase;
import c.local.com.example.db.entity.CommentEntity;
import c.local.com.example.db.entity.ProductEntity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
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
	// ポケモンリストのデータ
	private MediatorLiveData<List<Pokemon>> mObservablePokemon;
	// ページ
	private MediatorLiveData<Integer> mPage;
	// 検索のサブジェクト
	private PublishSubject<Integer> mPublishSubject;
	// 検索のObservable
	private Observable<PokemonResponse2> searchObservable;
	// 検索実行のプロセル管理
	public PublishProcessor<Integer> mPublishProcessor;

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

		// Pokemon
		mObservablePokemon = new MediatorLiveData<>();
		mPage = new MediatorLiveData<>();
		mApiService = apiService;
		mPublishSubject = PublishSubject.create();
		mPublishProcessor = PublishProcessor.create();
		mPublishProcessor.onBackpressureLatest().observeOn(Schedulers.from(Executors.newCachedThreadPool()), false, 1).subscribe(p -> {
			// System.out.println("★ Processor：" + p);
			mPublishSubject.onNext(p);
		});
		searchObservable = mPublishSubject
				.throttleLast(5000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
				.concatMap(p -> mApiService.getPokemons(), 1);

		searchObservable
				.map(pokemonResponse -> pokemonResponse.toPokemonList())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
							mObservablePokemon.setValue(result);
						},
						error -> {
							// System.out.println("★ onSuccess");
						}, () -> {
							// System.out.println("★ onComplete");
						});

		RxJavaPlugins.setErrorHandler(e -> {
			Log.d(TAG, e.toString());
		});

	}

	public void fetchPokemonList(int page) {
		mObservablePokemon.setValue(new ArrayList<>());
		mPublishSubject.onNext(page);
		// mPublishProcessor.onNext(page);
	}

	public LiveData<List<Pokemon>> getPokemonList() {
		return mObservablePokemon;
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


	public void clearPokemon() {
		mObservablePokemon.setValue(new ArrayList<>());
	}

	public boolean isPokemon() {
		return mObservablePokemon.getValue().size() > 0;
	}
}
