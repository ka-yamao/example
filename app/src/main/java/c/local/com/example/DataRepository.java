package c.local.com.example;


import android.util.Log;

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
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

	private static DataRepository sInstance;

	private final AppDatabase mDatabase;
	private MediatorLiveData<List<ProductEntity>> mObservableProducts;

	private PokeAPIService mApiService;
	private MediatorLiveData<List<Pokemon>> mObservablePokemon;


	// 検索のサブジェクト
	private PublishSubject<String> mPublishSubject;
	// 検索のObservable
	private Observable<PokemonResponse2> searchObservable;
	// 検索実行のプロセル管理
	public PublishProcessor<String> mPublishProcessor;

	private DataRepository(final AppDatabase database, final PokeAPIService apiService) {
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
		mApiService = apiService;
		mPublishSubject = PublishSubject.create();
		mPublishProcessor = PublishProcessor.create();
		mPublishProcessor.onBackpressureLatest().observeOn(Schedulers.from(Executors.newCachedThreadPool()), false, 1).subscribe(s -> {
			System.out.println("hoge subscribe: " + s);
			mPublishSubject.onNext(s);
		});

		searchObservable = mPublishSubject.throttleLast(1000, TimeUnit.MILLISECONDS).concatMap(query -> {
			System.out.println("hoge getPokemon: " + query);
			return mApiService.getPokemons();
		}, 1);
		setSubscribe(searchObservable);
	}

	private void setSubscribe(Observable<PokemonResponse2> observable) {

		observable.subscribeOn(Schedulers.io())
				.map(pokemonResponse -> pokemonResponse.toPokemonList())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> {
							mObservablePokemon.setValue(result);
						},
						error -> {
							Log.e("onError", "getPokemons: " + error.getMessage());
						}, () -> {
							Log.d("onComplete", "Complete");
						});
	}


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

	public LiveData<List<Pokemon>> getPokemons() {
		return mObservablePokemon;
	}

	public void clearPokemon() {
		mObservablePokemon.setValue(new ArrayList<>());
	}

	public boolean isPokemon() {
		return mObservablePokemon.getValue().size() > 0;
	}

	public LiveData<List<Pokemon>> getPokemon(String query) {

		System.out.println("hoge onNext: " + query);
		mPublishSubject.onNext(query);

//		for (int i = 0; i < 10; i++) {
//			System.out.println("hoge onNext: " + i);
//			mPublishProcessor.onNext("" + i);
//		}


//		mApiService.getPokemons()
//				.subscribeOn(Schedulers.io())
//				.map(pokemonResponse -> {
//					return pokemonResponse.toPokemonList();
//				})
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(result -> {
//							mObservablePokemon.setValue(result);
//						},
//						error -> {
//							Log.e("", "getPokemons: " + error.getMessage());
//						});
		return mObservablePokemon;
	}
}
