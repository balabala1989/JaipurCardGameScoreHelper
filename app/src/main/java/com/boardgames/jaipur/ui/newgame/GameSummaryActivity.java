package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.MainActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationSummaryActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class GameSummaryActivity extends AppCompatActivity {

    private NewGameViewModel newGameViewModel;
    private AlertDialog dialog;
    private GameDetails gameDetails;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_summary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.game_summary_activity_title));

        newGameViewModel = new ViewModelProvider(this).get(NewGameViewModel.class);

        Intent receivedIntent = getIntent();
        if (receivedIntent == null)
            handleException();


        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);

        if (gameDetails == null || gameDetails.getPlayersInAGame() == null
                || gameDetails.getPlayersInAGame().getPlayerOne() == null
                || gameDetails.getPlayersInAGame().getPlayerTwo() == null)
            handleException();

        int width = PlayerUtils.getWidthforImageViewByOneThird(GameSummaryActivity.this);
        TextView playerOneTextView = findViewById(R.id.playerOneTextView);
        ImageView playerOneImageView = findViewById(R.id.playerOneImageView);

        Glide.with(getApplicationContext()).load(gameDetails.getPlayersInAGame().getPlayerOneProfile()).override(width, width).into(playerOneImageView);
        playerOneTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());

        TextView playerTwoTextView = findViewById(R.id.playerTwoTextView);
        ImageView playerTwoImageView = findViewById(R.id.playerTwoImageView);

        Glide.with(getApplicationContext()).load(gameDetails.getPlayersInAGame().getPlayerTwoProfile()).override(width, width).into(playerTwoImageView);
        playerTwoTextView.setText(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());

        if (gameDetails.getRoundInProgress() == 1) {
            startRoundCalculation();
        }

        findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        handleBackButtonPress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_game_summary_menu, menu);

       if (isGameOver) {
           for (int i = 0; i < menu.size(); i++) {
               MenuItem menuItem = menu.getItem(i);
               if (menuItem.getItemId() == R.id.calculateButton)
                   menuItem.setVisible(false);
           }
       }
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackButtonPress();
            return true;
        }

        else if (item.getItemId() == R.id.calculateButton) {
            startRoundCalculation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE) {
            if (data == null)
                handleException();
           handleRoundCalculationResult(data);
        }
    }

    private void handleException() {
        Intent resetIntent = new Intent(GameSummaryActivity.this, MainActivity.class);
        resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(resetIntent);
        Toast.makeText(getApplicationContext(), getString(R.string.game_create_issue), Toast.LENGTH_LONG).show();
        finish();
        return;
    }

    private void handleBackButtonPress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setMessage(getString(R.string.alertdialog_backbutton_press_game_summary_msg));
        builder.setPositiveButton(getString(R.string.alertdialog_confirmation_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newGameViewModel.deleteAGame(gameDetails.getGame());
                Intent resetIntent = new Intent(GameSummaryActivity.this, MainActivity.class);
                resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(resetIntent);
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.alertdialog_confirmation_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void startRoundCalculation() {
        Intent roundCalIntent = new Intent(GameSummaryActivity.this, RoundsCalculationSummaryActivity.class);
        roundCalIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        startActivityForResult(roundCalIntent, ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE);
    }

    private void handleRoundCalculationResult(Intent data) {
        String winMessage = data.getStringExtra(ApplicationConstants.ROUND_CALC_SUMM_TO_GAME_SUMM_WIN_MESSAGE);
        gameDetails = data.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        if (gameDetails.getRoundsCompleted() == 1)
            displayRoundOneResults();
        else if (gameDetails.getRoundsCompleted() == 2)
            displayRoundTwoResults();
        else if (gameDetails.getRoundsCompleted() == 3)
            displayRoundThreeResults();

        if (isGameOver)
            winMessage += ApplicationConstants.GAME_OVER;

        Toast.makeText(this, winMessage, Toast.LENGTH_LONG).show();
    }

    private void displayRoundOneResults() {
        TextView roundOnePlayerOneTextView = findViewById(R.id.roundOnePlayerOneScoreTextView);
        TextView roundOnePlayerTwoTextView = findViewById(R.id.roundOnePlayerTwoScoreTextView);

        int playerOneScore = gameDetails.getPlayerOneRounds().get(1).getScore();
        int playerTwoScore = gameDetails.getPlayerTwoRounds().get(1).getScore();

        roundOnePlayerOneTextView.setText(String.valueOf(playerOneScore));
        roundOnePlayerTwoTextView.setText(String.valueOf(playerTwoScore));

        if (gameDetails.getRoundWinners().get(1).getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            findViewById(R.id.roundOnePlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
            findViewById(R.id.roundOnePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.roundOnePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
            findViewById(R.id.roundOnePlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
        }

        isGameOver = false;
        gameDetails.setRoundInProgress(2);
        gameDetails.setRoundsCompleted(1);

    }

    private void displayRoundTwoResults() {
        findViewById(R.id.roundTwoView).setVisibility(View.VISIBLE);
        TextView roundTwoPlayerOneTextView = findViewById(R.id.roundTwoPlayerOneScoreTextView);
        TextView roundTwoPlayerTwoTextView = findViewById(R.id.roundTwoPlayerTwoScoreTextView);

        int playerOneScore = gameDetails.getPlayerOneRounds().get(2).getScore();
        int playerTwoScore = gameDetails.getPlayerTwoRounds().get(2).getScore();

        roundTwoPlayerOneTextView.setText(String.valueOf(playerOneScore));
        roundTwoPlayerTwoTextView.setText(String.valueOf(playerTwoScore));

        if (gameDetails.getRoundWinners().get(2).getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            findViewById(R.id.roundTwoPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
            findViewById(R.id.roundTwoPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.roundTwoPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
            findViewById(R.id.roundTwoPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
        }

        //Decide if the third round is required
        if (gameDetails.getRoundWinners().get(1).getId() == gameDetails.getRoundWinners().get(2).getId()) {
            findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerAnnouncementView).setVisibility(View.VISIBLE);

            if (gameDetails.getRoundWinners().get(1).getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                findViewById(R.id.winnerPlayerOneSOE).setVisibility(View.VISIBLE);
                findViewById(R.id.winnerPlayerTwoSOE).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.winnerPlayerOneSOE).setVisibility(View.INVISIBLE);
                findViewById(R.id.winnerPlayerTwoSOE).setVisibility(View.VISIBLE);
            }
            isGameOver = true;
            invalidateOptionsMenu();
        }
        else {
            gameDetails.setRoundInProgress(3);
            gameDetails.setRoundsCompleted(2);
            findViewById(R.id.winnerPlayerOneSOE).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerPlayerTwoSOE).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
            isGameOver = false;
        }
    }

    private void displayRoundThreeResults() {

        findViewById(R.id.roundThreeView).setVisibility(View.VISIBLE);

        TextView roundThreePlayerOneTextView = findViewById(R.id.roundThreePlayerOneScoreTextView);
        TextView roundThreePlayerTwoTextView = findViewById(R.id.roundThreePlayerTwoScoreTextView);

        int playerOneScore = gameDetails.getPlayerOneRounds().get(3).getScore();
        int playerTwoScore = gameDetails.getPlayerTwoRounds().get(3).getScore();

        roundThreePlayerOneTextView.setText(String.valueOf(playerOneScore));
        roundThreePlayerTwoTextView.setText(String.valueOf(playerTwoScore));

        if (gameDetails.getRoundWinners().get(3).getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            findViewById(R.id.roundThreePlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
            findViewById(R.id.roundThreePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.roundThreePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
            findViewById(R.id.roundThreePlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
        }

        int playerOneWinningCount = 0, playerTwoWinningCount = 0;

        for (Map.Entry<Integer, Player> entry : gameDetails.getRoundWinners().entrySet()) {
            if (entry.getValue().getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                playerOneWinningCount++;
            else
                playerTwoWinningCount++;
        }

        if (playerOneWinningCount > playerTwoWinningCount) {
            findViewById(R.id.winnerPlayerOneSOE).setVisibility(View.VISIBLE);
            findViewById(R.id.winnerPlayerTwoSOE).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.winnerPlayerOneSOE).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerPlayerTwoSOE).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.winnerAnnouncementView).setVisibility(View.VISIBLE);
        isGameOver = true;
        invalidateOptionsMenu();
    }
}
