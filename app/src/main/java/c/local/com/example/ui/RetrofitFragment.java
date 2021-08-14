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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import c.local.com.example.R;
import c.local.com.example.adapter.PokemonAdapter;
import c.local.com.example.data.Pokemon;
import c.local.com.example.databinding.RetrofitFragmentBinding;
import c.local.com.example.viewmodel.RetrofitViewModel;

public class RetrofitFragment extends Fragment {

	public static final String TAG = RetrofitFragment.class.getSimpleName();

	private PokemonAdapter mPokemonAdapter;

	private RetrofitFragmentBinding mBinding;

	private  RetrofitViewModel mViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.retrofit_fragment, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ViewModel
		mViewModel = new ViewModelProvider(this).get(RetrofitViewModel.class);
		// Adapter 初期化
		mPokemonAdapter = new PokemonAdapter(mPokemonClickCallback);
		mBinding.setProgress(true);
		mBinding.pokemonList.setAdapter(mPokemonAdapter);
		mBinding.setRetrofitViewModel(mViewModel);
		subscribeUi(mViewModel.getPokemonList());
		mBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mViewModel.fetchPokemon(1);
			}
		});
		mBinding.pokemonList.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
			System.out.println("test");
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void subscribeUi(LiveData<List<Pokemon>> liveData) {
		// Update the list when the data changes
		liveData.observe(getViewLifecycleOwner(), pokemons -> {
			if (pokemons != null) {
				mBinding.setProgress(false);
				mPokemonAdapter.setPokemonList(pokemons);
			} else {
				mBinding.setProgress(true);
			}
			mBinding.executePendingBindings();
			mBinding.refresh.setRefreshing(false);
		});
		// １ページ名を取得
		mViewModel.fetchPokemon(1);
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
