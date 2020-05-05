package com.boardgames.jaipur.ui.rateme;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RateMeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RateMeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rate me fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
