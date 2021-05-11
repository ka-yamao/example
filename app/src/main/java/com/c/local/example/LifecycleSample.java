package com.c.local.example;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LifecycleSample implements LifecycleObserver {

	@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
	void handleCreate() {
		Log.d("LifecycleLesson", "ON_CREATE");
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	void handleDestroy() {
		Log.d("LifecycleLesson", "ON_DESTROY");
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	void handlePause() {
		Log.d("LifecycleLesson", "ON_PAUSE");
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	void handleResume() {
		Log.d("LifecycleLesson", "ON_RESUME");
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	void handleStart() {
		Log.d("LifecycleLesson", "ON_START");
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	void handleStop() {
		Log.d("LifecycleLesson", "ON_STOP");
	}
}

