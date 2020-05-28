package com.boardgames.jaipur.ui.newgame;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.PlayerRepository;

import java.util.List;

public class NewGameViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private PlayerRepository playerRepository;

    private LiveData<List<Player>> allPlayers;

    public NewGameViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        allPlayers = playerRepository.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public long insert(Player player) {
        return playerRepository.insert(player);
    }
}