package com.boardgames.jaipur.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rounds")
public class Rounds implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "game_id")
    private long gameID;

    @NonNull
    @ColumnInfo(name = "player_id")
    private long playerID;

    @NonNull
    @ColumnInfo(name = "round_number")
    private int roundNumber;

    @NonNull
    @ColumnInfo(name = "score")
    private int score;

    @ColumnInfo(name = "diamond_count")
    private String diamondCount;

    @ColumnInfo(name = "gold_count")
    private String goldCount;

    @ColumnInfo(name = "silver_count")
    private String silverCount;

    @ColumnInfo(name = "silk_count")
    private String silkCount;

    @ColumnInfo(name = "spice_count")
    private String spiceCount;

    @ColumnInfo(name = "leather_count")
    private String leatherCount;

    @ColumnInfo(name = "seal_of_excellence")
    private String sealOfExcellence;

    @ColumnInfo(name = "camel_count")
    private String camelCount;

    @ColumnInfo(name = "photo_path")
    private String photoPath;

    @NonNull
    @ColumnInfo(name = "time_created")
    private long timeCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(String diamondCount) {
        this.diamondCount = diamondCount;
    }

    public String getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(String goldCount) {
        this.goldCount = goldCount;
    }

    public String getSilverCount() {
        return silverCount;
    }

    public void setSilverCount(String silverCount) {
        this.silverCount = silverCount;
    }

    public String getSilkCount() {
        return silkCount;
    }

    public void setSilkCount(String silkCount) {
        this.silkCount = silkCount;
    }

    public String getSpiceCount() {
        return spiceCount;
    }

    public void setSpiceCount(String spiceCount) {
        this.spiceCount = spiceCount;
    }

    public String getLeatherCount() {
        return leatherCount;
    }

    public void setLeatherCount(String leatherCount) {
        this.leatherCount = leatherCount;
    }

    public String getSealOfExcellence() {
        return sealOfExcellence;
    }

    public void setSealOfExcellence(String sealOfExcellence) {
        this.sealOfExcellence = sealOfExcellence;
    }

    public String getCamelCount() {
        return camelCount;
    }

    public void setCamelCount(String camelCount) {
        this.camelCount = camelCount;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
