package com.boardgames.jaipur.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "games", indices = {@Index("status"), @Index("player_one_id"), @Index("player_two_id")})
public class Game implements Parcelable {

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

    @ColumnInfo(name = "notes")
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.playerOneID);
        dest.writeLong(this.playerTwoID);
        dest.writeInt(this.roundsCompleted);
        dest.writeString(this.playerOneScores);
        dest.writeString(this.playerTwoScores);
        dest.writeLong(this.winner);
        dest.writeString(this.gamePlayStatus);
        dest.writeString(this.gamePhotoLocation);
        dest.writeString(this.notes);
        dest.writeLong(this.timeCreated);
        dest.writeLong(this.timeUpdated);
    }

    public Game() {
    }

    protected Game(Parcel in) {
        this.id = in.readLong();
        this.playerOneID = in.readLong();
        this.playerTwoID = in.readLong();
        this.roundsCompleted = in.readInt();
        this.playerOneScores = in.readString();
        this.playerTwoScores = in.readString();
        this.winner = in.readLong();
        this.gamePlayStatus = in.readString();
        this.gamePhotoLocation = in.readString();
        this.notes = in.readString();
        this.timeCreated = in.readLong();
        this.timeUpdated = in.readLong();
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
