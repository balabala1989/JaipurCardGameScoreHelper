package com.boardgames.jaipur.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.dao.PlayerDao;
import com.boardgames.jaipur.database.PlayerRoomDatabase;
import com.boardgames.jaipur.entities.Player;

import java.util.List;

public class PlayerRepository {

    private PlayerDao playerDao;

    public PlayerRepository(Application application) {
        PlayerRoomDatabase playerRoomDatabase = PlayerRoomDatabase.getDatabase(application);
        playerDao = playerRoomDatabase.playerDao();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    public LiveData<Player> getPlayer(long playerId) {
        return playerDao.getPlayer(playerId);
    }

    public long updatePlayerAvatar(long playerId, String playerAvatar) {
        final long[] updateStatus = {0};
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            updateStatus[0] = playerDao.updatePlayerAvatar(playerId, System.currentTimeMillis()/100, playerAvatar);
        });
        return updateStatus[0];
    }

    //TODO check for the name existence and then insert the value. Duplicate names are being inserted
    public long insert(final Player player) {
        final long[] updateStatus = {0};
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            updateStatus[0] = playerDao.insertPlayer(player);
        });
        return  updateStatus[0];
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
