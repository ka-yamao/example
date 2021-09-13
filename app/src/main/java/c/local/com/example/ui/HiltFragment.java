package c.local.com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.R;
import c.local.com.example.databinding.HiltFragmentBinding;
import c.local.com.example.viewmodel.HiltViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HiltFragment extends Fragment {

	public static final String TAG = HiltFragment.class.getSimpleName();

	private HiltFragmentBinding mBinding;

	private HiltViewModel mViewModel;

	public static HiltFragment newInstance() {
		return new HiltFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mBinding = DataBindingUtil.inflate(inflater, R.layout.hilt_fragment, container, false);

		mViewModel = new ViewModelProvider(this).get(HiltViewModel.class);
		mBinding.setLifecycleOwner(this);
		mBinding.setHiltViewModel(mViewModel);

		return mBinding.getRoot();

	}


}