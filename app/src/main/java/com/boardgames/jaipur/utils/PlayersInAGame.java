package com.boardgames.jaipur.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;

import java.io.Serializable;

public class PlayersInAGame implements Parcelable {

    private Player playerOne;
    private Player playerTwo;
    private Uri playerOneProfile;
    private Uri playerTwoProfile;

    public PlayersInAGame(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Uri getPlayerOneProfile() {
        return playerOneProfile;
    }

    public void setPlayerOneProfile(Uri playerOneProfile) {
        this.playerOneProfile = playerOneProfile;
    }

    public Uri getPlayerTwoProfile() {
        return playerTwoProfile;
    }

    public void setPlayerTwoProfile(Uri playerTwoProfile) {
        this.playerTwoProfile = playerTwoProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.playerOne, flags);
        dest.writeParcelable(this.playerTwo, flags);
        dest.writeParcelable(this.playerOneProfile, flags);
        dest.writeParcelable(this.playerTwoProfile, flags);
    }

    protected PlayersInAGame(Parcel in) {
        this.playerOne = in.readParcelable(Player.class.getClassLoader());
        this.playerTwo = in.readParcelable(Player.class.getClassLoader());
        this.playerOneProfile = in.readParcelable(Uri.class.getClassLoader());
        this.playerTwoProfile = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlayersInAGame> CREATOR = new Parcelable.Creator<PlayersInAGame>() {
        @Override
        public PlayersInAGame createFromParcel(Parcel source) {
            return new PlayersInAGame(source);
        }

        @Override
        public PlayersInAGame[] newArray(int size) {
            return new PlayersInAGame[size];
        }
    };
}
