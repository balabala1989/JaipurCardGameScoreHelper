package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;

import java.util.List;

public class PlayerListAdapater extends RecyclerView.Adapter<PlayerListAdapater.PlayerViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerItemTextView;

        private PlayerViewHolder(View view) {
            super(view);
            playerItemTextView = view.findViewById(R.id.playersListTextView);
        }
    }

    private final LayoutInflater inflater;
    private List<Player> playersList;

    public PlayerListAdapater(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_players_list, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        if(playersList != null) {
            Player player = playersList.get(position);
            holder.playerItemTextView.setText(player.getPlayerName());
        }
        else {
            holder.playerItemTextView.setText("No Players Found....");
        }

    }

    public void setPlayersList(List<Player> players) {
        playersList = players;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (playersList != null) {
            return playersList.size();
        }
        return 0;
    }


}
