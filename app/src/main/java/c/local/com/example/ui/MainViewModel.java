package c.local.com.example.ui;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;
import c.local.com.example.Repository;

public class MainViewModel extends ViewModel {

	private final Repository repository;
	// private final SavedStateHandle savedStateHandle;

	@ViewModelInject
	public MainViewModel(Repository repository) {
		this.repository = repository;
	}

//	// public MainViewModel(Repository repository, @Assisted SavedStateHandle savedStateHandle) {
//		this.repository = repository;
//		this.savedStateHandle = savedStateHandle;
//	}

}