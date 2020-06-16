package com.boardgames.jaipur.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.DraggedItemListAdapter;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.ui.rounds.DraggedItemsListViewModel;
import com.boardgames.jaipur.ui.rounds.DiamondRoundsCalculationFragment;
import com.boardgames.jaipur.ui.rounds.GoldRoundsCalculationFragment;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationActivity;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class GameUtils {

    //Holds the value of the coins for the dragshadow used in Round Calculation
    public static final HashMap<Integer, Integer> dragShadowResourceToValue;
    private static MaterialStyledDialog dialog;


    static  {
        dragShadowResourceToValue = new HashMap<Integer, Integer>();
        dragShadowResourceToValue.put(R.drawable.a1_2_diamonds_77_drag_shadow, 14);
        dragShadowResourceToValue.put(R.drawable.a1_4_diamonds_5_drag_shadow, 5);
        dragShadowResourceToValue.put(R.drawable.a2_2_gold_66_drag_shadow, 12);
        dragShadowResourceToValue.put(R.drawable.a2_4_gold_5_drag_shadow, 5);
    }

    public static void loadPlayerDetailsInDisplay(Fragment parentFragment, View fragmentView, GameDetails gameDetails) {
        int width = PlayerUtils.getWidthforImageViewByOneThird(parentFragment.getActivity());
        View roundsDisplayView = fragmentView.findViewById(R.id.roundUsersDisplay);
        ImageView playerOneImageView = roundsDisplayView.findViewById(R.id.playerOneImageView);
        ImageView playerTwoImageView = roundsDisplayView.findViewById(R.id.playerTwoImageView);
        TextView playerOneTextView = roundsDisplayView.findViewById(R.id.playerOneTextView);
        TextView playerTwoTextView = roundsDisplayView.findViewById(R.id.playerTwoTextView);

        playerOneTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());
        playerTwoTextView.setText(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());

        Glide.with(parentFragment.getContext())
                .load(gameDetails.getPlayersInAGame().getPlayerOneProfile())
                .override(width, width)
                .into(playerOneImageView);
        Glide.with(parentFragment.getContext())
                .load(gameDetails.getPlayersInAGame().getPlayerTwoProfile())
                .override(width, width)
                .into(playerTwoImageView);
    }

    public static void computeScoreForDraggedItem(Activity mainActivity, long playerID, int draggedItemID, String playerName, String keyForFragmentData, ArrayList<String> dragAndDropOrder) {
        HashMap<Long, Integer> playersScore;
        StringBuffer stringBuffer = new StringBuffer();
        RoundsCalculationActivity roundsCalculationActivity = (RoundsCalculationActivity) mainActivity;
        if (roundsCalculationActivity.getGoodsToPlayersScore() == null ||
                roundsCalculationActivity.getGoodsToPlayersScore().isEmpty()) {
            HashMap<String, HashMap<Long, Integer>> goodsToPlayersScore = new HashMap<>();
            roundsCalculationActivity.setGoodsToPlayersScore(goodsToPlayersScore);
        }

        if (!roundsCalculationActivity.getGoodsToPlayersScore().containsKey(keyForFragmentData)) {
            playersScore = new HashMap<>();
            roundsCalculationActivity.getGoodsToPlayersScore().put(keyForFragmentData, playersScore);
        }
        playersScore = roundsCalculationActivity.getGoodsToPlayersScore().get(keyForFragmentData);
        if (!playersScore.containsKey(playerID))
            playersScore.put(playerID, 0);
        playersScore.put(playerID, playersScore.get(playerID) + GameUtils.dragShadowResourceToValue.get(draggedItemID));
        stringBuffer.append(String.valueOf(playerID))
                .append(ApplicationConstants.SEPARATOR_OF_VIEWS)
                .append(playerName)
                .append(ApplicationConstants.SEPARATOR_OF_VIEWS)
                .append(String.valueOf(draggedItemID));
        dragAndDropOrder.add(stringBuffer.toString());
    }

    public static void displayDraggedItemsForRemoval(Activity activity, Context context, Fragment parentFragment, ArrayList<String> dragAndDropOrder, String goodsInDisplay) {


        DraggedItemsListViewModel draggedItemsListViewModel = ((RoundsCalculationActivity) activity).getDraggedItemsListViewModel();
        draggedItemsListViewModel.setDragAndDroppedOrder(dragAndDropOrder);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_remove_dragged_item_alertdialog, null);
        RecyclerView draggedListView = dialogView.findViewById(R.id.draggedItemsRecyclerView);
        final DraggedItemListAdapter listAdapter = new DraggedItemListAdapter(activity, context, dialogView, draggedItemsListViewModel, goodsInDisplay);
        draggedListView.setAdapter(listAdapter);
        draggedListView.setLayoutManager( new LinearLayoutManager(context));

        MaterialStyledDialog.Builder builder = new MaterialStyledDialog.Builder(context);
        builder.setTitle(R.string.alert_title_for_dropped_items);
        builder.setCustomView(dialogView, 0, 0, 0, 0);
        builder.setScrollable(Boolean.TRUE);
        builder.setCancelable(Boolean.FALSE);
        builder.setHeaderColor(R.color.defaultColor);
        builder.setIcon(R.drawable.delete_item);
        dialog = builder.build();




        draggedItemsListViewModel.getDraggedItemList().observe(parentFragment, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> dragAndDropOrder) {
                ArrayList<String> reversedList = new ArrayList<>(dragAndDropOrder);
                Collections.reverse(reversedList);
                listAdapter.setDragAndDropOrder(reversedList);
            }
        });

       Button closeButton = (Button) dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (parentFragment instanceof DiamondRoundsCalculationFragment)
                   ((DiamondRoundsCalculationFragment) parentFragment).handleAlertDialogCloseButton();
               else if (parentFragment instanceof GoldRoundsCalculationFragment)
                   ((GoldRoundsCalculationFragment) parentFragment).handleAlertDialogCloseButton();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void handleNextButtonClick(Activity activity, String goodsInDisplay) {
        RoundsCalculationActivity roundsCalculationActivity = (RoundsCalculationActivity)activity;
        if (!roundsCalculationActivity.getGoodsToPlayersScore().containsKey(goodsInDisplay))
            roundsCalculationActivity.handleException();

        GameDetails gameDetails = roundsCalculationActivity.getGameDetails();

        //Calculate first player scores
        if (gameDetails.getPlayerOneRounds() == null || gameDetails.getPlayerOneRounds().isEmpty()) {
            HashMap<Integer, Round> playerOneRounds = new HashMap<>();
            gameDetails.setPlayerOneRounds(playerOneRounds);
        }

        if (!gameDetails.getPlayerOneRounds().containsKey(gameDetails.getRoundInProgress())) {
            gameDetails.getPlayerOneRounds().put(gameDetails.getRoundInProgress(), new Round());
        }

        Round playerOneRoundDetails = gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress());

        setScoresToGameDetails(roundsCalculationActivity.getGoodsToPlayersScore(),
                playerOneRoundDetails,
                gameDetails,
                gameDetails.getPlayersInAGame().getPlayerOne().getId(),
                goodsInDisplay);

        //Calculate second player scores
        if (gameDetails.getPlayerTwoRounds() == null || gameDetails.getPlayerTwoRounds().isEmpty()) {
            HashMap<Integer, Round> playerTwoRounds = new HashMap<>();
            gameDetails.setPlayerTwoRounds(playerTwoRounds);
        }

        if (!gameDetails.getPlayerTwoRounds().containsKey(gameDetails.getRoundInProgress())) {
            gameDetails.getPlayerTwoRounds().put(gameDetails.getRoundInProgress(), new Round());
        }

        Round playerTwoRoundDetails = gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress());

        setScoresToGameDetails(roundsCalculationActivity.getGoodsToPlayersScore(),
                playerTwoRoundDetails,
                gameDetails,
                gameDetails.getPlayersInAGame().getPlayerTwo().getId(),
                goodsInDisplay);
    }

   private static void setScoresToGameDetails(HashMap<String, HashMap<Long, Integer>> goodsToPlayerScore, Round round, GameDetails gameDetails, long playerID, String goodsInDisplay) {
        if (goodsToPlayerScore == null ||
                !goodsToPlayerScore.containsKey(goodsInDisplay) ||
                goodsToPlayerScore.get(goodsInDisplay) == null ||
                !goodsToPlayerScore.get(goodsInDisplay).containsKey(playerID))
            return;
        HashMap<Long, Integer> playerScores = goodsToPlayerScore.get(goodsInDisplay);
        int playerScoreInGoods = playerScores.get(playerID);
        if (round.getPlayerID() != playerID) {
            round.setGameID(gameDetails.getGame().getId());
            round.setPlayerID(playerID);
            round.setRoundNumber(gameDetails.getRoundInProgress());
        }

        setScore(round, playerScoreInGoods, goodsInDisplay);
    }

    private static void setScore(Round round, int score, String goodsInDisplay) {
        switch (goodsInDisplay) {
            case ApplicationConstants.ROUNDS_CALC_DIAMOND_GOODS:
                round.setDiamondScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_GOLD_GOODS:
                round.setGoldScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_SILVER_GOODS:
                round.setSilverScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS:
                round.setClothScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_SPICE_GOODS:
                round.setSpiceScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_LEATHER_GOODS:
                round.setLeatherScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_3_CARD_TOKEN:
                round.setThreeCardTokenScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_4_CARD_TOKEN:
                round.setFourCardTokenScore(score);
                break;

            case ApplicationConstants.ROUNDS_CALC_5_CARD_TOKEN:
                round.setFiveCardTokenScore(score);
                break;
        }
    }

    public static void resetOrInitializeActivityVars(Activity mainActivity, String goodsInDisplay) {
        RoundsCalculationActivity roundsCalculationActivity = (RoundsCalculationActivity) mainActivity;
        if (roundsCalculationActivity.getGoodsToPlayersScore() != null && roundsCalculationActivity.getGoodsToPlayersScore().containsKey(goodsInDisplay)) {
            roundsCalculationActivity.getGoodsToPlayersScore().put(goodsInDisplay, new HashMap<Long, Integer>());
        }

        //Resetting gamedetails
        GameDetails gameDetails = roundsCalculationActivity.getGameDetails();

        if (gameDetails.getPlayerOneRounds() != null && gameDetails.getPlayerOneRounds().containsKey(gameDetails.getRoundInProgress())) {
            setScore(gameDetails.getPlayerOneRounds().get(gameDetails.getRoundInProgress()), 0, goodsInDisplay);
        }

        if (gameDetails.getPlayerTwoRounds() != null && gameDetails.getPlayerTwoRounds().containsKey(gameDetails.getRoundInProgress())) {
            setScore(gameDetails.getPlayerTwoRounds().get(gameDetails.getRoundInProgress()), 0, goodsInDisplay);
        }
    }
}
