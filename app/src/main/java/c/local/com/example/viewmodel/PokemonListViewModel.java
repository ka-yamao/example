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
import android.text.TextUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import c.local.com.example.BasicApp;
import c.local.com.example.DataRepository;
import c.local.com.example.data.Pokemon;

public class PokemonListViewModel extends AndroidViewModel {
	private static final String QUERY_KEY = "Poke";

	private final SavedStateHandle mSavedStateHandler;
	private final DataRepository mRepository;
	private final LiveData<List<Pokemon>> mPokemons;

	public PokemonListViewModel(@NonNull Application application,
								@NonNull SavedStateHandle savedStateHandle) {
		super(application);
		mSavedStateHandler = savedStateHandle;

		mRepository = ((BasicApp) application).getRepository();

		mPokemons = Transformations.switchMap(
				savedStateHandle.getLiveData("QUERY", null),
				(Function<CharSequence, LiveData<List<Pokemon>>>) query -> {
					if (TextUtils.isEmpty(query)) {
						return mRepository.getPokemons();
					}
					return mRepository.getPokemon(String.valueOf(query));
				});
	}

	/**
	 * Expose the LiveData Products query so the UI can observe it.
	 */
	public LiveData<List<Pokemon>> getPokemon(String query) {
		return mRepository.getPokemon(query);
	}

	public LiveData<List<Pokemon>> getPokemons() {
		return mRepository.getPokemons();
	}
}
