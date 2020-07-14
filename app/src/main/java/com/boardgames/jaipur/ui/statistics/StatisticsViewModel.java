package com.boardgames.jaipur.ui.statistics;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.repository.PlayerRepository;
import com.boardgames.jaipur.utils.PlayerStatistics;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {

    private GamesAndRoundsRepository gamesAndRoundsRepository;
    private PlayerRepository playerRepository;
    private Context context;
    private MediatorLiveData<List<PlayerStatistics>> playerStatisticsList;

   public StatisticsViewModel(Application application) {
        super(application);
       gamesAndRoundsRepository = new GamesAndRoundsRepository(application);
       playerRepository = new PlayerRepository(application);
       context = application.getApplicationContext();
    }

    public MutableLiveData<List<PlayerStatistics>> getAllPlayersStats() {

       if (playerStatisticsList == null)
           playerStatisticsList = new MediatorLiveData<>();

       LiveData<List<Player>> players = playerRepository.getAllPlayers();


       return playerStatisticsList;
    }

}