package c.local.com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.R;
import c.local.com.example.databinding.RxJavaFragmentBinding;
import c.local.com.example.viewmodel.RxJavaViewModel;

public class RxJavaFragment extends Fragment {

	private RxJavaViewModel mViewModel;
	private RxJavaFragmentBinding mBinding;
	private ArrayAdapter<String> mAdapter;

	public static RxJavaFragment newInstance() {
		return new RxJavaFragment();
	}

	public interface  ButtonClickCallback {
		void onClick(int index);
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
						 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.rx_java_fragment, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ViewModel
		mViewModel = new ViewModelProvider(this).get(RxJavaViewModel.class);
		// Adapter 初期化
		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
		mBinding.list.setAdapter(mAdapter);
		mViewModel.getLogListLiveData().observe(getViewLifecycleOwner(), logList -> {
			mAdapter.clear();
			mAdapter.addAll(logList);
			mBinding.executePendingBindings();
		});
		// ボタンのクリックリスナー
		mBinding.setCallback(index -> mViewModel.fetch(index));
	}

	@Override
	public void onDestroyView() {
		mBinding = null;
		mAdapter = null;
		super.onDestroyView();
	}

}