package c.local.com.example.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.DLog;
import c.local.com.example.R;
import c.local.com.example.adapter.PokemonAdapter;
import c.local.com.example.data.Pokemon;
import c.local.com.example.databinding.HiltFragmentBinding;
import c.local.com.example.viewmodel.HiltViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HiltFragment extends Fragment {

	public static final String TAG = HiltFragment.class.getSimpleName();

	private HiltFragmentBinding mBinding;

	private HiltViewModel mViewModel;

	private PokemonAdapter mPokemonAdapter;

	public static HiltFragment newInstance() {
		return new HiltFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.hilt_fragment, container, false);
		return mBinding.getRoot();

	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewModel = new ViewModelProvider(this).get(HiltViewModel.class);

		// Adapter 初期化
		mPokemonAdapter = new PokemonAdapter(null);
		mBinding.setIsLoading(true);
		mBinding.pokemonList.setAdapter(mPokemonAdapter);
		mBinding.setHiltViewModel(mViewModel);
		subscribeUi(mViewModel.getPokemonListLiveData());
		// レコメンドのデータを取得
		new Handler(Looper.getMainLooper()).post(() -> mViewModel.fetch());
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


}