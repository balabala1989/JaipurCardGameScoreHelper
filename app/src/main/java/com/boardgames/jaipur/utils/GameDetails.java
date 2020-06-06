package com.boardgames.jaipur.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.entities.Round;

import java.io.Serializable;

public class GameDetails implements Parcelable {
    private Game game;
    private PlayersInAGame playersInAGame;
    private Round[] playerOneRounds;
    private Round[] playerTwoRounds;
    private Player[] roundWinners;
    private int roundInProgress;
    private int roundsCompleted;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public PlayersInAGame getPlayersInAGame() {
        return playersInAGame;
    }

    public void setPlayersInAGame(PlayersInAGame playersInAGame) {
        this.playersInAGame = playersInAGame;
    }

    public Round[] getPlayerOneRounds() {
        return playerOneRounds;
    }

    public void setPlayerOneRounds(Round[] playerOneRounds) {
        this.playerOneRounds = playerOneRounds;
    }

    public Round[] getPlayerTwoRounds() {
        return playerTwoRounds;
    }

    public void setPlayerTwoRounds(Round[] playerTwoRounds) {
        this.playerTwoRounds = playerTwoRounds;
    }

    public int getRoundInProgress() {
        return roundInProgress;
    }

    public void setRoundInProgress(int roundInProgress) {
        this.roundInProgress = roundInProgress;
    }

    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    public void setRoundsCompleted(int roundsCompleted) {
        this.roundsCompleted = roundsCompleted;
    }

    public Player[] getRoundWinners() {
        return roundWinners;
    }

    public void setRoundWinners(Player[] roundWinners) {
        this.roundWinners = roundWinners;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.game, flags);
        dest.writeParcelable(this.playersInAGame, flags);
        dest.writeTypedArray(this.playerOneRounds, flags);
        dest.writeTypedArray(this.playerTwoRounds, flags);
        dest.writeTypedArray(this.roundWinners, flags);
        dest.writeInt(this.roundInProgress);
        dest.writeInt(this.roundsCompleted);
    }

    public GameDetails() {
    }

    protected GameDetails(Parcel in) {
        this.game = in.readParcelable(Game.class.getClassLoader());
        this.playersInAGame = in.readParcelable(PlayersInAGame.class.getClassLoader());
        this.playerOneRounds = in.createTypedArray(Round.CREATOR);
        this.playerTwoRounds = in.createTypedArray(Round.CREATOR);
        this.roundWinners = in.createTypedArray(Player.CREATOR);
        this.roundInProgress = in.readInt();
        this.roundsCompleted = in.readInt();
    }

    public static final Parcelable.Creator<GameDetails> CREATOR = new Parcelable.Creator<GameDetails>() {
        @Override
        public GameDetails createFromParcel(Parcel source) {
            return new GameDetails(source);
        }

        @Override
        public GameDetails[] newArray(int size) {
            return new GameDetails[size];
        }
    };
}
