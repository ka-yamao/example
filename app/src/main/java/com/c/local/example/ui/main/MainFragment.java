package com.c.local.example.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c.local.example.EllipsizedTextView;
import com.c.local.example.R;
import com.c.local.example.databinding.MainFragmentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {


	private MainFragmentBinding binding;
	private MainViewModel mainViewModel;


	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		binding = MainFragmentBinding.inflate(inflater, container, false);

		View view = binding.getRoot();
		EllipsizedTextView title = view.findViewById(R.id.ellipsizedTextView3);
		title.setEllipsis("・・・省略を動的に変更");
		title.setEllipsisColor(Color.RED);
		title.setText("Javaで動的にほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげほげ");
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}