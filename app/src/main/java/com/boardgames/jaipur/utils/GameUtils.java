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
import com.boardgames.jaipur.ui.rounds.DraggedItemsListViewModel;
import com.boardgames.jaipur.ui.rounds.DiamondRoundsCalculationFragment;
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
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void handleNextButtonClick(Activity activity, String goodsInDisplay) {

    }
}
