package c.local.com.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import c.local.com.example.data.Item;
import c.local.com.example.data.User;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ItemFragment extends Fragment {

	private OnListFragmentInteractionListener mListener;
	private ItemAdapter adapter;
	private List<Item> items = new ArrayList<>();
	private HttpAsync task;
	private Disposable disposable;

	public ItemFragment() {
	}

	@SuppressLint("CheckResult")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ItemAdapter(items, mListener);

		// 非同期通信
//		task = new HttpAsync();
//		task.setListener(createListener());
//		task.execute();


//		Single.create(subscriber -> {
//			String json = HttpConnection.getQiita();
//			subscriber.onSuccess(json);
//		}).subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(result -> Log.d("★", result.toString()));


//		List<String> textList = new ArrayList<>();
//		textList.add("hoge");
//		textList.add("fuga");
//		textList.add("piyo");
//		textList.add("nya-");
//		textList.add("nya-");
//		textList.add("nya-");
//		textList.add("nya-");
//		textList.add("nya-");

//		disposable = Observable.interval(5, TimeUnit.SECONDS).take(10)
//				.subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread()).subscribe(json -> {
//
//					Log.d("★", String.valueOf(json));
//				});

//		disposable = getList()
//				.subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread()).subscribe(json -> {
//
//					Log.d("★", String.valueOf(json));
//				});


		disposable = getList()
				.repeatWhen(observable -> observable.delay(3, TimeUnit.SECONDS))
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(json -> {

					Log.d("★", String.valueOf(json));


				}, json -> {

					Log.d("★", String.valueOf(json));


				}, () -> {

					Log.d("★", "");


				});

	}

	Observable<String> getList() {
		return Observable.create(subscriber -> {
			String json = HttpConnection.getQiita();
			subscriber.onNext(json);
			subscriber.onComplete();

			// subscriber.onError(new Exception());

		});
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, container, false);
		// Set the adapter
		if (view instanceof RecyclerView) {
			RecyclerView recyclerView = (RecyclerView) view;
			recyclerView.setAdapter(adapter);
		}


		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		if (disposable != null) {
			disposable.dispose();
		}
	}


	public interface OnListFragmentInteractionListener {
		void onListFragmentInteraction(Item item);
	}

	private HttpAsync.Listener createListener() {
		return new HttpAsync.Listener() {
			@Override
			public void onSuccess(String result) {

				try {
					if (result != null) {

						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {

							Item item = new Item();
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							item.setId(jsonObject.getString("id"));
							item.setTitle(jsonObject.getString("title"));
							item.setBody(jsonObject.getString("rendered_body"));
							JSONObject userObject = jsonObject.getJSONObject("user");

							User user = new User();
							user.setId(userObject.getString("id"));
							user.setName(userObject.getString("name"));
							user.setImage(userObject.getString("name"));
							item.setUser(user);

							items.add(item);
						}
					}

					// 通知
					adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					Log.d("★", e.toString());
				}
			}
		};
	}


//	Observable<String> getUserInfo(Integer authId) {
//				return Observable.create((Subscriber<? super String> subscriber) -> {
//   　　　// 本来以下はWebやDBアクセスなど
//					try {
//						System.out.println(String.format("getUserInfo, ThreadName: %s, Params: %d", Thread.currentThread().getName(), authId));
//						Thread.sleep(2000);
//						subscriber.onNext("Tom");
//						subscriber.onCompleted();
//					} catch (Exception e) {
//						subscriber.onError(e);
//					}
//				}).subscribeOn(Schedulers.io());
//			}
//
//			Observable<List<String>> getUserItems(Integer authId) {
//				return Observable.create((Subscriber<? super List<String>> subscriber) -> {
//   　　　// 本来以下はWebやDBアクセスなど
//					try {
//						System.out.println(String.format("getUserItems, ThreadName: %s, Params: %d", Thread.currentThread().getName(), authId));
//						Thread.sleep(3000);
//						subscriber.onNext(asList("Apple", "Banana"));
//						subscriber.onCompleted();
//					} catch (Exception e) {
//						subscriber.onError(e);
//					}
//		}).subscribeOn(Schedulers.io());
//	}
}
