package com.boardgames.jaipur.ui.playersmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayersManagementViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PlayersManagementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is players management fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
