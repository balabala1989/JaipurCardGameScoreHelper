package com.boardgames.jaipur.ui.playersmanagement;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.PlayerRepository;

import java.util.List;

public class PlayersManagementViewModel extends AndroidViewModel {

    private PlayerRepository playerRepository;

    private LiveData<List<Player>> allPlayers;

    public PlayersManagementViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        allPlayers = playerRepository.getAllPlayers();
    }

    LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public long insert(Player player) {
        return playerRepository.insert(player);
    }

    public void update(Player player) {
        playerRepository.update(player);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public void updatePlayerAvatar(long playerId, String playerAvatar) {
        playerRepository.updatePlayerAvatar(playerId, playerAvatar);
    }
}
