package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    private Player playerOne;
    private Player playerTwo;
    private Player startingPlayer;
    private int countOfHandler = 0;
    private Button tryAgainButton;
    private NewGameViewModel newGameViewModel;
    private long gameId;

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

        playerOne = (Player) receivedIntent.getSerializableExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERONE_DETAILS);
        playerTwo = (Player) receivedIntent.getSerializableExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERTWO_DETAILS);

        if (playerOne == null || playerTwo == null)
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
           if (playerOne == null || playerTwo == null)
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
                startingPlayer = new Random().nextBoolean() ? playerOne : playerTwo;
                ImageView startingPlayerImageView = (ImageView) findViewById(R.id.startingPlayerDisplayImageView);
                TextView startingTextView = (TextView) findViewById(R.id.startingPlayerTextView);
                int width = PlayerUtils.getWidthforImageView(StartingPlayerActivity.this);
                if (startingPlayer.getPlayerAvatar() != null && startingPlayer.getPlayerAvatar().equalsIgnoreCase(""))
                    Glide.with(getApplicationContext()).load(R.drawable.default_player_avatar).override(width, width).into(startingPlayerImageView);
                else {
                    Bitmap imageMap = BitmapFactory.decodeFile(startingPlayer.getPlayerAvatar());
                    Glide.with(getApplicationContext()).load(imageMap).override(width, width).into(startingPlayerImageView);
                }
                startingTextView.setText(startingPlayer.getPlayerName());
                countOfHandler++;
                loadHandler.postDelayed(this, 50);
            }
        };
        loadHandler.postDelayed(runnable, 20);
    }

    private void createAGame() {
        Game game = new Game();
        game.setPlayerOneID(playerOne.getId());
        game.setPlayerTwoID(playerTwo.getId());
        game.setRoundsCompleted(0);
        game.setGamePlayStatus("P");
        gameId = newGameViewModel.createAGame(game);
    }

    private void startGame() {
        Intent startIntent = new Intent(StartingPlayerActivity.this, RoundsCalculationActivity.class);
        startIntent.putExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERONE_DETAILS, playerOne);
        startIntent.putExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERTWO_DETAILS, playerTwo);
        startActivity(startIntent);
    }
}
