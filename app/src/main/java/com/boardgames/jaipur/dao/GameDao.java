package com.boardgames.jaipur.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.boardgames.jaipur.entities.Game;

import java.util.List;

public interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertGame(Game game);

    @Update
    public void updateGame(Game game);

    @Delete
    public void deleteGame(Game game);

    @Query("SELECT * FROM games ORDER BY time_created DESC")
    public LiveData<List<Game>> getAllGames();

    @Query("SELECT * FROM games where id = :gameId")
    public LiveData<Game> getAGame(long gameId);

    @Transaction
    @Query("DELETE FROM games")
    public void deleteAllGames();
}
