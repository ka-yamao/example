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

package c.local.com.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.R;
import c.local.com.example.adapter.PokemonAdapter;
import c.local.com.example.data.Pokemon;
import c.local.com.example.databinding.PokemonListFragmentBinding;
import c.local.com.example.viewmodel.PokemonListViewModel;

public class PokemonListFragment extends Fragment {

	public static final String TAG = "ProductListFragment";

	private PokemonAdapter mPokemonAdapter;

	private PokemonListFragmentBinding mBinding;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.pokemon_list_fragment, container, false);

		mPokemonAdapter = new PokemonAdapter(mPokemonClickCallback);
		mBinding.pokemonList.setAdapter(mPokemonAdapter);

		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final PokemonListViewModel viewModel =
				new ViewModelProvider(this).get(PokemonListViewModel.class);

		mBinding.pokemonSearchBtn.setOnClickListener(v -> {
			String query = String.valueOf(mBinding.pokemonSearchBox.getText());
			viewModel.fetchPokemon(1);
		});
		mBinding.pokemonSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				viewModel.fetchPokemon(1);
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mBinding.setPokemonListViewModel(viewModel);
		subscribeUi(viewModel.getPokemonList());
	}

	private void subscribeUi(LiveData<List<Pokemon>> liveData) {
		// Update the list when the data changes
		liveData.observe(getViewLifecycleOwner(), pokemons -> {
			if (pokemons != null) {
				mBinding.setIsLoading(false);
				mPokemonAdapter.setPokemonList(pokemons);
			} else {
				mBinding.setIsLoading(true);
			}
			// espresso does not know how to wait for data binding's loop so we execute changes
			// sync.
			mBinding.executePendingBindings();
		});
	}

	@Override
	public void onDestroyView() {
		mBinding = null;
		mPokemonAdapter = null;
		super.onDestroyView();
	}

	private final PokemonClickCallback mPokemonClickCallback = pokemon -> {
		if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
			((MainActivity) requireActivity()).showPokemon(pokemon);
		}
	};
}
