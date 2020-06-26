package com.boardgames.jaipur.ui.rounds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.ItemListAdapter;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ItemsDisplayActivity extends AppCompatActivity {

    private HashMap<String, Long> goodsToPlayer;
    private GameDetails gameDetails;
    private String goodsNameReceived;
    private String goodsDataReceived;
    private long selectedPlayerID;
    private ArrayList<String> goodsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_display);

        Intent receivedIntent = getIntent();
        if (receivedIntent == null)
            handleException(true);

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(computeRoundTitle());

        goodsNameReceived = receivedIntent.getStringExtra(ApplicationConstants.GOODS_NAME_FROM_SUMMARY_TO_ITEM_DETAILS);
        goodsDataReceived = receivedIntent.getStringExtra(ApplicationConstants.GOODS_DATA_FROM_SUMMARY_TO_ITEM_DETAILS);
        selectedPlayerID = receivedIntent.getIntExtra(ApplicationConstants.SELECTED_PLAYER_FROM_SUMMARY_TO_ITEM_DETAILS, (int) ApplicationConstants.DEFAULT_PLAYER_ID);
        if (selectedPlayerID == ApplicationConstants.DEFAULT_PLAYER_ID)
            handleException(true);

        initializeGoodToPlayer();

        for (Map.Entry<String, Long> entry : goodsToPlayer.entrySet()) {
            goodsData.add(entry.getKey() + ApplicationConstants.SEPARATOR_OF_VIEWS + String.valueOf(entry.getValue()));
        }

        Collections.sort(goodsData);
        RecyclerView itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        final ItemListAdapter itemListAdapter = new ItemListAdapter(getApplicationContext(), goodsToPlayer, goodsData, goodsNameReceived, selectedPlayerID, gameDetails);
        itemsRecyclerView.setAdapter(itemListAdapter);
        itemsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_player_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackOrHomeButtonClick();
        }
        else if (item.getItemId() == R.id.addPlayerSaveButton) {
            handleResultOk();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        handleBackOrHomeButtonClick();
    }

    private void handleBackOrHomeButtonClick() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setMessage(getString(R.string.alertdialog_backbutton_press_item_calc_msg));
        builder.setPositiveButton(getString(R.string.alertdialog_confirmation_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleException(false);
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

    private void handleResultOk() {
        int score = 0;
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, Long> entry : goodsToPlayer.entrySet()) {
            if (entry.getValue() != ApplicationConstants.DEFAULT_PLAYER_ID)
                buffer.append(entry.getKey())
                        .append(ApplicationConstants.SEPARATOR_OF_VIEWS)
                        .append(String.valueOf(entry.getValue()))
                        .append(ApplicationConstants.SEPARATOR_OF_DATA);
            if (entry.getValue() == selectedPlayerID)
                score += GameUtils.goodsToItemsToScore.get(goodsNameReceived).get(entry.getKey());
        }

        if (selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            setScoreForPlayer(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()), score);
        }
        else {
            setScoreForPlayer(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()), score);
        }

        goodsDataReceived = buffer.toString();
        Intent replyIntent = new Intent();
        replyIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        replyIntent.putExtra(ApplicationConstants.GOODS_DATA_FROM_SUMMARY_TO_ITEM_DETAILS, goodsDataReceived.substring(0, goodsDataReceived.length() - 1));
        replyIntent.putExtra(ApplicationConstants.GOODS_NAME_FROM_SUMMARY_TO_ITEM_DETAILS, goodsNameReceived);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void setScoreForPlayer(Round round, int score) {
        switch (goodsNameReceived) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
                round.setDiamondScore(score);
            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                round.setGoldScore(score);
            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                round.setSilverScore(score);
            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                round.setClothScore(score);
            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                round.setSpiceScore(score);
            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                round.setLeatherScore(score);
            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                round.setThreeCardTokenScore(score);
            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                round.setFourCardTokenScore(score);
            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                round.setFiveCardTokenScore(score);
            case ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN:
                round.setCamelReceived('Y');
            default:
                return;
        }
    }

    private void handleException(boolean isExceptionOccurred) {
        Intent replyIntent = new Intent();
        if (isExceptionOccurred)
            replyIntent.putExtra(ApplicationConstants.EXCEPTION_DUE_TO_UNAVAILABILITY_OF_INTENT,"Y");
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    private void initializeGoodToPlayer() {
        //Initialize the map with -1 indicating no player assigned
        String items = GameUtils.goodsNameToGoodsItems.get(goodsNameReceived);
        StringTokenizer tokenizer = new StringTokenizer(items,ApplicationConstants.SEPARATOR_OF_VIEWS);
        while (tokenizer.hasMoreTokens()) {
            goodsToPlayer.put(tokenizer.nextToken(), (long) -1);
        }

        if (goodsDataReceived == null || goodsDataReceived.trim().isEmpty())
            return;

        //Initialize the map with goods to player mapping
        tokenizer = new StringTokenizer(goodsDataReceived, ApplicationConstants.SEPARATOR_OF_DATA);
        while (tokenizer.hasMoreTokens()) {
            String[] splitData = tokenizer.nextToken().trim().split(ApplicationConstants.SEPARATOR_OF_VIEWS);
            goodsToPlayer.put(splitData[0],Long.parseLong(splitData[1]));
        }
    }

    private String computeRoundTitle() {
        String roundTitle;
        switch(gameDetails.getRoundInProgress()) {
            case 1:
                roundTitle = getString(R.string.gamesummary_roundone_title) + ApplicationConstants.HYPEN;
            case 2:
                roundTitle = getString(R.string.gamesummary_roundtwo_title) + ApplicationConstants.HYPEN;
            case 3:
                roundTitle = getString(R.string.gamesummary_roundthree_title) + ApplicationConstants.HYPEN;
            default:
                roundTitle = "";
        }

        switch(goodsNameReceived) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
                roundTitle += getString(R.string.diamond_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                roundTitle += getString(R.string.gold_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                roundTitle += getString(R.string.silver_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                roundTitle += getString(R.string.cloth_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                roundTitle += getString(R.string.spice_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                roundTitle += getString(R.string.leather_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                roundTitle += getString(R.string.three_card_token_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                roundTitle += getString(R.string.four_card_token_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                roundTitle += getString(R.string.five_card_token_calculation_fragment_label);
            case ApplicationConstants.ROUNDS_CALC_CAMEL_TOKEN:
                roundTitle += getString(R.string.camel_token_calculation_fragment_label);
            default:
                roundTitle = "";
        }

        return roundTitle;
    }
}
