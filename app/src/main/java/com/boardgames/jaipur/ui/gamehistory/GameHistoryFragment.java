package com.boardgames.jaipur.ui.gamehistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.GamesItemAdapter;
import com.boardgames.jaipur.utils.GameDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class GameHistoryFragment extends Fragment {

    private GameHistoryViewModel gameHistoryViewModel;
    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gameHistoryViewModel = new ViewModelProvider(this).get(GameHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_game_history, container, false);

        RecyclerView gameRecyclerView = root.findViewById(R.id.gameHistoryRecyclerView);
        final GamesItemAdapter gamesItemAdapter = new GamesItemAdapter(this);
        gameRecyclerView.setAdapter(gamesItemAdapter);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gameHistoryViewModel.getAllGames().observe(getViewLifecycleOwner(), new Observer<List<GameDetails>>() {
            @Override
            public void onChanged(List<GameDetails> gameDetails) {
                gamesItemAdapter.setGamesList(gameDetails);
            }
        });

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = root.findViewById(R.id.gameHistoryAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return root;
    }
}
