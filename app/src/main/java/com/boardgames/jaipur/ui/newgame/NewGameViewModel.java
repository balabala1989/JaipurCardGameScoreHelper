package com.boardgames.jaipur.ui.newgame;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.repository.PlayerRepository;

import java.util.List;

public class NewGameViewModel extends AndroidViewModel {

    private PlayerRepository playerRepository;

    private GamesAndRoundsRepository gamesAndRoundsRepository;

    public NewGameViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        gamesAndRoundsRepository = new GamesAndRoundsRepository(application);
    }

    public LiveData<List<Player>> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    public long insertAPlayer(Player player) {
        return playerRepository.insert(player);
    }

    public long createAGame(Game game) {
        return gamesAndRoundsRepository.insertGame(game);
    }

    public void deleteAGame(Game game) {
        gamesAndRoundsRepository.deleteGame(game);
    }

    public void updateAGame(Game game) { gamesAndRoundsRepository.updateGame(game); }
}