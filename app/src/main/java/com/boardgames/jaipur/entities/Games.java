package com.boardgames.jaipur.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "games")
public class Games implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "player_one_id")
    private long playerOneID;

    @NonNull
    @ColumnInfo(name = "player_two_id")
    private long playerTwoID;

    @ColumnInfo(name = "rounds_completed")
    private int roundsCompleted;

    @ColumnInfo(name = "player_one_scores")
    private String playerOneScores;

    @ColumnInfo(name = "player_two_scores")
    private String playerTwoScores;

    @ColumnInfo(name = "winner")
    private long winner;

    @ColumnInfo(name = "status")
    private String gamePlayStatus;

    @ColumnInfo(name = "photo_location")
    private String gamePhotoLocation;

    @NonNull
    @ColumnInfo(name = "time_created")
    private long timeCreated;

    @NonNull
    @ColumnInfo(name = "time_updated")
    private long timeUpdated;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayerOneID() {
        return playerOneID;
    }

    public void setPlayerOneID(long playerOneID) {
        this.playerOneID = playerOneID;
    }

    public long getPlayerTwoID() {
        return playerTwoID;
    }

    public void setPlayerTwoID(long playerTwoID) {
        this.playerTwoID = playerTwoID;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public void setRoundsCompleted(int roundsCompleted) {
        this.roundsCompleted = roundsCompleted;
    }

    public String getPlayerOneScores() {
        return playerOneScores;
    }

    public void setPlayerOneScores(String playerOneScores) {
        this.playerOneScores = playerOneScores;
    }

    public String getPlayerTwoScores() {
        return playerTwoScores;
    }

    public void setPlayerTwoScores(String playerTwoScores) {
        this.playerTwoScores = playerTwoScores;
    }

    public long getWinner() {
        return winner;
    }

    public void setWinner(long winner) {
        this.winner = winner;
    }

    public String getGamePlayStatus() {
        return gamePlayStatus;
    }

    public void setGamePlayStatus(String gamePlayStatus) {
        this.gamePlayStatus = gamePlayStatus;
    }

    public String getGamePhotoLocation() {
        return gamePhotoLocation;
    }

    public void setGamePhotoLocation(String gamePhotoLocation) {
        this.gamePhotoLocation = gamePhotoLocation;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }
}
