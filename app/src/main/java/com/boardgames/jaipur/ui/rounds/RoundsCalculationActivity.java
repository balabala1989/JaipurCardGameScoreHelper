package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;

import android.content.Intent;
import android.os.Bundle;

public class RoundsCalculationActivity extends AppCompatActivity {

    private GameDetails gameDetails;
    private String roundTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds_calculation);

        Intent receivedIntent = getIntent();
        if (receivedIntent == null)
            handleException();

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        roundTitle = computeRoundTitle();
    }

    private void handleException() {
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
}
