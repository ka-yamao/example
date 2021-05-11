package com.c.local.example.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c.local.example.databinding.MainFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

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
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// ビュー バインディング
		binding.setData(new Data());
		binding.button.setOnClickListener(v -> {
			binding.text.setText("time: " + System.currentTimeMillis());
		});
	}

}