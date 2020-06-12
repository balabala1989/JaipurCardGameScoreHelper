package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.HashMap;

public class RoundsCalculationActivity extends AppCompatActivity {

    private GameDetails gameDetails;
    private String roundTitle;
    private HashMap<String, HashMap<Long, Integer>> goodsToPlayersScore;
    private HashMap<Long, Uri> playerToProfileUri;
    private DraggedItemsListViewModel draggedItemsListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds_calculation);

        Intent receivedIntent = getIntent();
        if (receivedIntent == null)
            handleException();

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        roundTitle = computeRoundTitle();

        playerToProfileUri = new HashMap<>();
        playerToProfileUri.put(gameDetails.getPlayersInAGame().getPlayerOne().getId(), gameDetails.getPlayersInAGame().getPlayerOneProfile());
        playerToProfileUri.put(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), gameDetails.getPlayersInAGame().getPlayerTwoProfile());
       draggedItemsListViewModel = new ViewModelProvider(this).get(DraggedItemsListViewModel.class);
    }

    public void handleException() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    private String computeRoundTitle() {
        switch(gameDetails.getRoundInProgress()) {
            case 1:
                return getString(R.string.gamesummary_roundone_title) + ApplicationConstants.HYPEN;
            case 2:
                return getString(R.string.gamesummary_roundtwo_title) + ApplicationConstants.HYPEN;
            case 3:
                return getString(R.string.gamesummary_roundthree_title) + ApplicationConstants.HYPEN;
            default:
                return "";
        }
    }

    public GameDetails getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(GameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }

    public String getRoundTitle() {
        return roundTitle;
    }

    public void setRoundTitle(String roundTitle) {
        this.roundTitle = roundTitle;
    }

    public HashMap<String, HashMap<Long, Integer>> getGoodsToPlayersScore() {
        return goodsToPlayersScore;
    }

    public void setGoodsToPlayersScore(HashMap<String, HashMap<Long, Integer>> goodsToPlayersScore) {
        this.goodsToPlayersScore = goodsToPlayersScore;
    }

    public HashMap<Long, Uri> getPlayerToProfileUri() {
        return playerToProfileUri;
    }

    public void setPlayerToProfileUri(HashMap<Long, Uri> playerToProfileUri) {
        this.playerToProfileUri = playerToProfileUri;
    }

    public DraggedItemsListViewModel getDraggedItemsListViewModel() {
        return draggedItemsListViewModel;
    }

    public void setDraggedItemsListViewModel(DraggedItemsListViewModel draggedItemsListViewModel) {
        this.draggedItemsListViewModel = draggedItemsListViewModel;
    }
}
