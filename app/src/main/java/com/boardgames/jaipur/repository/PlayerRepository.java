package com.boardgames.jaipur.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.dao.PlayerDao;
import com.boardgames.jaipur.database.PlayerRoomDatabase;
import com.boardgames.jaipur.entities.Player;

import java.util.List;

public class PlayerRepository {

    private PlayerDao playerDao;
    private LiveData<List<Player>> allPlayers;

    public PlayerRepository(Application application) {
        PlayerRoomDatabase playerRoomDatabase = PlayerRoomDatabase.getDatabase(application);
        playerDao = playerRoomDatabase.playerDao;
        allPlayers = playerDao.getAllPlayers();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return allPlayers;
    }

    public void insert(final Player player) {
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            playerDao.insertPlayer(player);
        });
    }

    public void update(final Player player) {
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            playerDao.updatePlayer(player);
        });
    }

    public void delete(final Player player) {
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            playerDao.deletePlayer(player);
        });
    }
}
