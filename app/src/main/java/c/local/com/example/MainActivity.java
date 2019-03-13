package c.local.com.example;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import c.local.com.example.data.Item;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


		/*



		 */


		// QiitaのAPI：https://qiita.com/api/v2/items?page=1&per_page=20


//		final ExecutorService executor = Executors.newSingleThreadExecutor();
//		executor.execute(() -> test());
//		executor.shutdown();
//
//		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10);
//		pool.scheduleWithFixedDelay(new Thread(() -> test()), 1000,3000, TimeUnit.MILLISECONDS);


//		Completable.fromAction(() -> test())
//				.subscribeOn(Schedulers.newThread())
//				.subscribe();


//		Single.create(emitter -> search()).subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(bbb -> {
//					Log.d("★★", bbb.toString());
//
//				});

		ft.replace(R.id.content, new ItemFragment());
		ft.commit();
	}

	@Override
	public void onListFragmentInteraction(Item item) {

	}

	public void test() {
		Log.d("★", "test");
	}

	public String search() {
		return "aaa";
	}
}
