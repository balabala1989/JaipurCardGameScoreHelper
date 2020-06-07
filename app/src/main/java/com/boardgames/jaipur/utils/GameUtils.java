package com.boardgames.jaipur.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boardgames.jaipur.R;
import com.bumptech.glide.Glide;

import androidx.fragment.app.Fragment;


public class GameUtils {

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
}
