package c.local.com.example.di;


import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import c.local.com.example.DLog;
import c.local.com.example.HttpBinService;
import c.local.com.example.adapter.ErrorHandlingAdapter;
import c.local.com.example.model.Ip;
import c.local.com.example.model.RetrofitException;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import retrofit2.Response;

/**
 * Created by Abhinav Singh on 17,June,2020
 */
public class Repository {

	public static final String TAG = Repository.class.getSimpleName();
	// API
	private HttpBinService mHttpBinService;

	// 検索のサブジェクト
	private PublishSubject<Integer> mPublishSubject;
	// 検索実行のプロセル管理
	public PublishProcessor<Integer> mPublishProcessor;

	@Inject
	public Repository(HttpBinService apiService) {
		mHttpBinService = apiService;
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
	 * @param liveData MutableLiveData<StateData<String>>
	 * @return
	 */
	public Disposable createObservableSubscribe(MutableLiveData<String> liveData) {
		return mPublishSubject
				// 1000ミリ秒間の間で最後の値を流す
				.throttleLast(1000, TimeUnit.MILLISECONDS)
				// 入出力用のスレッド
				.subscribeOn(Schedulers.io())
				// 非同期の解決順で並び
				.concatMap(num -> wrapObservable(num), 1)
				// メインスレッド
				.observeOn(AndroidSchedulers.mainThread())
				// サブスクライブ リストを生成して、LiveData へ設定
				.subscribe(result -> {
					liveData.setValue(result);
				});
	}

	public void fetch(int c) {
		mPublishSubject.onNext(c);
	}

	private Observable<String> wrapObservable(Integer i) {

		return Observable.create(e -> {

			Observable<Ip> observable;
			if (i == 200) {
				observable = mHttpBinService.getOK200("ip");
			} else if (i == 400 || i == 500) {
				observable = mHttpBinService.getErr("status", String.valueOf(i));
			} else if (i == 0) {
				observable = mHttpBinService.getTimeout();
			} else if (i == 1) {
				observable = mHttpBinService.getErr("status", String.valueOf(i));
			} else if (i == 2) {
				observable = mHttpBinService.getErr("stat", String.valueOf(i));
			} else {
				observable = mHttpBinService.getOK200("ip");
			}
			observable.subscribe(result -> {
				e.onNext(result.origin);
				e.onComplete();
			}, error -> {
				RetrofitException.Kind s = ((RetrofitException) error).getKind();
				e.onNext(String.valueOf(s));
				e.onComplete();
			});
		});
	}

	private Observable<String> wrapObservableResult(Integer i) {
		return Observable.create(e -> {
			ErrorHandlingAdapter.MyCall<Ip> ip;
			if (i == 200) {
				ip = mHttpBinService.getOK("ip");

			} else if (i == 400 || i == 500) {
				ip = mHttpBinService.getError("status", String.valueOf(i));
			} else if (i == 0) {
				ip = mHttpBinService.getDelay();
			} else if (i == 1) {
				ip = mHttpBinService.getError("status", String.valueOf(i));
			} else if (i == 2) {
				ip = mHttpBinService.getError("stat", String.valueOf(i));
			} else {
				ip = mHttpBinService.getOK("ip");
			}
			ip.enqueue(new ErrorHandlingAdapter.MyCallback<>() {
				@Override
				public void success(Response<Ip> response) {
					e.onNext(response.body().origin);
					e.onComplete();
				}

				@Override
				public void unauthenticated(Response<?> response) {
					e.onNext("unauthenticated");
					e.onComplete();

				}

				@Override
				public void clientError(Response<?> response) {
					e.onNext("clientError");
					e.onComplete();

				}

				@Override
				public void serverError(Response<?> response) {
					e.onNext("serverError");
					e.onComplete();
				}

				@Override
				public void networkError(IOException ex) {
					e.onNext("networkError : " + ex.getMessage());
					e.onComplete();
				}

				@Override
				public void unexpectedError(Throwable t) {
					e.onNext("unexpectedError : " + t.getMessage());
					e.onComplete();
				}
			});

		});
	}
}
