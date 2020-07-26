package com.boardgames.jaipur.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.boardgames.jaipur.entities.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertGame(Game game);

    @Update
    public void updateGame(Game game);

    @Delete
    public void deleteGame(Game game);

    @Query("SELECT * FROM games WHERE STATUS ='C' ORDER BY time_created DESC")
    public LiveData<List<Game>> getAllGames();

    @Query("SELECT * FROM games WHERE id = :gameId")
    public LiveData<Game> getAGame(long gameId);

    @Transaction
    @Query("DELETE FROM games")
    public void deleteAllGames();

    @Query("SELECT * FROM games")
    public LiveData<List<Game>> getPendingGame();

    @Transaction
    @Query("SELECT * FROM games WHERE status = 'C' AND (player_one_id = :playerId OR player_two_id = :playerId)")
    public LiveData<List<Game>> getGamesForAPlayer(long playerId);

}
