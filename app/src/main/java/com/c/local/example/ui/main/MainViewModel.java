package com.c.local.example.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
	private MutableLiveData<Integer> count;
	private MutableLiveData<String> text;

	public String getText() {
		return text.getValue();
	}

	public void setText(String str) {
		if (text == null) {
			text = new MutableLiveData<>();
		}
		text.setValue(str);

	}

}