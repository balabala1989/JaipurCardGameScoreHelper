package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.boardgames.jaipur.utils.PlayersInAGame;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class StartingPlayerActivity extends AppCompatActivity {

    private Player startingPlayer;
    private int countOfHandler = 0;
    private Button tryAgainButton;
    private NewGameViewModel newGameViewModel;
    private long gameId;
    private Game game;
    private PlayersInAGame playersInAGame;
    private GameDetails gameDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_player);

        newGameViewModel = new ViewModelProvider(this).get(NewGameViewModel.class);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.starting_player_activity_title));

        Intent receivedIntent = getIntent();

        if (receivedIntent == null) {
            handleException();
        }

        playersInAGame = receivedIntent.getParcelableExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERS_IN_A_GAME);

        if (playersInAGame == null || playersInAGame.getPlayerOne() == null || playersInAGame.getPlayerTwo() == null)
            handleException();

        tryAgainButton = (Button) findViewById(R.id.retryButton);
        tryAgainButton.setVisibility(View.INVISIBLE);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countOfHandler = 0;
                tryAgainButton.setVisibility(View.INVISIBLE);
                findStartingPlayer();
            }
        });
        findStartingPlayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent replyTent = new Intent();
       if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED, replyTent);
            finish();
            return true;
        }
       else if (item.getItemId() == R.id.startNewGameButton) {
           if (playersInAGame == null || playersInAGame.getPlayerOne() == null || playersInAGame.getPlayerTwo() == null)
               handleException();
           createAGame();
           startGame();
       }

       return super.onOptionsItemSelected(item);
    }

    private void handleException() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        Toast.makeText(getApplicationContext(), getString(R.string.game_create_issue), Toast.LENGTH_LONG).show();
        finish();
        return;
    }

    private void findStartingPlayer() {
        Handler loadHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (countOfHandler > 10) {
                    tryAgainButton.setVisibility(View.VISIBLE);
                    return;
                }
                startingPlayer = new Random().nextBoolean() ? playersInAGame.getPlayerOne() : playersInAGame.getPlayerTwo();
                ImageView startingPlayerImageView = (ImageView) findViewById(R.id.startingPlayerDisplayImageView);
                TextView startingTextView = (TextView) findViewById(R.id.startingPlayerTextView);
                int width = PlayerUtils.getWidthforImageViewByHalf(StartingPlayerActivity.this);
                String startingPlayerName;
                Uri startingPlayerProfileUri;
                if (startingPlayer.getId() == playersInAGame.getPlayerOne().getId()){
                    startingPlayerProfileUri = playersInAGame.getPlayerOneProfile();
                    startingPlayerName = playersInAGame.getPlayerOne().getPlayerName();
                }
                else {
                    startingPlayerProfileUri = playersInAGame.getPlayerTwoProfile();
                    startingPlayerName = playersInAGame.getPlayerTwo().getPlayerName();
                }

                Glide.with(getApplicationContext()).load(startingPlayerProfileUri).override(width, width).into(startingPlayerImageView);
                startingTextView.setText(startingPlayerName);
                countOfHandler++;
                loadHandler.postDelayed(this, 50);
            }
        };
        loadHandler.postDelayed(runnable, 20);
    }

    private void createAGame() {
        game = new Game();
        game.setPlayerOneID(playersInAGame.getPlayerOne().getId());
        game.setPlayerTwoID(playersInAGame.getPlayerTwo().getId());
        game.setRoundsCompleted(0);
        game.setGamePlayStatus("P");
        game.setTimeCreated(System.currentTimeMillis());
        game.setTimeUpdated(System.currentTimeMillis());
        gameId = newGameViewModel.createAGame(game);

        if (gameId == -1)
            handleException();

        gameDetails = new GameDetails();
        gameDetails.setGame(game);
        gameDetails.setPlayersInAGame(playersInAGame);
        gameDetails.setRoundInProgress(1);
        gameDetails.setRoundsCompleted(0);
    }

    private void startGame() {
        Intent startIntent = new Intent(StartingPlayerActivity.this, GameSummaryActivity.class);
        startIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME,gameDetails);
        startActivity(startIntent);
    }
}
