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
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import c.local.com.example.DLog;
import c.local.com.example.R;
import c.local.com.example.adapter.PokemonAdapter;
import c.local.com.example.data.Pokemon;
import c.local.com.example.databinding.RetrofitFragmentBinding;
import c.local.com.example.viewmodel.RetrofitViewModel;

public class RetrofitFragment extends Fragment {

	public static final String TAG = RetrofitFragment.class.getSimpleName();

	public static RetrofitFragment newInstance() {
		return new RetrofitFragment();
	}

	private PokemonAdapter mPokemonAdapter;

	private RetrofitFragmentBinding mBinding;

	private RetrofitViewModel mViewModel;

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
		mBinding.setIsLoading(true);
		mBinding.pokemonList.setAdapter(mPokemonAdapter);
		mBinding.setRetrofitViewModel(mViewModel);
		subscribeUi(mViewModel.getPokemonListLiveData());
		mBinding.refresh.setOnRefreshListener(() -> {
			mBinding.setIsLoading(true);
			mViewModel.fetch(false);
		});

		mBinding.pokemonList.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) mBinding.pokemonList.getLayoutManager()) {
			@Override
			public void onLoadMore(int page) {
				mViewModel.fetch(true);
			}
		});
		// レコメンドのデータを取得
		new Handler(Looper.getMainLooper()).post(() -> mViewModel.fetch(false));
	}

	private void subscribeUi(LiveData<List<Pokemon>> liveData) {
		// Update the list when the data changes
		liveData.observe(getViewLifecycleOwner(), pokemonList -> {
			if (pokemonList != null) {
				mBinding.setIsLoading(false);
				DLog.d(TAG, "list size: " + pokemonList.size());
				mPokemonAdapter.setPokemonList(pokemonList);
			} else {
				mBinding.setIsLoading(true);
			}
			mBinding.executePendingBindings();
			mBinding.refresh.setRefreshing(false);
		});
	}

	@Override
	public void onDestroyView() {
		mBinding = null;
		mPokemonAdapter = null;
		super.onDestroyView();
	}

	private final PokemonClickCallback mPokemonClickCallback = pokemon -> Toast.makeText(getContext(), pokemon.getName(), Toast.LENGTH_SHORT).show();
}
