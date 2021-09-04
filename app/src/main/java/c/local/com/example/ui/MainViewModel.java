package c.local.com.example.ui;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;
import c.local.com.example.Repository;

public class MainViewModel extends ViewModel {

	private Repository repository;

	@ViewModelInject
	public MainViewModel(Repository repository) {
		this.repository = repository;
	}

}