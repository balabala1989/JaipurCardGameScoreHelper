package com.boardgames.jaipur.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "rounds", indices = {@Index("game_id"), @Index("player_id")})
public class Round implements Parcelable, Serializable {

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

    @ColumnInfo(name = "diamond_score")
    private int diamondScore;

    @ColumnInfo(name = "gold_score")
    private int goldScore;

    @ColumnInfo(name = "silver_score")
    private int silverScore;

    @ColumnInfo(name = "cloth_score")
    private int clothScore;

    @ColumnInfo(name = "spice_score")
    private int spiceScore;

    @ColumnInfo(name = "leather_score")
    private int leatherScore;

    @ColumnInfo(name = "three_card_token_score")
    private int threeCardTokenScore;

    @ColumnInfo(name = "four_card_token_score")
    private int fourCardTokenScore;

    @ColumnInfo(name = "five_card_token_score")
    private int fiveCardTokenScore;

    @ColumnInfo(name = "seal_of_excellence")
    private char sealOfExcellence;

    @ColumnInfo(name = "camel_received")
    private char camelReceived;

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

    public int getDiamondScore() {
        return diamondScore;
    }

    public void setDiamondScore(int diamondScore) {
        this.diamondScore = diamondScore;
    }

    public int getGoldScore() {
        return goldScore;
    }

    public void setGoldScore(int goldScore) {
        this.goldScore = goldScore;
    }

    public int getSilverScore() {
        return silverScore;
    }

    public void setSilverScore(int silverScore) {
        this.silverScore = silverScore;
    }

    public int getClothScore() {
        return clothScore;
    }

    public void setClothScore(int clothScore) {
        this.clothScore = clothScore;
    }

    public int getSpiceScore() {
        return spiceScore;
    }

    public void setSpiceScore(int spiceScore) {
        this.spiceScore = spiceScore;
    }

    public int getLeatherScore() {
        return leatherScore;
    }

    public void setLeatherScore(int leatherScore) {
        this.leatherScore = leatherScore;
    }

    public char getSealOfExcellence() {
        return sealOfExcellence;
    }

    public void setSealOfExcellence(char sealOfExcellence) {
        this.sealOfExcellence = sealOfExcellence;
    }

    public char getCamelReceived() {
        return camelReceived;
    }

    public void setCamelReceived(char camelReceived) {
        this.camelReceived = camelReceived;
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

    public int getThreeCardTokenScore() {
        return threeCardTokenScore;
    }

    public void setThreeCardTokenScore(int threeCardTokenScore) {
        this.threeCardTokenScore = threeCardTokenScore;
    }

    public int getFourCardTokenScore() {
        return fourCardTokenScore;
    }

    public void setFourCardTokenScore(int fourCardTokenScore) {
        this.fourCardTokenScore = fourCardTokenScore;
    }

    public int getFiveCardTokenScore() {
        return fiveCardTokenScore;
    }

    public void setFiveCardTokenScore(int fiveCardTokenScore) {
        this.fiveCardTokenScore = fiveCardTokenScore;
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
        dest.writeInt(this.diamondScore);
        dest.writeInt(this.goldScore);
        dest.writeInt(this.silverScore);
        dest.writeInt(this.clothScore);
        dest.writeInt(this.spiceScore);
        dest.writeInt(this.leatherScore);
        dest.writeInt(this.threeCardTokenScore);
        dest.writeInt(this.fourCardTokenScore);
        dest.writeInt(this.fiveCardTokenScore);
        dest.writeInt(this.sealOfExcellence);
        dest.writeInt(this.camelReceived);
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
        this.diamondScore = in.readInt();
        this.goldScore = in.readInt();
        this.silverScore = in.readInt();
        this.clothScore = in.readInt();
        this.spiceScore = in.readInt();
        this.leatherScore = in.readInt();
        this.threeCardTokenScore = in.readInt();
        this.fourCardTokenScore = in.readInt();
        this.fiveCardTokenScore = in.readInt();
        this.sealOfExcellence = (char) in.readInt();
        this.camelReceived = (char) in.readInt();
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
