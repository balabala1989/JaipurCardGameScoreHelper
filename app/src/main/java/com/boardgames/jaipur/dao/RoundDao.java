package com.boardgames.jaipur.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.boardgames.jaipur.entities.Round;

import java.util.List;

@Dao
public interface RoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertRound(Round round);

    @Update
    public void updateRound(Round round);

    @Delete
    public void deleteRound(Round round);

    @Query("SELECT * FROM rounds ORDER BY time_created DESC")
    public LiveData<List<Round>> getAllRounds();

    @Query("SELECT * FROM rounds where id = :roundId")
    public LiveData<Round> getARound(long roundId);

    @Transaction
    @Query("DELETE FROM rounds")
    public void deleteAllRounds();

    @Query("SELECT * FROM rounds where game_id = :gameId ORDER BY round_number ASC")
    public List<Round> getRoundsForAGame(long gameId);

    @Transaction
    @Query("DELETE FROM rounds WHERE game_id = :gameId")
    public int deleteAllRoundsForAGame(long gameId);
}
