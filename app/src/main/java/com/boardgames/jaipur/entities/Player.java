package com.boardgames.jaipur.entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class Player {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String playerName;

    @ColumnInfo(name = "player_avatar")
    private String playerAvatar;

    //Time is saved in Epoch time/UTC unix time
    @NonNull
    @ColumnInfo(name = "time_created")
    private long timeCreated;

    @NonNull
    @ColumnInfo(name = "time_updated")
    private long timeUpdated;

    @NonNull
    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(@NonNull long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @NonNull
    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(@NonNull long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(@NonNull String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerAvatar() {
        return playerAvatar;
    }

    public void setPlayerAvatar(String playerAvatar) {
        this.playerAvatar = playerAvatar;
    }
}
