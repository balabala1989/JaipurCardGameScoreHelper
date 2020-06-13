package com.boardgames.jaipur.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.entities.Round;

import java.util.HashMap;

public class GameDetails implements Parcelable {
    private Game game;
    private PlayersInAGame playersInAGame;
    private HashMap<Integer, Round> playerOneRounds;
    private HashMap<Integer, Round> playerTwoRounds;
    private HashMap<Integer, Player> roundWinners;
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

    public HashMap<Integer, Round> getPlayerOneRounds() {
        return playerOneRounds;
    }

    public void setPlayerOneRounds(HashMap<Integer, Round> playerOneRounds) {
        this.playerOneRounds = playerOneRounds;
    }

    public HashMap<Integer, Round> getPlayerTwoRounds() {
        return playerTwoRounds;
    }

    public void setPlayerTwoRounds(HashMap<Integer, Round> playerTwoRounds) {
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

    public HashMap<Integer, Player> getRoundWinners() {
        return roundWinners;
    }

    public void setRoundWinners(HashMap<Integer, Player> roundWinners) {
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
        dest.writeSerializable(this.playerOneRounds);
        dest.writeSerializable(this.playerTwoRounds);
        dest.writeSerializable(this.roundWinners);
        dest.writeInt(this.roundInProgress);
        dest.writeInt(this.roundsCompleted);
    }

    public GameDetails() {
    }

    protected GameDetails(Parcel in) {
        this.game = in.readParcelable(Game.class.getClassLoader());
        this.playersInAGame = in.readParcelable(PlayersInAGame.class.getClassLoader());
        this.playerOneRounds = (HashMap<Integer, Round>) in.readSerializable();
        this.playerTwoRounds = (HashMap<Integer, Round>) in.readSerializable();
        this.roundWinners = (HashMap<Integer, Player>) in.readSerializable();
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
