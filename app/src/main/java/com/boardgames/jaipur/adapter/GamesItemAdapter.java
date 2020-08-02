package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.ui.gamehistory.GameDetailsActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class GamesItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class GameViewHolder extends RecyclerView.ViewHolder {
        private final TextView playersNameTextView;
        private final TextView gameDateTextView;
        private final ImageView playerOneImageView;
        private final ImageView playerTwoImageView;
        private final TextView playerOneNameTextView;
        private final TextView playerTwoNameTextView;
        private final FrameLayout playerOneWinnerFrameLayout;
        private final FrameLayout playerTwoWinnerFrameLayout;
        private final ConstraintLayout itemLayout;

        private GameViewHolder(View view) {
            super(view);
            playersNameTextView = view.findViewById(R.id.playersNameTextView);
            gameDateTextView = view.findViewById(R.id.gameDateTextView);
            playerOneImageView = view.findViewById(R.id.playerOneImageView);
            playerTwoImageView = view.findViewById(R.id.playerTwoImageView);
            playerOneNameTextView = view.findViewById(R.id.playerOnenameTextView);
            playerTwoNameTextView = view.findViewById(R.id.playerTwoNameTextView);
            playerOneWinnerFrameLayout = view.findViewById(R.id.playerOneWinnerFrameLayout);
            playerTwoWinnerFrameLayout = view.findViewById(R.id.playerTwoWinnerFrameLayout);
            itemLayout = view.findViewById(R.id.itemHolderLayout);
        }
    }

    class EmptyGameViewHolder extends RecyclerView.ViewHolder {
        private EmptyGameViewHolder(View view) {
            super(view);
        }
    }

    private final LayoutInflater inflater;
    private List<GameDetails> gamesList;
    private Context contextObj;
    private boolean isPermissionGranted;
    private Fragment parentFragment;
    public static final int EMPTY_VIEW = 10;

    public GamesItemAdapter(Fragment fragment) {
        parentFragment = fragment;
        contextObj = fragment.getContext();
        inflater = LayoutInflater.from(contextObj);
        isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(contextObj);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == EMPTY_VIEW) {
            view = inflater.inflate(R.layout.recycle_empty_game_view, parent, false);
            return new EmptyGameViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.recycleview_game_item, parent, false);
            return new GameViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GameViewHolder) {
            GameViewHolder gameViewHolder = (GameViewHolder) holder;
            GameDetails gameDetails = gamesList.get(position);
            gameViewHolder.playersNameTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName() + " vs " + gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());
            gameViewHolder.gameDateTextView.setText(GameUtils.convertTimeToDate(gameDetails.getGame().getTimeCreated()));
            Glide.with(contextObj).load(gameDetails.getPlayersInAGame().getPlayerOneProfile()).into(gameViewHolder.playerOneImageView);
            Glide.with(contextObj).load(gameDetails.getPlayersInAGame().getPlayerTwoProfile()).into(gameViewHolder.playerTwoImageView);
            gameViewHolder.playerOneNameTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());
            gameViewHolder.playerTwoNameTextView.setText(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());
            if (gameDetails.getGame().getWinner() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                gameViewHolder.playerOneWinnerFrameLayout.setVisibility(View.VISIBLE);
                gameViewHolder.playerTwoWinnerFrameLayout.setVisibility(View.INVISIBLE);
            }
            else {
                gameViewHolder.playerOneWinnerFrameLayout.setVisibility(View.INVISIBLE);
                gameViewHolder.playerTwoWinnerFrameLayout.setVisibility(View.VISIBLE);
            }

            gameViewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contextObj, GameDetailsActivity.class);
                    intent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
                    parentFragment.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (gamesList != null) {
            return gamesList.size() > 0 ? gamesList.size() : 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (gamesList.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setGamesList(List<GameDetails> gamesList) {
        this.gamesList = gamesList;
        notifyDataSetChanged();
    }
}
