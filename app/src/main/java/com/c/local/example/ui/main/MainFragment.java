package com.c.local.example.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c.local.example.databinding.MainFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MainFragment extends Fragment {

	private MainViewModel mViewModel;
	private MainFragmentBinding binding;


	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {

		binding = MainFragmentBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		return view;
		// return inflater.inflate(R.layout.main_fragment, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

		binding.message.setText("Click");
		binding.button.setOnClickListener(v -> {
			binding.text.setText("time: " + System.currentTimeMillis());
		});


		// binding.setVm(mViewModel);
//		binding.button.setOnClickListener(new View.OnClickListener() {
//        viewModel.userClicked()
//		});


//		ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.main_fragment);
//		binding.vm = mViewModel;

		// TODO: Use the ViewModel
	}

}