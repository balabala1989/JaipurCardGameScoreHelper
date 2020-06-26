package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.GoodsDetailsForARound;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class RoundsCalculationSummaryActivity extends AppCompatActivity {

    private GameDetails gameDetails;
    private GoodsDetailsForARound goodsDetailsForARound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounds_calculation_summary);

        Intent receivedIntent = getIntent();
        if (receivedIntent == null)
            handleException(true);

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(computeRoundTitle(gameDetails.getRoundInProgress()));

        goodsDetailsForARound = new GoodsDetailsForARound();

        GameUtils.loadPlayerDetailsInDisplay(this, gameDetails);


    }




    private String computeRoundTitle(int roundInProgress) {
        switch (roundInProgress) {
            case 1:
                return getString(R.string.gamesummary_roundone_title);
            case 2:
                return getString(R.string.gamesummary_roundtwo_title);
            case 3:
                return getString(R.string.gamesummary_roundthree_title);
            default:
                return "";
        }

    }

    private void handleException(boolean isExceptionOccurred) {
        Intent replyIntent = new Intent();
        if (isExceptionOccurred)
            replyIntent.putExtra(ApplicationConstants.EXCEPTION_DUE_TO_UNAVAILABILITY_OF_INTENT,"Y");
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}
