package com.boardgames.jaipur.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "players", indices = {@Index("name")})
public class Player implements Parcelable, Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.playerName);
        dest.writeString(this.playerAvatar);
        dest.writeLong(this.timeCreated);
        dest.writeLong(this.timeUpdated);
    }

    public Player() {
    }

    protected Player(Parcel in) {
        this.id = in.readLong();
        this.playerName = in.readString();
        this.playerAvatar = in.readString();
        this.timeCreated = in.readLong();
        this.timeUpdated = in.readLong();
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
