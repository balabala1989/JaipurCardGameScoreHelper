package com.boardgames.jaipur.ui.gamehistory;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.repository.PlayerRepository;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.boardgames.jaipur.utils.PlayersInAGame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryViewModel extends AndroidViewModel {

    private List<GameDetails> gameDetailsList;
    private GamesAndRoundsRepository gamesAndRoundsRepository;
    private PlayerRepository playerRepository;
    private Context context;

    public GameHistoryViewModel(Application application) {
        super(application);
        gamesAndRoundsRepository = new GamesAndRoundsRepository(application);
        playerRepository = new PlayerRepository(application);
        gameDetailsList = new ArrayList<>();
        context = application.getApplicationContext();
    }

    public List<GameDetails> getAllGames() {
        List<Game> games = gamesAndRoundsRepository.getAllGames();

        for (Game game : games) {
            GameDetails gameDetails = new GameDetails();
            gameDetails.setGame(game);
            Player playerOne = playerRepository.getPlayer(game.getPlayerOneID());
            Player playerTwo = playerRepository.getPlayer(game.getPlayerTwoID());

            PlayersInAGame playersInAGame = new PlayersInAGame(playerOne, playerTwo);

            if (playerOne.getPlayerAvatar() == null || playerOne.getPlayerAvatar().trim().isEmpty())
                playersInAGame.setPlayerOneProfile(PlayerUtils.getUriForDrawable(context, R.drawable.default_player_avatar));
            else
                playersInAGame.setPlayerOneProfile(Uri.fromFile(new File(playerOne.getPlayerAvatar())));

            if (playerTwo.getPlayerAvatar() == null || playerTwo.getPlayerAvatar().trim().isEmpty())
                playersInAGame.setPlayerTwoProfile(PlayerUtils.getUriForDrawable(context, R.drawable.default_player_avatar));
            else
                playersInAGame.setPlayerTwoProfile(Uri.fromFile(new File(playerTwo.getPlayerAvatar())));

            gameDetails.setGame(game);
            gameDetails.setPlayersInAGame(playersInAGame);

            gameDetailsList.add(gameDetails);

        }
        return gameDetailsList;
    }


}