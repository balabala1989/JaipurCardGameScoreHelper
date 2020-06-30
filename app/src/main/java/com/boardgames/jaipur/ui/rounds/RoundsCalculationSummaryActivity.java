package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.GoodsDetailsForARound;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView clothPlayerOneTextView;
    private TextView clothPlayerTwoTextView;
    private TextView spicePlayerOneTextView;
    private TextView spicePlayerTwoTextView;
    private TextView leatherPlayerOneTextView;
    private TextView leatherPlayerTwoTextView;
    private TextView threeTokenPlayerOneTextView;
    private TextView threeTokenPlayerTwoTextView;
    private TextView fourTokenPlayerOneTextView;
    private TextView fourTokenPlayerTwoTextView;
    private TextView fiveTokenPlayerOneTextView;
    private TextView fiveTokenPlayerTwoTextView;
    private TextView camelTokenPlayerOneTextView;
    private TextView camelTokenPlayerTwoTextView;
    private TextView sumPlayerOneTextView;
    private TextView sumPlayerTwoTextView;
    private long winnerOfRound;
    private GamesAndRoundsRepository gamesAndRoundsRepository;
    private boolean isTie = false;

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

        /*if (gameDetails.getGoodsDetailsForARoundMap() == null)
            gameDetails.setGoodsDetailsForARoundMap(new HashMap<>());
        if (gameDetails.getGoodsDetailsForARoundMap().isEmpty() || !gameDetails.getGoodsDetailsForARoundMap().containsKey(gameDetails.getRoundInProgress()) || gameDetails.getGoodsDetailsForARoundMap().get(gameDetails.getRoundInProgress()) == null)
           */

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

        if (gameDetails.getPlayerOneRounds().isEmpty() || !gameDetails.getPlayerOneRounds().containsKey(gameDetails.getRoundInProgress()) || gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()) == null) {
            gameDetails.getPlayerOneRounds().put(gameDetails.getRoundInProgress(), initializeRoundWithPlayer(gameDetails.getPlayersInAGame().getPlayerOne().getId()));
        }

        if (gameDetails.getPlayerTwoRounds().isEmpty() || !gameDetails.getPlayerTwoRounds().containsKey(gameDetails.getRoundInProgress()) || gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()) == null) {
            gameDetails.getPlayerTwoRounds().put(gameDetails.getRoundInProgress(), initializeRoundWithPlayer(gameDetails.getPlayersInAGame().getPlayerTwo().getId()));
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackOrHomeButtonClick();
        }
        else if (item.getItemId() == R.id.startNewGameButton) {
            handleResultOk();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        handleBackOrHomeButtonClick();
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

    private void handleBackOrHomeButtonClick() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setMessage(getString(R.string.alertdialog_backbutton_press_round_calc_msg));
        builder.setPositiveButton(getString(R.string.alertdialog_confirmation_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleCancelRequest();
            }
        });

        builder.setNegativeButton(getString(R.string.alertdialog_confirmation_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private String computeRoundTitle(int roundInProgress) {
        switch (roundInProgress) {
            case 1:
                return getString(R.string.gamesummary_roundone_title) + " Summary";
            case 2:
                return getString(R.string.gamesummary_roundtwo_title) + " Summary";
            case 3:
                return getString(R.string.gamesummary_roundthree_title) + " Summary";
            default:
                return "";
        }

    }

    private void handleException(boolean isExceptionOccurred) {
        Intent replyIntent = new Intent();
        if (isExceptionOccurred)
            replyIntent.putExtra(ApplicationConstants.EXCEPTION_DUE_TO_UNAVAILABILITY_OF_INTENT,"Y");
        replyIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    private void handleCancelRequest() {
        if (gameDetails.getPlayerOneRounds() != null)
            gameDetails.getPlayerOneRounds().put(gameDetails.getRoundInProgress(), null);
        if (gameDetails.getPlayerTwoRounds() != null)
            gameDetails.getPlayerTwoRounds().put(gameDetails.getRoundInProgress(), null);
        if (gameDetails.getGoodsDetailsForARoundMap() != null)
            gameDetails.getGoodsDetailsForARoundMap().put(gameDetails.getRoundInProgress(), null);
        if (gameDetails.getRoundWinners() != null )
            gameDetails.getRoundWinners().put(gameDetails.getRoundInProgress(), null);

        handleException(false);
    }

    private void initializeAllTextViewClickListeners() {
        diamondPlayerOneTextView = findViewById(R.id.playerOneDiamondTextView);
        diamondPlayerTwoTextView = findViewById(R.id.playerTwoDiamondTextView);
        goldPlayerOneTextView = findViewById(R.id.playerOneGoldTextView);
        goldPlayerTwoTextView = findViewById(R.id.playerTwoGoldTextView);
        silverPlayerOneTextView = findViewById(R.id.playerOneSilverTextView);
        silverPlayerTwoTextView = findViewById(R.id.playerTwoSilverTextView);
        clothPlayerOneTextView = findViewById(R.id.playerOneClothTextView);
        clothPlayerTwoTextView = findViewById(R.id.playerTwoClothTextView);
        spicePlayerOneTextView = findViewById(R.id.playerOneSpiceTextView);
        spicePlayerTwoTextView = findViewById(R.id.playerTwoSpiceTextView);
        leatherPlayerOneTextView = findViewById(R.id.playerOneLeatherTextView);
        leatherPlayerTwoTextView = findViewById(R.id.playerTwoLeatherTextView);
        threeTokenPlayerOneTextView = findViewById(R.id.playerOneThreeTokenTextView);
        threeTokenPlayerTwoTextView = findViewById(R.id.playerTwoThreeTokenTextView);
        fourTokenPlayerOneTextView = findViewById(R.id.playerOneFourTokenTextView);
        fourTokenPlayerTwoTextView = findViewById(R.id.playerTwoFourTokenTextView);
        fiveTokenPlayerOneTextView = findViewById(R.id.playerOneFiveTokenTextView);
        fiveTokenPlayerTwoTextView = findViewById(R.id.playerTwoFiveTokenTextView);
        camelTokenPlayerOneTextView = findViewById(R.id.playerOneCamelTokenTextView);
        camelTokenPlayerTwoTextView = findViewById(R.id.playerTwoCamelTokenTextView);
        sumPlayerOneTextView = findViewById(R.id.playerOneSumTextView);
        sumPlayerTwoTextView = findViewById(R.id.playerTwoSumTextView);

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
        clothPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);
            }
        });
        clothPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);
            }
        });
        spicePlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_SPICE_GOODS);
            }
        });
        spicePlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_SPICE_GOODS);
            }
        });
        leatherPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS);
            }
        });
        leatherPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS);
            }
        });
        threeTokenPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN);
            }
        });
        threeTokenPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN);
            }
        });
        fourTokenPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN);
            }
        });
        fourTokenPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN);
            }
        });
        fiveTokenPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN);
            }
        });
        fiveTokenPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN);
            }
        });
        camelTokenPlayerOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerOne().getId(), ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN);
            }
        });
        camelTokenPlayerTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTextViewClick(gameDetails.getPlayersInAGame().getPlayerTwo().getId(), ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN);
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

        sumPlayerOneTextView.setText(String.valueOf(computeScore(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()))));
        sumPlayerTwoTextView.setText(String.valueOf(computeScore(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()))));

        findWinnerOfRound();
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
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    clothPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getClothScore()));
                else
                    clothPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getClothScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                goodsDetailsForARound.setSpiceGoodsDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    spicePlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getSpiceScore()));
                else
                    spicePlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getSpiceScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                goodsDetailsForARound.setLeatherGoodsDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    leatherPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getLeatherScore()));
                else
                    leatherPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getLeatherScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                goodsDetailsForARound.setThreeTokenDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    threeTokenPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getThreeCardTokenScore()));
                else
                    threeTokenPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getThreeCardTokenScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                goodsDetailsForARound.setFourTokenDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    fourTokenPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getFourCardTokenScore()));
                else
                    fourTokenPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getFourCardTokenScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                goodsDetailsForARound.setFiveTokenDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    fiveTokenPlayerOneTextView.setText(String.valueOf(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getFiveCardTokenScore()));
                else
                    fiveTokenPlayerTwoTextView.setText(String.valueOf(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getFiveCardTokenScore()));
                break;
            case ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN:
                goodsDetailsForARound.setCamelTokenDetail(goodsDataReceived);
                if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                    char camelReceived = gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getCamelReceived();
                    camelTokenPlayerOneTextView.setText(camelReceived == 'Y' ? "5" : "0");
                }
                else {
                    char camelReceived = gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getCamelReceived();
                    camelTokenPlayerTwoTextView.setText(camelReceived == 'Y' ? "5" : "0");
                }
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
                return goodsDetailsForARound.getClothGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                return goodsDetailsForARound.getSpiceGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                return goodsDetailsForARound.getLeatherGoodsDetail();
            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                return goodsDetailsForARound.getThreeTokenDetail();
            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                return goodsDetailsForARound.getFourTokenDetail();
            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                return goodsDetailsForARound.getFiveTokenDetail();
            case ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN:
                return goodsDetailsForARound.getCamelTokenDetail();
            default:
                return "";
        }
    }

    private Round initializeRoundWithPlayer(long playerID) {
        Round round = new Round();
        round.setGameID(gameDetails.getGame().getId());
        round.setPlayerID(playerID);
        round.setRoundNumber(gameDetails.getRoundInProgress());
        round.setDiamondScore(0);
        round.setGoldScore(0);
        round.setSilverScore(0);
        round.setClothScore(0);
        round.setSpiceScore(0);
        round.setLeatherScore(0);
        round.setThreeCardTokenScore(0);
        round.setFourCardTokenScore(0);
        round.setFiveCardTokenScore(0);
        round.setCamelReceived('N');
        round.setTimeCreated(System.currentTimeMillis()/100);
        round.setTimeUpdated(System.currentTimeMillis()/100);
        round.setScore(0);
        return round;
    }

    private int computeScore(Round round) {
        int score = 0;
        score += round.getDiamondScore() + round.getGoldScore() + round.getSilverScore() + round.getClothScore() + round.getSpiceScore() + round.getLeatherScore()
                + round.getThreeCardTokenScore() + round.getFourCardTokenScore() + round.getFiveCardTokenScore();

        score += round.getCamelReceived() == 'Y' ? 5 : 0;

        round.setScore(score);

        return score;
    }

    private void findWinnerOfRound() {
        Round playerOneRound = gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress());
        Round playerTwoRound = gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress());

        ImageView playerOneSealOfExcellence = findViewById(R.id.winnerPlayerOneSealOfExcellence);
        ImageView playerTwoSealOfExcellence = findViewById(R.id.winnerPlayerTwoSealOfExcellence);

        if (playerOneRound.getScore() > playerTwoRound.getScore()) {
            playerOneSealOfExcellence.setVisibility(View.VISIBLE);
            playerTwoSealOfExcellence.setVisibility(View.INVISIBLE);
            winnerOfRound = gameDetails.getPlayersInAGame().getPlayerOne().getId();
        }
        else if (playerOneRound.getScore() < playerTwoRound.getScore()) {
            playerTwoSealOfExcellence.setVisibility(View.VISIBLE);
            playerOneSealOfExcellence.setVisibility(View.INVISIBLE);
            winnerOfRound = gameDetails.getPlayersInAGame().getPlayerTwo().getId();
        }
        else {
            winnerOfRound = GameUtils.resolveTieToFindWinner(gameDetails, goodsDetailsForARound);
            if (winnerOfRound == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                playerOneSealOfExcellence.setVisibility(View.VISIBLE);
                playerTwoSealOfExcellence.setVisibility(View.INVISIBLE);
            }
            else {
                playerTwoSealOfExcellence.setVisibility(View.VISIBLE);
                playerOneSealOfExcellence.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void handleResultOk() {
        Round playerOneRound;
        Round playerTwoRound;
        StringBuffer winMessage = new StringBuffer();
        if (gameDetails.getGoodsDetailsForARoundMap() == null)
            gameDetails.setGoodsDetailsForARoundMap(new HashMap<>());
        if (gameDetails.getGoodsDetailsForARoundMap().isEmpty() || !gameDetails.getGoodsDetailsForARoundMap().containsKey(gameDetails.getRoundInProgress()) || gameDetails.getGoodsDetailsForARoundMap().get(gameDetails.getRoundInProgress()) == null)
            gameDetails.getGoodsDetailsForARoundMap().put(gameDetails.getRoundInProgress(), goodsDetailsForARound);

        if (gameDetails.getRoundWinners() == null )
            gameDetails.setRoundWinners(new HashMap<>());
        if (gameDetails.getRoundWinners().isEmpty() || !gameDetails.getRoundWinners().containsKey(gameDetails.getRoundInProgress()) || gameDetails.getRoundWinners().get(gameDetails.getRoundInProgress()) == null) {
            Player winnerPlayer = winnerOfRound == gameDetails.getPlayersInAGame().getPlayerOne().getId() ? gameDetails.getPlayersInAGame().getPlayerOne() : gameDetails.getPlayersInAGame().getPlayerTwo();
            gameDetails.getRoundWinners().put(gameDetails.getRoundInProgress(), winnerPlayer);
        }

        if (winnerOfRound == gameDetails.getPlayersInAGame().getPlayerOne().getId())
            winMessage.append(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName())
                    .append(ApplicationConstants.SPACE)
                    .append(ApplicationConstants.WIN_MESSAGE_PART_1)
                    .append(String.valueOf(gameDetails.getRoundInProgress()));
        else
            winMessage.append(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName())
                    .append(ApplicationConstants.SPACE)
                    .append(ApplicationConstants.WIN_MESSAGE_PART_1)
                    .append(String.valueOf(gameDetails.getRoundInProgress()));

        if (gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()).getScore() == gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()).getScore()) {
            if (goodsDetailsForARound.getPlayerOneBonusTokens() != goodsDetailsForARound.getPlayerTwoBonusTokens())
                winMessage.append(ApplicationConstants.SPACE).append(ApplicationConstants.WIN_MESSAGE_PART_2_BY_BONUS);
            else
                winMessage.append(ApplicationConstants.SPACE).append(ApplicationConstants.WIN_MESSAGE_PART_2_BY_GOODS);
        }

        playerOneRound = gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress());
        playerTwoRound = gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress());
        gamesAndRoundsRepository = new GamesAndRoundsRepository(getApplication());

        gamesAndRoundsRepository.insertRound(playerOneRound);
        gamesAndRoundsRepository.insertRound(playerTwoRound);

        gameDetails.setRoundsCompleted(gameDetails.getRoundInProgress());

        Intent replyIntent = new Intent();
        replyIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        replyIntent.putExtra(ApplicationConstants.ROUND_CALC_SUMM_TO_GAME_SUMM_WIN_MESSAGE, winMessage.toString());
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
