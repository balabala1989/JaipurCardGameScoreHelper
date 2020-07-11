package com.boardgames.jaipur.ui.gamehistory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RoundDetailsActivity extends AppCompatActivity {

    private GameDetails gameDetails;
    private int roundInDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_details);


        Intent receivedIntent = getIntent();
        if (receivedIntent == null) {
            handleException();
        }

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        roundInDisplay = receivedIntent.getIntExtra(ApplicationConstants.GAME_SUMM_TO_ROUND_SUMM_ROUND_EDIT, -1);

        if (gameDetails == null || roundInDisplay == -1)
            handleException();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(GameUtils.computeRoundTitle(this, roundInDisplay));

        GameUtils.loadUserDetailsInSummaryPage(RoundDetailsActivity.this, getApplicationContext(), gameDetails);
        if (gameDetails.getPlayerOneRounds().get(roundInDisplay).getWinner() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
            findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
        }

        setScores();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleException() {
        Toast.makeText(getApplicationContext(), "Unable to load Game Details!!!!", Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void setScores() {
        Round playerOneRound = gameDetails.getPlayerOneRounds().get(roundInDisplay);
        Round playerTwoRound = gameDetails.getPlayerTwoRounds().get(roundInDisplay);

        setScoreInTextView(findViewById(R.id.playerOneDiamondTextView),playerOneRound.getDiamondScore());
        setScoreInTextView(findViewById(R.id.playerTwoDiamondTextView),playerTwoRound.getDiamondScore());
        setScoreInTextView(findViewById(R.id.playerOneGoldTextView),playerOneRound.getGoldScore());
        setScoreInTextView(findViewById(R.id.playerTwoGoldTextView),playerTwoRound.getGoldScore());
        setScoreInTextView(findViewById(R.id.playerOneSilverTextView),playerOneRound.getSilverScore());
        setScoreInTextView(findViewById(R.id.playerTwoSilverTextView),playerTwoRound.getSilverScore());
        setScoreInTextView(findViewById(R.id.playerOneClothTextView),playerOneRound.getClothScore());
        setScoreInTextView(findViewById(R.id.playerTwoClothTextView),playerTwoRound.getClothScore());
        setScoreInTextView(findViewById(R.id.playerOneSpiceTextView),playerOneRound.getSpiceScore());
        setScoreInTextView(findViewById(R.id.playerTwoSpiceTextView),playerTwoRound.getSpiceScore());
        setScoreInTextView(findViewById(R.id.playerOneLeatherTextView),playerOneRound.getLeatherScore());
        setScoreInTextView(findViewById(R.id.playerTwoLeatherTextView),playerTwoRound.getLeatherScore());
        setScoreInTextView(findViewById(R.id.playerOneThreeTokenTextView),playerOneRound.getThreeCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerTwoThreeTokenTextView),playerTwoRound.getThreeCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerOneFourTokenTextView),playerOneRound.getFourCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerTwoFourTokenTextView),playerTwoRound.getFourCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerOneFiveTokenTextView),playerOneRound.getFiveCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerTwoFiveTokenTextView),playerTwoRound.getFiveCardTokenScore());
        setScoreInTextView(findViewById(R.id.playerOneCamelTokenTextView),playerOneRound.getCamelReceived() == 'Y' ? 5 : 0);
        setScoreInTextView(findViewById(R.id.playerTwoCamelTokenTextView),playerTwoRound.getCamelReceived() == 'Y' ? 5 : 0);
        setScoreInTextView(findViewById(R.id.playerOneSumTextView),playerOneRound.getScore());
        setScoreInTextView(findViewById(R.id.playerTwoSumTextView),playerTwoRound.getScore());

    }

    private void setScoreInTextView(TextView textView, int score) {
        textView.setText(String.valueOf(score));
    }
}
