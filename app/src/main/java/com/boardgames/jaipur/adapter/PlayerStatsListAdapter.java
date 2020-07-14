package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.PlayerStatistics;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class PlayerStatsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class PlayerStatsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView playerImageView;
        private final TextView playerNameTextView;
        private final TextView playerWinPercentageTextView;
        private final TextView playerGamesPlayedTextView;

        private PlayerStatsViewHolder(View view) {
            super(view);
            playerImageView = view.findViewById(R.id.playerImageView);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
            playerWinPercentageTextView = view.findViewById(R.id.playerWinPercentage);
            playerGamesPlayedTextView = view.findViewById(R.id.playerGamesPlayedTextView);
        }
    }

    class EmptyPlayerStatsViewHolder extends RecyclerView.ViewHolder {
        private final TextView emptyTextView;

        private EmptyPlayerStatsViewHolder(View view) {
            super(view);
            emptyTextView = view.findViewById(R.id.emptyGameTextView);
        }
    }

    private final LayoutInflater inflater;
    private List<PlayerStatistics> playerStatisticsList;
    private Context contextObj;
    private boolean isPermissionGranted;
    private Fragment parentFragment;
    public static final int EMPTY_VIEW = 10;

    public PlayerStatsListAdapter(Fragment fragment) {
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
            view = inflater.inflate(R.layout.layout_player_stats_item, parent, false);
            return new PlayerStatsViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.recycle_empty_game_view, parent, false);
            return new EmptyPlayerStatsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlayerStatsViewHolder) {
            PlayerStatsViewHolder playerStatsViewHolder = (PlayerStatsViewHolder) holder;
            PlayerStatistics playerStatistics = playerStatisticsList.get(position);
            Glide.with(contextObj).load(playerStatistics.getPlayerProfile()).into(playerStatsViewHolder.playerImageView);
            playerStatsViewHolder.playerNameTextView.setText(playerStatistics.getPlayer().getPlayerName());
            double winPercentage = playerStatistics.getGamesWon() / playerStatistics.getGamesPlayed();
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            playerStatsViewHolder.playerWinPercentageTextView.setText(decimalFormat.format(winPercentage));
            playerStatsViewHolder.playerGamesPlayedTextView.setText(String.valueOf(playerStatistics.getGamesPlayed()));
        }
        else if (holder instanceof EmptyPlayerStatsViewHolder) {
            EmptyPlayerStatsViewHolder emptyPlayerStatsViewHolder = (EmptyPlayerStatsViewHolder) holder;
            emptyPlayerStatsViewHolder.emptyTextView.setText(ApplicationConstants.EMPTY_STATS_MESSAGE);
        }
    }

    @Override
    public int getItemCount() {
        if (playerStatisticsList != null) {
            return playerStatisticsList.size() > 0 ? playerStatisticsList.size() : 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (playerStatisticsList.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setPlayerStatisticsList(List<PlayerStatistics> playerStatisticsList) {
        this.playerStatisticsList = playerStatisticsList;
        notifyDataSetChanged();
    }
}
