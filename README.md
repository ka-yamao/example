

### 参考サイト
* https://qiita.com/toastkidjp/items/581e89559f05952fbdb6
* https://qiita.com/sho5nn/items/f63ebd7ccc0c86d98e4b
* https://akira-watson.com/android/toast.html
* https://qiita.com/takahirom/items/f3e576e91b219c7239e7
* https://qiita.com/toastkidjp/items/9d8487d43ed8211e8cdb


## RxJavaメモ
```

		// AsyncTaskで非同期通信
		task = new HttpAsync();
		task.setListener(createListener());
		task.execute();



		Single.create(subscriber -> {
			String json = HttpConnection.getQiita();
			subscriber.onSuccess(json);
		}).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(result -> Log.d("★", result.toString()));


		List<String> textList = new ArrayList<>();
		textList.add("hoge");
		textList.add("fuga");
		textList.add("piyo");


		disposable = Observable.interval(5, TimeUnit.SECONDS).take(10)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread()).subscribe(json -> {

					Log.d("★", String.valueOf(json));
				});

		disposable = getList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread()).subscribe(json -> {

					Log.d("★", String.valueOf(json));
				});
```