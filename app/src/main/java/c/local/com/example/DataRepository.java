package c.local.com.example;


import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import c.local.com.example.data.Pokemon;
import c.local.com.example.db.AppDatabase;
import c.local.com.example.db.entity.CommentEntity;
import c.local.com.example.db.entity.ProductEntity;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

	private static DataRepository sInstance;

	private final AppDatabase mDatabase;
	private MediatorLiveData<List<ProductEntity>> mObservableProducts;

	private PokeAPIService api;
	private MediatorLiveData<List<Pokemon>> mObservablePokemon;

	private DataRepository(final AppDatabase database) {
		mDatabase = database;
		mObservableProducts = new MediatorLiveData<>();
		mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
				productEntities -> {
					if (mDatabase.getDatabaseCreated().getValue() != null) {
						mObservableProducts.postValue(productEntities);
					}
				});

		mObservablePokemon = new MediatorLiveData<>();
	}


	public static DataRepository getInstance(final AppDatabase database) {
		if (sInstance == null) {
			synchronized (DataRepository.class) {
				if (sInstance == null) {
					sInstance = new DataRepository(database);
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

	public LiveData<List<Pokemon>> getPokemon(String query) {
		List list = new ArrayList<Pokemon>();
		list.add(new Pokemon(25, "pikachu", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"));

		// mObservablePokemon.postValue(list);
		return mObservablePokemon;
	}
}
