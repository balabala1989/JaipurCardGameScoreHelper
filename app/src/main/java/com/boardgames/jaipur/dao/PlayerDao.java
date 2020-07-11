package com.boardgames.jaipur.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.boardgames.jaipur.entities.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertPlayer(Player player);

    @Update
    public void updatePlayer(Player player);

    @Delete
    public void deletePlayer(Player player);

   @Query("SELECT * from players ORDER BY name ASC")
    public LiveData<List<Player>> getAllPlayers();

    @Query("SELECT * FROM players WHERE id = :playerId")
    Player getPlayer(long playerId);

    @Transaction
    @Query("DELETE from players")
    public void deleteAllPlayers();

    @Query("UPDATE players SET player_avatar = :playerAvatar, time_updated = :timeUpdated WHERE id = :playerId")
    public int updatePlayerAvatar(long playerId, long timeUpdated, String playerAvatar);
}
