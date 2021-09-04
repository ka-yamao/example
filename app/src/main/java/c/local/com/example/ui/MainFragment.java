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
import c.local.com.example.databinding.MainFragmentBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

	public static final String TAG = MainFragment.class.getSimpleName();

	// インスタンス
	public static MainFragment newInstance() {
		return new MainFragment();
	}

	// ViewModel
	private MainViewModel mViewModel;
	// データバインディング
	private MainFragmentBinding mBinding;

	static final int PAGE_COUNT = 1;
	// タブのタイトル
	String[] titles = {"Hilt"};

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
		mBinding.setLifecycleOwner(this);
		return mBinding.getRoot();
	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ViewModel
		mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
	}
}

