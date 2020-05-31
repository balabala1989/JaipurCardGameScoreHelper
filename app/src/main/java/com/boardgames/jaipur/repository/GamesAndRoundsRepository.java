package com.boardgames.jaipur.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.boardgames.jaipur.dao.GameDao;
import com.boardgames.jaipur.dao.RoundDao;
import com.boardgames.jaipur.database.GamesRoomDatabase;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Round;

import java.util.List;

public class GamesAndRoundsRepository {

    private GameDao gameDao;
    private RoundDao roundDao;

    public GamesAndRoundsRepository(Application application) {
        GamesRoomDatabase gamesRoomDatabase = GamesRoomDatabase.getDabase(application);
        gameDao = gamesRoomDatabase.gameDao();
        roundDao = gamesRoomDatabase.roundDao();
    }

    //Implementing GameDao functions

    public LiveData<List<Game>> getAllGames() {return gameDao.getAllGames();}

    public long insertGame(Game game) {return gameDao.insertGame(game);}

    public void updateGame(Game game) {gameDao.updateGame(game);}

    public void deleteGame(Game game) {gameDao.deleteGame(game);}

    public LiveData<Game> getAGame(long gameId) {return gameDao.getAGame(gameId);}

    public LiveData<List<Game>> getPendingGame() {return gameDao.getPendingGame();}

    //Implementing RounDao functions

    public LiveData<List<Round>> getAllRounds() {return roundDao.getAllRounds();}

    public long insertRound(Round round) {return roundDao.insertRound(round);}

    public void updateRound(Round round) {roundDao.updateRound(round);}

    public void deleteRound(Round round) {roundDao.deleteRound(round);}

    public LiveData<Round> getARound(long roundId) {return roundDao.getARound(roundId);}

    public LiveData<List<Round>> getRoundsForAGame(long gameId) {return roundDao.getRoundsForAGame(gameId);}

}
