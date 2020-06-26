package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.GoodsDetailsForARound;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class RoundsCalculationSummaryActivity extends AppCompatActivity {

    private GameDetails gameDetails;
    private GoodsDetailsForARound goodsDetailsForARound;
    private TextView diamondPlayerOneTextView;
    private TextView diamondPlayerTwoTextView;
    private TextView goldPlayerOneTextView;
    private TextView goldPlayerTwoTextView;
    private TextView silverPlayerOneTextView;
    private TextView silverPlayerTwoTextView;

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

        ImageView playerOneImageView = findViewById(R.id.playerOneImageView);
        ImageView playerTwoImageView = findViewById(R.id.playerTwoImageView);
        TextView playerOneTextView = findViewById(R.id.playerOneTextView);
        TextView playerTwoTextView = findViewById(R.id.playerTwoTextView);

        playerOneTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());
        playerTwoTextView.setText(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());

        int width = PlayerUtils.getWidthforImageViewByOneThird(this);
        Glide.with(this)
                .load(gameDetails.getPlayersInAGame().getPlayerOneProfile())
                .override(width, width)
                .into(playerOneImageView);
        Glide.with(this)
                .load(gameDetails.getPlayersInAGame().getPlayerTwoProfile())
                .override(width, width)
                .into(playerTwoImageView);

        initializeAllTextViewClickListeners();

        if (gameDetails.getPlayerOneRounds() == null) {
            gameDetails.setPlayerOneRounds(new HashMap<>());
        }

        if (gameDetails.getPlayerTwoRounds() == null) {
            gameDetails.setPlayerTwoRounds(new HashMap<>());
        }

        if (gameDetails.getPlayerOneRounds().isEmpty() || !gameDetails.getPlayerOneRounds().containsKey(gameDetails.getRoundInProgress())) {
            gameDetails.getPlayerOneRounds().put(gameDetails.getRoundInProgress(), initializeRoundWithPlayer(gameDetails.getPlayersInAGame().getPlayerOne().getId()));
        }

        if (gameDetails.getPlayerTwoRounds().isEmpty() || !gameDetails.getPlayerTwoRounds().containsKey(gameDetails.getRoundInProgress())) {
            gameDetails.getPlayerTwoRounds().put(gameDetails.getRoundInProgress(), initializeRoundWithPlayer(gameDetails.getPlayersInAGame().getPlayerTwo().getId()));
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ApplicationConstants.ROUND_SUMMARY_ITEM_DISPLAY_REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                handleOKResult(data);
            }
        }
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

    private void initializeAllTextViewClickListeners() {
        diamondPlayerOneTextView = findViewById(R.id.playerOneDiamondTextView);
        diamondPlayerTwoTextView = findViewById(R.id.playerTwoDiamondTextView);
        goldPlayerOneTextView = findViewById(R.id.playerOneGoldTextView);
        goldPlayerTwoTextView = findViewById(R.id.playerTwoGoldTextView);
        silverPlayerOneTextView = findViewById(R.id.playerOneSilverTextView);
        silverPlayerTwoTextView = findViewById(R.id.playerTwoSilverTextView);


        diamondPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS);
            }
        });
        diamondPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS);
            }
        });
        goldPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_GOLD_GOODS);
            }
        });
        goldPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_GOLD_GOODS);
            }
        });
        silverPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_SILVER_GOODS);
            }
        });
        silverPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_SILVER_GOODS);
            }
        });
    }

    private void handleTextViewClick(long playerClicked, String goodsSelected) {
        Intent requestIntent = new Intent(this, ItemsDisplayActivity.class);
        requestIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        requestIntent.putExtra(ApplicationConstants.GOODS_NAME_FROM_SUMMARY_TO_ITEM_DETAILS, goodsSelected);

        requestIntent.putExtra(ApplicationConstants.GOODS_DATA_FROM_SUMMARY_TO_ITEM_DETAILS, getGoodsDetailFromGoods(goodsSelected));
        requestIntent.putExtra(ApplicationConstants.SELECTED_PLAYER_FROM_SUMMARY_TO_ITEM_DETAILS, playerClicked);
        startActivityForResult(requestIntent, ApplicationConstants.ROUND_SUMMARY_ITEM_DISPLAY_REQUEST_IMAGE);
    }

    private void handleOKResult(Intent data) {
        String goodsDataReceived = "", goodsNameReceived = "";
        long selectedPlayerID;
        gameDetails = (GameDetails) data.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        goodsDataReceived = data.getStringExtra(ApplicationConstants.GOODS_DATA_FROM_SUMMARY_TO_ITEM_DETAILS);
        goodsNameReceived = data.getStringExtra(ApplicationConstants.GOODS_NAME_FROM_SUMMARY_TO_ITEM_DETAILS);
        selectedPlayerID = data.getLongExtra(ApplicationConstants.SELECTED_PLAYER_FROM_SUMMARY_TO_ITEM_DETAILS, ApplicationConstants.DEFAULT_PLAYER_ID);

        updateGoodsDetailsForARoundAndScore(goodsNameReceived, goodsDataReceived, selectedPlayerID);

    }

    private void updateGoodsDetailsForARoundAndScore(String goodsNameReceived, String goodsDataReceived, long selectedPlayerID) {
        switch(goodsNameReceived) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
                goodsDetailsForARound.setDiamondGoodsDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    diamondPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getDiamondScore()));
                else
                    diamondPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getDiamondScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                goodsDetailsForARound.setGoldGoodsDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    goldPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getGoldScore()));
                else
                    goldPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getGoldScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                goodsDetailsForARound.setSilverGoodsDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    silverPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getSilverScore()));
                else
                    silverPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getSilverScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                goodsDetailsForARound.setClothGoodsDetail(goodsDataReceived);
                break;
            default:
                return;
        }
    }

    private String getGoodsDetailFromGoods(String goodsSelected) {
        switch (goodsSelected) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
               return goodsDetailsForARound.getDiamondGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                return goodsDetailsForARound.getGoldGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                return goodsDetailsForARound.getSilverGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                //goodsDetailsForARound.setClothGoodsDetail(goodsDataReceived);
                break;
            default:
                return "";
        }
        return "";
    }

    private Round initializeRoundWithPlayer(long playerID) {
        Round round = new Round();
        round.setGameID(gameDetails.getGame().getId());
        round.setPlayerID(playerID);
        round.setRoundNumber(gameDetails.getRoundInProgress());
        round.setDiamondScore(0);
        round.setGoldScore(0);
        round.setSilverScore(0);
        return round;
    }
}
