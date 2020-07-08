package com.boardgames.jaipur.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.dao.GameDao;
import com.boardgames.jaipur.dao.RoundDao;
import com.boardgames.jaipur.database.GamesRoomDatabase;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Round;

import java.util.List;
import java.util.concurrent.Semaphore;

public class GamesAndRoundsRepository {

    private GameDao gameDao;
    private RoundDao roundDao;
    private Semaphore semaphore;
    private long gameId = -1;
    private long roundId = -1;

    public GamesAndRoundsRepository(Application application) {
        GamesRoomDatabase gamesRoomDatabase = GamesRoomDatabase.getDabase(application);
        gameDao = gamesRoomDatabase.gameDao();
        roundDao = gamesRoomDatabase.roundDao();
    }

    //Implementing GameDao functions

    public LiveData<List<Game>> getAllGames() {
        return gameDao.getAllGames();
    }

    public long insertGame(Game game) {
        semaphore = new Semaphore(0);
        new GameInsertAsyncTask().execute(game);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
       return gameId;
    }

    public void updateGame(Game game) {
        GamesRoomDatabase.databaseWriterExecutor.execute(() -> {
            gameDao.updateGame(game);
        });
    }

    public void deleteGame(Game game) {
        GamesRoomDatabase.databaseWriterExecutor.execute(() -> {
            gameDao.deleteGame(game);
        });
    }

    public LiveData<Game> getAGame(long gameId) {return gameDao.getAGame(gameId);}

    public LiveData<List<Game>> getPendingGame() {return gameDao.getPendingGame();}

    //Implementing RounDao functions

    public LiveData<List<Round>> getAllRounds() {return roundDao.getAllRounds();}

    public long insertRound(Round round) {
        semaphore = new Semaphore(0);
        new RoundInsertAsyncTask().execute(round);
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return roundId;
    }

    public void updateRound(Round round) {
        GamesRoomDatabase.databaseWriterExecutor.execute(() -> {
            roundDao.updateRound(round);
        });
    }

    public void deleteRound(Round round) {
        GamesRoomDatabase.databaseWriterExecutor.execute(() -> {
            roundDao.deleteRound(round);
        });
    }

    public LiveData<Round> getARound(long roundId) {return roundDao.getARound(roundId);}

    public LiveData<List<Round>> getRoundsForAGame(long gameId) {return roundDao.getRoundsForAGame(gameId);}


    private class GameInsertAsyncTask extends AsyncTask<Game, Void, Void> {

        @Override
        protected Void doInBackground(Game... games) {
            gameId = gameDao.insertGame(games[0]);
            semaphore.release();
            return null;
        }
    }

    private class RoundInsertAsyncTask extends AsyncTask<Round, Void, Void> {

        @Override
        protected Void doInBackground(Round... rounds) {
            roundId = roundDao.insertRound(rounds[0]);
            semaphore.release();
            return null;
        }
    }

}
