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
import androidx.lifecycle.SavedStateHandle;
import c.local.com.example.BasicApp;
import c.local.com.example.DataRepository;
import c.local.com.example.data.Pokemon;

public class RetrofitViewModel extends AndroidViewModel {

	private final DataRepository mRepository;
	private boolean isLoading;


	public RetrofitViewModel(@NonNull Application application,
							 @NonNull SavedStateHandle savedStateHandle) {
		super(application);
		mRepository = ((BasicApp) application).getRepository();
	}


	public LiveData<List<Pokemon>> getPokemonList() {
		return mRepository.getPokemonList();
	}

	/**
	 * Expose the LiveData Products query so the UI can observe it.
	 */
	public LiveData<List<Pokemon>> fetchPokemon(int page) {
		return mRepository.fetchPokemonList(page);
	}


}
