package c.local.com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import c.local.com.example.R;
import c.local.com.example.viewmodel.HiltTestViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HiltFragment extends Fragment {

	private HiltTestViewModel mViewModel;

	public static HiltFragment newInstance() {
		return new HiltFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.hilt_fragment, container, false);
	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// TODO ↓ エラーになる。 java.lang.RuntimeException: Cannot create an instance of class c.local.com.example.viewmodel.HiltTestViewModel
		mViewModel = new ViewModelProvider(this).get(HiltTestViewModel.class);
	}

}