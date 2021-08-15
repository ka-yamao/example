/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package c.local.com.example.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import c.local.com.example.BasicApp;
import c.local.com.example.DataRepository;
import c.local.com.example.data.Pokemon;
import c.local.com.example.data.PokemonListInfo;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RetrofitViewModel extends AndroidViewModel {

	private final DataRepository mRepository;

	private CompositeDisposable compositeDisposable = new CompositeDisposable();

	// ポケモンリストのデータ
	private MediatorLiveData<List<Pokemon>> mPokemonListLiveData = new MediatorLiveData<>();

	private MediatorLiveData<PokemonListInfo> mPokemonListInfo = new MediatorLiveData<>();

	public RetrofitViewModel(@NonNull Application application,
							 @NonNull SavedStateHandle savedStateHandle) {
		super(application);
		mRepository = ((BasicApp) application).getRepository();
		compositeDisposable.add(mRepository.createObservable(mPokemonListLiveData, mPokemonListInfo));
	}

	public LiveData<List<Pokemon>> getPokemonListLiveData() {
		return mPokemonListLiveData;
	}

	public LiveData<PokemonListInfo> getPokemonListInfoLiveData() {
		return mPokemonListInfo;
	}

	/**
	 * Expose the LiveData Products query so the UI can observe it.
	 */
	public void fetch(boolean isAdd) {
		mRepository.fetch(isAdd ? mPokemonListInfo.getValue(): new PokemonListInfo());
	}

	@Override
	protected void onCleared() {
		super.onCleared();
		compositeDisposable.clear();
	}
}
