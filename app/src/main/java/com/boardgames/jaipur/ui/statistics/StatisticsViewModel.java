package com.boardgames.jaipur.ui.statistics;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.repository.PlayerRepository;
import com.boardgames.jaipur.utils.CustomPlayerGameLiveData;
import com.boardgames.jaipur.utils.PlayerStatistics;
import com.boardgames.jaipur.utils.PlayerUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsViewModel extends AndroidViewModel {

    private GamesAndRoundsRepository gamesAndRoundsRepository;
    private PlayerRepository playerRepository;
    private Context context;
    private LiveData<List<PlayerStatistics>> playerStatisticsList;
    private MutableLiveData<Integer> totalGamesPlayed;

   public StatisticsViewModel(Application application) {
        super(application);
       gamesAndRoundsRepository = new GamesAndRoundsRepository(application);
       playerRepository = new PlayerRepository(application);
       context = application.getApplicationContext();
    }

    public LiveData<Integer> getTotalGames() {
       if (totalGamesPlayed == null) {
           totalGamesPlayed = new MutableLiveData<>();
           totalGamesPlayed.setValue(0);
       }
       return totalGamesPlayed;
    }

    public LiveData<List<PlayerStatistics>> getAllPlayersStats() {

       if (playerStatisticsList == null)
           playerStatisticsList = new MediatorLiveData<>();

       LiveData<List<Player>> playerLiveDataList = playerRepository.getAllPlayers();
       LiveData<List<Game>> gameLiveDataList = gamesAndRoundsRepository.getAllGames();
       CustomPlayerGameLiveData customPlayerGameLiveData = new CustomPlayerGameLiveData(playerLiveDataList, gameLiveDataList);
       playerStatisticsList = Transformations.switchMap(customPlayerGameLiveData, input -> {
            return getPlayerStats(input.first, input.second);
        });

       return playerStatisticsList;
    }

    private MutableLiveData<List<PlayerStatistics>> getPlayerStats(List<Player> playerList, List<Game> gameList) {
       MutableLiveData<List<PlayerStatistics>> playerStatsLiveData = new MutableLiveData<>();
       ArrayList<PlayerStatistics> playerStatisticsArrayList = new ArrayList<>();
        HashMap<Long, PlayerStatistics> playerStatisticsHashMap = new HashMap<>();

       if ((playerList == null || playerList.isEmpty())) {
           playerStatsLiveData.setValue(playerStatisticsArrayList);
           return playerStatsLiveData;
       }

       for (Player player : playerList) {
           PlayerStatistics playerStatistics = new PlayerStatistics();
           playerStatistics.setPlayer(player);
           if (player.getPlayerAvatar() == null || player.getPlayerAvatar().equalsIgnoreCase(""))
               playerStatistics.setPlayerProfile(PlayerUtils.getUriForDrawable(context, R.drawable.default_player_avatar));
           else {
               playerStatistics.setPlayerProfile(Uri.fromFile(new File(player.getPlayerAvatar())));
           }
           playerStatistics.setGamesPlayed(0L);
           playerStatistics.setGamesWon(0L);
           if (!playerStatisticsHashMap.containsKey(player.getId()))
               playerStatisticsHashMap.put(player.getId(), playerStatistics);
       }

       if (gameList != null) {
           int gamesTotal = 0;
           for (Game game : gameList) {
               if (!playerStatisticsHashMap.containsKey(game.getPlayerOneID()) || !playerStatisticsHashMap.containsKey(game.getPlayerTwoID())) {
                   continue;
               }
               gamesTotal++;
               playerStatisticsHashMap.get(game.getPlayerOneID()).setGamesPlayed(playerStatisticsHashMap.get(game.getPlayerOneID()).getGamesPlayed() + 1);
               playerStatisticsHashMap.get(game.getPlayerTwoID()).setGamesPlayed(playerStatisticsHashMap.get(game.getPlayerTwoID()).getGamesPlayed() + 1);
               playerStatisticsHashMap.get(game.getWinner()).setGamesWon(playerStatisticsHashMap.get(game.getWinner()).getGamesWon() + 1);
           }
           totalGamesPlayed.setValue(gamesTotal);
       }

       for (Map.Entry<Long, PlayerStatistics> entry : playerStatisticsHashMap.entrySet()) {
           playerStatisticsArrayList.add(entry.getValue());
       }

       playerStatsLiveData.setValue(playerStatisticsArrayList);

       return playerStatsLiveData;
    }

}