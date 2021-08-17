package c.local.com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import c.local.com.example.BasicApp
import c.local.com.example.DataRepository
import c.local.com.example.PokeAPIService
import c.local.com.example.data.Pokemon
import c.local.com.example.data.PokemonListInfo
import c.local.com.example.model.PokemonResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class KotlinViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable();

    private var mRepository: DataRepository = (application as BasicApp).repository

    // ポケモンリストのデータ
    private val mPokemonListLiveData = MediatorLiveData<List<Pokemon>>()

    // LiveData
    private val mPokemonListInfoLiveData = MediatorLiveData<PokemonListInfo>()

    private var mPublishSubject: PublishSubject<PokemonListInfo>? = null

    private val mApiService: PokeAPIService? = (application as BasicApp).api

    init {
        var dispose = createObservable()
        compositeDisposable.add(dispose);
    }

    fun createObservable(): Disposable? {
        mPublishSubject = PublishSubject.create()
        return mPublishSubject!!
            .throttleLast(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
            .concatMap<PokemonResponse>({ info: PokemonListInfo ->
                mApiService!!.getPokemons(info.toQueryMap())
            }, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result: PokemonResponse? ->
                    mPokemonListLiveData.value = result!!.toPokemonList()
                },
                { error: Throwable -> }
            ) { }
    }

    fun getPokemonListLiveData(): LiveData<List<Pokemon>> {
        return mPokemonListLiveData
    }

    fun getPokemonListInfoLiveData(): LiveData<PokemonListInfo?>? {
        return mPokemonListInfoLiveData
    }


    /**
     * ポケモンリストの取得、追加読み込み
     */
    fun fetch(isAdd: Boolean) {
        mPublishSubject!!.onNext(PokemonListInfo())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}