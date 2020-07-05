package com.boardgames.jaipur.ui.gamehistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.GamesItemAdapter;

public class GameHistoryFragment extends Fragment {

    private GameHistoryViewModel gameHistoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gameHistoryViewModel = new ViewModelProvider(this).get(GameHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_game_history, container, false);

        RecyclerView gameRecyclerView = root.findViewById(R.id.gameHistoryRecyclerView);
        final GamesItemAdapter gamesItemAdapter = new GamesItemAdapter(this);
        gameRecyclerView.setAdapter(gamesItemAdapter);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesItemAdapter.setGamesList(gameHistoryViewModel.getAllGames());
        return root;
    }
}
