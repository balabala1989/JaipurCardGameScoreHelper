package com.boardgames.jaipur.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.dao.PlayerDao;
import com.boardgames.jaipur.database.PlayerRoomDatabase;
import com.boardgames.jaipur.entities.Player;

import java.util.List;
import java.util.concurrent.Semaphore;

public class PlayerRepository {

    private PlayerDao playerDao;
    private Semaphore semaphore;
    private Player singlePlayer;

    public PlayerRepository(Application application) {
        PlayerRoomDatabase playerRoomDatabase = PlayerRoomDatabase.getDatabase(application);
        playerDao = playerRoomDatabase.playerDao();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    public Player getPlayer(long playerId) {
        semaphore = new Semaphore(0);
        new PlayerQueryAsyncTask().execute(playerId);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return singlePlayer;
    }

    public Player getPlayerByName(String playerName) {
        semaphore = new Semaphore(0);
        new PlayerQueryByNameAsyncTask().execute(playerName);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return singlePlayer;
    }

    public long updatePlayerAvatar(long playerId, String playerAvatar) {
        final long[] updateStatus = {0};
        PlayerRoomDatabase.databaseWriterExecutor.execute(() -> {
            updateStatus[0] = playerDao.updatePlayerAvatar(playerId, System.currentTimeMillis(), playerAvatar);
        });
        return updateStatus[0];
    }

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

    private class PlayerQueryAsyncTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... players) {
            singlePlayer = playerDao.getPlayer(players[0]);
            semaphore.release();
            return null;
        }
    }

    private class PlayerQueryByNameAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... players) {
            singlePlayer = playerDao.getPlayerByName(players[0]);
            semaphore.release();
            return null;
        }
    }
}
