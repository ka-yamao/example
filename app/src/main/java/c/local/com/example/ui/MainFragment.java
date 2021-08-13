package c.local.com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import c.local.com.example.R;
import c.local.com.example.databinding.MainFragmentBinding;

public class MainFragment extends Fragment {

	// インスタンス
	public static MainFragment newInstance() {
		return new MainFragment();
	}

	// ViewModel
	private MainViewModel mViewModel;
	// ページアダプター
	CollectionPagerAdapter collectionPagerAdapter;
	// データバインディング
	private MainFragmentBinding mBinding;


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false);
		return mBinding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// ViewModel
		mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
		// ページアダプター
		collectionPagerAdapter = new CollectionPagerAdapter(this);
		mBinding.pager.setAdapter(collectionPagerAdapter);
		// タブレイアウトを設定
		new TabLayoutMediator(mBinding.tabLayout, mBinding.pager, (tab, position) -> tab.setText("PAGE " + (position + 1))).attach();
	}
}

/**
 * ページアダプター
 */
class CollectionPagerAdapter extends FragmentStateAdapter {
	public CollectionPagerAdapter(Fragment f) {
		super(f);
	}

	@Override
	public Fragment createFragment(int position) {
		return new PokemonListFragment();
	}

	@Override
	public int getItemCount() {
		return 3;
	}
}