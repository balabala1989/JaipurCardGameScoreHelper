package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.MainActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameSummaryActivity extends AppCompatActivity {

    private NewGameViewModel newGameViewModel;
    private AlertDialog dialog;
    private GameDetails gameDetails;

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
            hideAllRounds();
            startRoundCalculation();
        }
    }

    @Override
    public void onBackPressed() {
        handleBackButtonPress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.activity_game_summary_menu, menu);
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

    private void computeRoundScore(int roundNumber) {
        Intent roundIntent = new Intent(GameSummaryActivity.this, RoundsCalculationActivity.class);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE) {
            if (data == null)
                handleException();
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
        Intent roundCalIntent = new Intent(GameSummaryActivity.this, RoundsCalculationActivity.class);
        roundCalIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        startActivityForResult(roundCalIntent, ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE);
    }

    private void handleRoundCalculationResult(Intent data) {

    }

    private void displayRoundOneResults(GameDetails gameDetails) {

    }

    private void displayRoundTwoResults(GameDetails gameDetails) {

    }

    private void displayRoundThreeResults(GameDetails gameDetails) {

    }

    private void hideAllRounds() {

    }
}
