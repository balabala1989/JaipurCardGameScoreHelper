package com.boardgames.jaipur.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rounds", indices = {@Index("game_id")})
public class Round implements Parcelable {

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

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "winner")
    private long winner;


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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public long getWinner() {
        return winner;
    }

    public void setWinner(long winner) {
        this.winner = winner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.gameID);
        dest.writeLong(this.playerID);
        dest.writeInt(this.roundNumber);
        dest.writeInt(this.score);
        dest.writeString(this.diamondCount);
        dest.writeString(this.goldCount);
        dest.writeString(this.silverCount);
        dest.writeString(this.silkCount);
        dest.writeString(this.spiceCount);
        dest.writeString(this.leatherCount);
        dest.writeString(this.sealOfExcellence);
        dest.writeString(this.camelCount);
        dest.writeString(this.photoPath);
        dest.writeString(this.notes);
        dest.writeLong(this.winner);
        dest.writeLong(this.timeCreated);
        dest.writeLong(this.timeUpdated);
    }

    public Round() {
    }

    protected Round(Parcel in) {
        this.id = in.readLong();
        this.gameID = in.readLong();
        this.playerID = in.readLong();
        this.roundNumber = in.readInt();
        this.score = in.readInt();
        this.diamondCount = in.readString();
        this.goldCount = in.readString();
        this.silverCount = in.readString();
        this.silkCount = in.readString();
        this.spiceCount = in.readString();
        this.leatherCount = in.readString();
        this.sealOfExcellence = in.readString();
        this.camelCount = in.readString();
        this.photoPath = in.readString();
        this.notes = in.readString();
        this.winner = in.readLong();
        this.timeCreated = in.readLong();
        this.timeUpdated = in.readLong();
    }

    public static final Parcelable.Creator<Round> CREATOR = new Parcelable.Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel source) {
            return new Round(source);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
