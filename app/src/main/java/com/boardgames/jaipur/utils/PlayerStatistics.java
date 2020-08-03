package com.boardgames.jaipur.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.boardgames.jaipur.entities.Player;

public class PlayerStatistics implements Parcelable {
    private Player player;
    private long gamesPlayed;
    private long gamesWon;
    private Uri playerProfile;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public long getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Uri getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(Uri playerProfile) {
        this.playerProfile = playerProfile;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.player, flags);
        dest.writeLong(this.gamesPlayed);
        dest.writeLong(this.gamesWon);
        dest.writeParcelable(this.playerProfile, flags);
    }

    public PlayerStatistics() {
    }

    protected PlayerStatistics(Parcel in) {
        this.player = in.readParcelable(Player.class.getClassLoader());
        this.gamesPlayed = in.readLong();
        this.gamesWon = in.readLong();
        this.playerProfile = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlayerStatistics> CREATOR = new Parcelable.Creator<PlayerStatistics>() {
        @Override
        public PlayerStatistics createFromParcel(Parcel source) {
            return new PlayerStatistics(source);
        }

        @Override
        public PlayerStatistics[] newArray(int size) {
            return new PlayerStatistics[size];
        }
    };
}
