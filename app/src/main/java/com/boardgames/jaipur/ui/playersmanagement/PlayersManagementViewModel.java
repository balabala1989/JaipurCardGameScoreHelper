package com.boardgames.jaipur.ui.playersmanagement;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.repository.PlayerRepository;

import java.util.List;

public class PlayersManagementViewModel extends AndroidViewModel {

    private PlayerRepository playerRepository;

    private GamesAndRoundsRepository gamesAndRoundsRepository;

    private MediatorLiveData<Long> tempMediator;

    public PlayersManagementViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        gamesAndRoundsRepository = new GamesAndRoundsRepository(application);
    }

    LiveData<List<Player>> getAllPlayers() {
        return playerRepository.getAllPlayers();
    }

    public long insert(Player player) {
        return playerRepository.insert(player);
    }

    public void update(Player player) {
        playerRepository.update(player);
    }

    public LiveData<Long> deleteAPlayerWithGamesRounds(Player player) {
        if (tempMediator == null)
            tempMediator = new MediatorLiveData<>();
        LiveData<List<Game>> gamesForAPlayer = gamesAndRoundsRepository.getGamesForAPlayer(player.getId());

        tempMediator.addSource(gamesForAPlayer, games -> {
            long count = 0;
            for (Game game : games) {
                count += gamesAndRoundsRepository.deleteAllRoundsForAGame(game.getId());
                gamesAndRoundsRepository.deleteGame(game);
            }
            playerRepository.delete(player);
            tempMediator.setValue(count);
        });
        return tempMediator;
    }
}
