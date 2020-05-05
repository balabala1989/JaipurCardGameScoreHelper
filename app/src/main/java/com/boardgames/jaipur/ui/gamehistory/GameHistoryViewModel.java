package com.boardgames.jaipur.ui.gamehistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GameHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gamehistory fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}