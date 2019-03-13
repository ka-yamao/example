package c.local.com.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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
	private List<Item> items = new ArrayList<>();
	private HttpAsync task;
	private Disposable disposable;
	private Button asyncTaskButton;
	private Button rxJavaButton;
	private Button stopButton;
	private Observable<String> listObservable;

	public ItemFragment() {
	}

	@SuppressLint("CheckResult")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		asyncTaskButton = toolbar.findViewById(R.id.asyncTask);
		rxJavaButton = toolbar.findViewById(R.id.rxJava);
		stopButton = toolbar.findViewById(R.id.stop);


		// AsyncTaskで取得
		asyncTaskButton.setOnClickListener(v -> {
			if (disposable != null) {
				disposable.dispose();
			}
			// AsyncTaskで非同期通信
			task = new HttpAsync();
			task.setListener(createListener());
			task.execute();

		});

		// RxJavaでポーリングもどき
		rxJavaButton.setOnClickListener(v -> {


			if (listObservable == null) {
				listObservable = getList()
						.repeatWhen(observable -> observable.delay(10, TimeUnit.SECONDS))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
			}

			disposable = listObservable
					.subscribe(json -> {
						// 反映
						render(json);
						Log.d("★", "next");
					}, error -> {
						Log.d("★", "error" + error.toString());
					}, () -> {
						Log.d("★", "complete");
					});


		});


		// RxJavaでポーリングもどき
		stopButton.setOnClickListener(v -> {

			if (disposable != null) {
				disposable.dispose();
			}

		});
		adapter = new ItemAdapter(getContext(), items, mListener);

	}


	/**
	 * APIで一覧を取得
	 *
	 * @return Observable<String>
	 */
	private Observable<String> getList() {

		return Observable.create(subscriber -> {
			// Qiitaの新着10件を取得する
			String json = HttpConnection.getQiita();
			subscriber.onNext(json);

			// タイムスタンプの最後が7だったらポーリングを止める
			String ms = String.valueOf(System.currentTimeMillis());
			Log.d("★", ms);
			if (ms.lastIndexOf("7") != 12) {
				subscriber.onComplete();
			}
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
				render(json);

			}
		};
	}

	private void render(String json) {

		// リストをクリアして、追加
		items.clear();
		items.addAll(parseJson(json));
		// 通知
		adapter.notifyDataSetChanged();

	}

	private List<Item> parseJson(String json) {

		List<Item> items = new ArrayList<>();

		//Calendarクラスのオブジェクトを生成する
		Calendar cl = Calendar.getInstance();

		//SimpleDateFormatクラスでフォーマットパターンを設定する
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");
		String timestamp = sdf.format(cl.getTime());
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
					item.setTimestamp(timestamp);
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
