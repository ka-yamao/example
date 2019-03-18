package c.local.com.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	private List<Item> itemList = new ArrayList<>();
	private HttpAsync task;
	private Disposable disposable;
	private Button asyncTaskButton;
	private Button rxJavaButton;
	private Button stopButton;
	private Handler handler = new Handler();
	private Runnable polling;

	public ItemFragment() {
	}

	@SuppressLint("CheckResult")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
		asyncTaskButton = toolbar.findViewById(R.id.asyncTask);
		rxJavaButton = toolbar.findViewById(R.id.rxJava);
		stopButton = toolbar.findViewById(R.id.stop);


		// AsyncTaskでポーリングもどき
		asyncTaskButton.setOnClickListener(v -> {

			v.setActivated(true);

			if (disposable != null) {
				disposable.dispose();
			}

			polling = new Runnable() {
				@Override
				public void run() {
					// AsyncTaskで非同期通信
					task = new HttpAsync();
					task.setListener(createListener());
					task.execute();
					handler.postDelayed(this, 10000);
				}
			};

			handler.postDelayed(polling, 3000);


		});

		// RxJavaでポーリングもどき
		rxJavaButton.setOnClickListener(v -> {

			v.setActivated(true);

			if (handler != null && polling != null) {
				handler.removeCallbacks(polling);
			}

			disposable = getItem()
					.repeatWhen(observable -> observable.delay(10, TimeUnit.SECONDS))
					.flatMap(items -> getUpdate(items))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(items -> {
								render(items);
								Log.d("★", "next");
							}, error -> Log.d("★", "error")
							, () -> Log.d("★", "complete")
					);
		});


		// RxJavaでポーリングもどき
		stopButton.setOnClickListener(v -> {

			asyncTaskButton.setActivated(false);
			rxJavaButton.setActivated(false);

			if (disposable != null) {
				disposable.dispose();
			}

			if (handler != null && polling != null) {
				handler.removeCallbacks(polling);
			}

		});
		adapter = new ItemAdapter(getContext(), itemList, mListener);

	}


	/**
	 * APIで一覧を取得
	 *
	 * @return Observable<String>
	 */
	private Observable<List<Item>> getItem() {

		return Observable.create(subscriber -> {
			// Qiitaの新着10件を取得する
			String json = HttpConnection.getItem();
			subscriber.onNext(parseJson(json));
			// タイムスタンプの最後が7だったらポーリングを止める
			String ms = String.valueOf(System.currentTimeMillis());
			Log.d("★", ms);
			if (ms.lastIndexOf("7") != 12) {
				subscriber.onComplete();
			}
		});
	}

	private Observable<List<Item>> getUpdate(List<Item> items) {


		return Observable.create(subscriber -> {
			// Calendarクラスのオブジェクトを生成する
			Calendar cl = Calendar.getInstance();
			// SimpleDateFormatクラスでフォーマットパターンを設定する
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");

			String timestamp = sdf.format(cl.getTime());

			Observable.fromIterable(items)
					.map(item -> {
						item.setTimestamp(timestamp);
						return item;
					})
					.toList()
					.subscribe(s -> {
						subscriber.onNext(s);
						subscriber.onComplete();
					});

		});


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_item_list, container, false);
		// Set the adapter
		if (view instanceof RecyclerView) {
			RecyclerView recyclerView = (RecyclerView) view;
			// 区切り線設定
			DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
					new LinearLayoutManager(getActivity()).getOrientation());
			recyclerView.addItemDecoration(dividerItemDecoration);
			// Adapterを設定
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
			public void onSuccess(String json) {

				// リストをクリアして、追加
				render(parseJson(json));

			}
		};
	}

	private void render(List<Item> items) {


		// リストをクリアして、追加
		itemList.clear();
		itemList.addAll(items);
		// 通知
		adapter.notifyDataSetChanged();

	}

	/**
	 * @param json
	 * @return
	 */
	private List<Item> parseJson(String json) {

		List<Item> items = new ArrayList<>();


		try {
			if (json != null) {
				JSONArray jsonArray = new JSONArray(json);
				for (int i = 0; i < jsonArray.length(); i++) {
					// アイテム
					Item item = new Item();
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					item.setId(jsonObject.getString("id"));
					item.setTitle(jsonObject.getString("title"));
					item.setUdate(jsonObject.getString("updated_at").substring(0, 10));
					item.setBody(jsonObject.getString("rendered_body"));
					JSONObject userObject = jsonObject.getJSONObject("user");
					// ユーザ
					User user = new User();
					user.setId(userObject.getString("id"));
					user.setName(userObject.getString("name"));
					user.setImage(userObject.getString("profile_image_url"));
					item.setUser(user);
					items.add(item);
				}
			}

		} catch (JSONException e) {
			Log.d("★", e.toString());
		}
		return items;
	}


}
