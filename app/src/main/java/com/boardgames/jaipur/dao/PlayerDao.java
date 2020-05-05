package com.boardgames.jaipur.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.boardgames.jaipur.entities.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertPlayer(Player player);

    @Update
    public void updatePlayer(Player player);

    @Delete
    public void deletePlayer(Player player);

    @Query("SELECT * from players ORDER BY name ASC")
    LiveData<List<Player>> getAllPlayers();
}
