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
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import c.local.com.example.data.Item;
import c.local.com.example.data.User;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class ItemFragment extends Fragment {

	private static final String TAG = "★";
	private OnListFragmentInteractionListener mListener;
	private ItemAdapter adapter;
	private List<Item> itemList = new ArrayList<>();
	private HttpAsync task;
	private Disposable disposable;
	private Button asyncTaskButton;
	private Button rxJavaButton;
	private Button subjectButton;
	private Button combineButton;
	private Button stopButton;
	private Handler handler = new Handler();
	private Runnable polling;
	public PublishSubject<Integer> publishSubject = PublishSubject.create();
	private Observable<List<Item>> itemObservable;
	PublishProcessor<Integer> publishProcessor = PublishProcessor.create();
	private Subscription subscription;
	private AtomicInteger pollingCount = new AtomicInteger();


	public ItemFragment() {
	}

	@SuppressLint("CheckResult")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
		asyncTaskButton = toolbar.findViewById(R.id.asyncTask);
		rxJavaButton = toolbar.findViewById(R.id.rxJava);
		subjectButton = toolbar.findViewById(R.id.subject);
		combineButton = toolbar.findViewById(R.id.combine);
		stopButton = toolbar.findViewById(R.id.stop);


		// AsyncTaskでポーリング
		asyncTaskButton.setOnClickListener(v -> {
			v.setActivated(true);
			// 全て停止
			initPolling();
			polling = new Runnable() {
				@Override
				public void run() {
					// AsyncTaskで非同期通信
					task = new HttpAsync();
					task.setListener(createListener());
					task.execute();
					handler.postDelayed(this, 5000);
				}
			};

			handler.postDelayed(polling, 3000);

		});

		// RxJavaでポーリング
		rxJavaButton.setOnClickListener(v -> {
			v.setActivated(true);
			initPolling();

			disposable = Observable.timer(1, TimeUnit.SECONDS)
					.concatMap(o -> {
						Log.d(TAG, " 1: getItem");
						return getItem(5);
					})
					.concatMap(items -> {
						Log.d(TAG, " 2: getUpdate");
						return getUpdate(items);
					})
					.repeatWhen(observable -> {
						Log.d(TAG, "repeatWhen");
						return observable.delay(5, TimeUnit.SECONDS)
								.take(5)
								.doOnNext(data -> {
									Log.d(TAG, "repeatWhen doOnNext");
								})
								.doOnComplete(() -> {
									Log.d(TAG, "repeatWhen doOnComplete");
								});
					})
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(items -> {
						Log.d(TAG, "next");
						render(items);
					}, error -> {
						Log.d(TAG, "error");
					}, () -> {
						Log.d(TAG, "complete");

						rxJavaButton.setActivated(false);

						// Observableのポーリンングを停止
						if (disposable != null) {
							disposable.dispose();
						}

					});

		});

		// Subjectボタン
		subjectButton.setOnClickListener(v -> {
			subjectButton.setActivated(true);

			// PublishProcessor連続で実行してみる
			publishProcessor.onNext(1);
			publishProcessor.onNext(2);
			publishProcessor.onNext(3);
			publishProcessor.onNext(4);
			publishProcessor.onNext(5);

		});

		// Combineボタン
		combineButton.setOnClickListener(v -> {

			Observable.combineLatest(getItem(1), getItem(2), (items1, items2) -> {
				// item1の1件にitem2の2件をマージする
				items1.addAll(items2);
				// 3件を返す
				return items1;
			}).subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(items -> {
								// 一覧へ反映
								render(items);
							},
							e -> {
								Log.d("★", "error" + e);

							}, () -> {
								Log.d("★", "complete");
							}

					);

		});


		// サブジェクトの実装 publishSubjectをonNextされたら、
		itemObservable = publishSubject
				// .throttleLast(3, TimeUnit.SECONDS)
				.flatMap(i -> {
					Log.d(TAG, String.valueOf(i));
					return getItemSync(i);
				}, 1)
				.flatMap(items -> {
					Log.d(TAG, items.toString());
					return getUpdate(items);
				}, 1)
				.replay(1)
				.refCount();

		itemObservable
				.subscribe(item -> {
					render(item);
					this.subscription.request(1);
					subjectButton.setActivated(false);
				}, e -> {
					Log.d("★", "error" + e);
				});


		publishProcessor
				.onBackpressureLatest()
				.subscribe(i -> {
					Log.d(TAG, "PublishProcessor onNext " + i);
					publishSubject.onNext(i);
				}, e -> {
					Log.d(TAG, "PublishProcessor onError");
				}, () -> {
					Log.d(TAG, "PublishProcessor onComplete");
				}, s -> {
					this.subscription = s;
					this.subscription.request(1);
					Log.d(TAG, "PublishProcessor onSubscribe");
				});


		// 全て停止する
		stopButton.setOnClickListener(v -> {
			initPolling();
		});

		adapter = new ItemAdapter(getContext(), itemList, mListener);

	}

	/**
	 * 各非同期処理の初期化
	 */
	public void initPolling() {

		asyncTaskButton.setActivated(false);
		rxJavaButton.setActivated(false);
		subjectButton.setActivated(false);

		// Observableのポーリンングを停止
		if (disposable != null) {
			disposable.dispose();
		}
		// Handlerのポーリンングを停止
		if (handler != null && polling != null) {
			handler.removeCallbacks(polling);
		}
		// Subscriptionを初期値に戻す
		if (this.subscription != null) {
			this.subscription.request(1);
		}
		// ポーリングカウントを0
		pollingCount.set(0);

	}


	/**
	 * APIで一覧を取得（同期）
	 *
	 * @return Observable<String>
	 */
	private Observable<List<Item>> getItem(int count) {

		return Observable.create(subscriber -> {
			// Qiitaの新着10件を取得する
			String json = HttpConnection.getItem(count);
			subscriber.onNext(parseJson(json));
			subscriber.onComplete();
		});
	}


	/**
	 * APIで一覧を取得（非同期）
	 *
	 * @param count 取得件数
	 * @return
	 */
	private Observable<List<Item>> getItemSync(int count) {

		return Observable.create(subscriber -> {

			task = new HttpAsync();
			task.setCount(count);
			task.setListener(result -> {
				subscriber.onNext(parseJson(result));
				subscriber.onComplete();
			});
			task.execute();

		});
	}

	private Observable<List<Item>> getUpdate(List<Item> items) {

		return Observable.create(subscriber -> {

			// ポーリングカウントをインクリメント
			final int count = pollingCount.incrementAndGet();
			// Calendarクラスのオブジェクトを生成する
			Calendar cl = Calendar.getInstance();
			// SimpleDateFormatクラスでフォーマットパターンを設定する
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss");

			String timestamp = sdf.format(cl.getTime());

			List<Item> list = Observable.fromIterable(items)
					.map(item -> {
						item.setTimestamp(timestamp);
						item.setCount(count);
						return item;
					})
					.toList()
					.blockingGet();

			subscriber.onNext(list);
			subscriber.onComplete();

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
		return json -> {

			// JSONをパース
			List<Item> item = parseJson(json);
			// 更新日を更新
			getUpdate(item).subscribe(i -> render(i));

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
