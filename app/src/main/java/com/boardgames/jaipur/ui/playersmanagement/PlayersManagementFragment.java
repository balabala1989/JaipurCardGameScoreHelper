package com.boardgames.jaipur.ui.playersmanagement;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerListAdapater;
import com.boardgames.jaipur.entities.Player;

import java.util.List;

public class PlayersManagementFragment extends Fragment {

    private PlayersManagementViewModel playersManagementViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playersManagementViewModel =
                new ViewModelProvider(this).get(PlayersManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_players_management, container, false);
        RecyclerView playerRecyclerView = root.findViewById(R.layout.recyclerview_players_list);
        final PlayerListAdapater adapater = new PlayerListAdapater(getContext());
        playerRecyclerView.setAdapter(adapater);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playersManagementViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                adapater.setPlayersList(players);
            }
        });
        return root;
    }

}
