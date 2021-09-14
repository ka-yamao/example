package c.local.com.example.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import c.local.com.example.model.RetrofitException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;


public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

	// オリジナルの RxJava3 アダプター
	private final RxJava3CallAdapterFactory original;

	private RxErrorHandlingCallAdapterFactory() {
		original = RxJava3CallAdapterFactory.create();
	}

	@Nullable
	@Override
	public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
		// 下記のカスタムアダプタを利用、第二引数でオリジナルの CallAdapter アダプタを取得して渡す
		return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
	}

	public static CallAdapter.Factory create() {
		return new RxErrorHandlingCallAdapterFactory();
	}

	private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
		private final Retrofit retrofit;
		private final CallAdapter<R, Object> wrapped;

		public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<R, Object> wrapped) {
			this.retrofit = retrofit;
			this.wrapped = wrapped;
		}

		@Override
		public Type responseType() {
			return wrapped.responseType();
		}

		@Override
		public Object adapt(Call<R> call) {
			// RxJava3の CallAdapter で適用
			Object result = wrapped.adapt(call);
			// エラーの場合は、独自の RetrofitException を返すよう実装
			if (result instanceof Single) {
				return ((Single) result).onErrorResumeNext((Function<Throwable, SingleSource>) throwable -> Single.error(RetrofitException.asRetrofitException(throwable)));
			}
			if (result instanceof Observable) {
				return ((Observable) result).onErrorResumeNext((Function<Throwable, ObservableSource>) throwable -> Observable.error(RetrofitException.asRetrofitException(throwable)));
			}
			if (result instanceof Completable) {
				return ((Completable) result).onErrorResumeNext(throwable -> Completable.error(RetrofitException.asRetrofitException(throwable)));
			}
			return result;
		}
	}
}