package com.boardgames.jaipur.utils;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;

import java.util.HashMap;
import java.util.List;

public class CustomPlayerGameLiveData extends MediatorLiveData<Pair<List<Player>, List<Game>>> {


    public CustomPlayerGameLiveData(LiveData<List<Player>> playersList, LiveData<List<Game>> gamesList) {
        addSource(playersList, players -> {
            setValue(Pair.create(players, gamesList.getValue()));
        });

        addSource(gamesList, games -> {
            setValue(Pair.create(playersList.getValue(), games));
        });
    }
}
