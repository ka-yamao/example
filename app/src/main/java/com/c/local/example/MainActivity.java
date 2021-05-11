package com.c.local.example;

import android.os.Bundle;
import android.util.Log;

import com.c.local.example.ui.main.MainFragment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("LifecycleLesson", "Activity::onCreate");
		setContentView(R.layout.main_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, MainFragment.newInstance())
					.commitNow();
		}
		

		getLifecycle().addObserver(new LifecycleSample());

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("LifecycleLesson", "Activity::onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("LifecycleLesson", "Activity::onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("LifecycleLesson", "Activity::onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("LifecycleLesson", "Activity::onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("LifecycleLesson", "Activity::onResume");
	}
}