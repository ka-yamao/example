package c.local.com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.R;
import c.local.com.example.adapter.PokemonAdapter;
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
		mBinding.setHiltViewModel(mViewModel);

	}

}