package com.boardgames.jaipur.ui.feedbacksupport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedbackSupportViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FeedbackSupportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is support management fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
