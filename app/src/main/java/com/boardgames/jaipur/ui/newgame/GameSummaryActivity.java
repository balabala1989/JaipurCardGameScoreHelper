package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.boardgames.jaipur.MainActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationSummaryActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GamePhotoStatusEnum;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameSummaryActivity extends AppCompatActivity {

    private NewGameViewModel newGameViewModel;
    private AlertDialog dialog;
    private GameDetails gameDetails;
    private boolean isGameOver = false;
    private String photoName;
    private GamePhotoStatusEnum gamePhotoStatusEnum;
    private InterstitialAd saveGameAdUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_summary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        actionBar.setDisplayHomeAsUpEnabled(false);
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

        GameUtils.loadUserDetailsInSummaryPage(GameSummaryActivity.this, getApplicationContext(), gameDetails);

        findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);

        //Button Actions
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveButton();
                finishGameAndGoBackToMainPage();
            }
        });

        Button rematchButton = findViewById(R.id.rematchGameButton);
        rematchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveButton();
                finishGameAndDoRematchPage();
            }
        });

        Button resetButton = findViewById(R.id.resetGameButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResetButton();
            }
        });

        ((TextView) findViewById(R.id.gameDateTextView)).setText(GameUtils.convertTimeToDate(gameDetails.getGame().getTimeCreated()));

        if (gameDetails.getRoundInProgress() == 1) {
            startRoundCalculation(ApplicationConstants.GAME_SUMMARY_TO_ROUND_SUMMARY_NORMAL_MODE, -1);
        }

        if (gameDetails.getGame().getGamePhotoLocation() == null || gameDetails.getGame().getGamePhotoLocation().isEmpty())
            gamePhotoStatusEnum = GamePhotoStatusEnum.EMPTY;
        else
            gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;

        saveGameAdUnit = new InterstitialAd(this);
        saveGameAdUnit.setAdUnitId(getString(R.string.save_game_inter_adUnit_Id));
        saveGameAdUnit.loadAd(new AdRequest.Builder().build());
        saveGameAdUnit.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                saveGameAdUnit.loadAd(new AdRequest.Builder().build());
            }
        });
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
               if (menuItem.getItemId() == R.id.shareButton)
                   menuItem.setVisible(true);
               if (menuItem.getItemId() == R.id.attachPhotoButton) {
                   if (gamePhotoStatusEnum == GamePhotoStatusEnum.EMPTY) {
                       menuItem.setVisible(true);
                   }
                   else {
                       menuItem.setVisible(false);
                   }
               }
               if (menuItem.getItemId() == R.id.displayPhotoButton) {
                   if (gamePhotoStatusEnum != GamePhotoStatusEnum.EMPTY)
                       menuItem.setVisible(true);
                   else
                       menuItem.setVisible(false);
               }
           }
       }
       else {
           for (int i = 0; i < menu.size(); i++) {
               MenuItem menuItem = menu.getItem(i);
               if (menuItem.getItemId() == R.id.calculateButton)
                   menuItem.setVisible(true);
               if (menuItem.getItemId() == R.id.shareButton)
                   menuItem.setVisible(false);
               if (menuItem.getItemId() == R.id.attachPhotoButton)
                   menuItem.setVisible(false);
               if (menuItem.getItemId() == R.id.displayPhotoButton)
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
            startRoundCalculation(ApplicationConstants.GAME_SUMMARY_TO_ROUND_SUMMARY_NORMAL_MODE, -1);
        }

        else if (item.getItemId() == R.id.shareButton) {
            if (CheckForPermissionsState.requestStorageCameraPermissions(this))
                shareScreenShot();
            else
                CheckForPermissionsState.deniedPermission(this);
        }

        else if (item.getItemId() == R.id.attachPhotoButton) {
            gamePhotoStatusEnum = GamePhotoStatusEnum.EMPTY;
            takePhotoUsingCamera();
        }
        else if (item.getItemId() == R.id.displayPhotoButton){
                Uri imageIUri = Uri.fromFile(new File(gameDetails.getGame().getGamePhotoLocation()));
                gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
                displayImage(imageIUri);
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
        else if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAME_SUMMARY_CAMERA_REQUEST_IMAGE) {
            File image;
            try {
                image = PlayerUtils.getAvatarAbsolutePath(this, GameUtils.getImageFile(getApplicationContext(), photoName), getString(R.string.gameactivity_external_path));
                gameDetails.getGame().setGamePhotoLocation(image.getAbsolutePath());
                gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
                Toast.makeText(this, getString(R.string.camera_success), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_LONG).show();
            }
            invalidateOptionsMenu();
        }
        else if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAME_SUMMARY_CAMERA_EDIT_REQUEST_IMAGE) {
            Uri imageUri = GameUtils.getImageFile(getApplicationContext(), photoName);
            gamePhotoStatusEnum = GamePhotoStatusEnum.EDIT;
            displayImage(imageUri);
            invalidateOptionsMenu();
        }
        else if (resultCode == RESULT_CANCELED && requestCode == ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE) {
            if (data == null)
                handleException();
            String operationMode = data.getStringExtra(ApplicationConstants.GAME_SUMM_TO_ROUND_SUMM_MODE);
            gameDetails = data.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
            if (!operationMode.equalsIgnoreCase(ApplicationConstants.GAME_SUMMARY_TO_ROUND_SUMMARY_EDIT_MODE) && gameDetails.getRoundInProgress() == 1) {
                newGameViewModel.deleteAGame(gameDetails.getGame());
                finishGameAndGoBackToMainPage();
            }
        }
    }

    private void handleException() {
        Toast.makeText(getApplicationContext(), getString(R.string.game_create_issue), Toast.LENGTH_LONG).show();
        newGameViewModel.deleteAGame(gameDetails.getGame());
        finishGameAndGoBackToMainPage();
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
                finishGameAndGoBackToMainPage();
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

    private void startRoundCalculation(String mode, int roundForEdit) {
        Intent roundCalIntent = new Intent(GameSummaryActivity.this, RoundsCalculationSummaryActivity.class);
        roundCalIntent.putExtra(ApplicationConstants.GAME_SUMM_TO_ROUND_SUMM_MODE, mode);
        roundCalIntent.putExtra(ApplicationConstants.GAME_SUMM_TO_ROUND_SUMM_ROUND_EDIT, roundForEdit);
        roundCalIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        startActivityForResult(roundCalIntent, ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE);
    }

    private void handleRoundCalculationResult(Intent data) {
        String winMessage = data.getStringExtra(ApplicationConstants.ROUND_CALC_SUMM_TO_GAME_SUMM_WIN_MESSAGE);
        String textMessage = "";
        gameDetails = data.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        if (gameDetails.getRoundsCompleted() == 1) {
            displayRoundOneResults();
            isGameOver = false;
            gameDetails.setRoundInProgress(2);
            gameDetails.setRoundsCompleted(1);
        }
        else if (gameDetails.getRoundsCompleted() == 2) {
            displayRoundTwoResults();
            //Decide if the third round is required
            if (gameDetails.getRoundWinners().get(1).getId() == gameDetails.getRoundWinners().get(2).getId()) {
                findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);
                findViewById(R.id.winnerAnnouncementView).setVisibility(View.VISIBLE);

                if (gameDetails.getRoundWinners().get(1).getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                    findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
                    findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
                    gameDetails.getGame().setWinner(gameDetails.getPlayersInAGame().getPlayerOne().getId());
                }
                else {
                    findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                    findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
                    gameDetails.getGame().setWinner(gameDetails.getPlayersInAGame().getPlayerTwo().getId());
                }
                isGameOver = true;
                findViewById(R.id.roundThreeView).setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
            else {
                gameDetails.setRoundInProgress(3);
                gameDetails.setRoundsCompleted(2);
                findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
                isGameOver = false;
            }
        }
        else if (gameDetails.getRoundsCompleted() == 3) {
            displayRoundThreeResults();
            int playerOneWinningCount = 0, playerTwoWinningCount = 0;

            for (Map.Entry<Integer, Player> entry : gameDetails.getRoundWinners().entrySet()) {
                if (entry.getValue().getId() == gameDetails.getPlayersInAGame().getPlayerOne().getId())
                    playerOneWinningCount++;
                else
                    playerTwoWinningCount++;
            }

            if (playerOneWinningCount > playerTwoWinningCount) {
                findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
                findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
                gameDetails.getGame().setWinner(gameDetails.getPlayersInAGame().getPlayerOne().getId());
            }
            else {
                findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
                gameDetails.getGame().setWinner(gameDetails.getPlayersInAGame().getPlayerTwo().getId());
            }

            gameDetails.setRoundsCompleted(3);
            findViewById(R.id.winnerAnnouncementView).setVisibility(View.VISIBLE);
            isGameOver = true;
        }

        enableEditForTheRound(gameDetails.getRoundsCompleted());
        invalidateOptionsMenu();
        if (isGameOver) {
            if (gameDetails.getGame().getWinner() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                winMessage += "\n" +  gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName();
                textMessage = gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName();
            }
            else {
                winMessage += "\n" +  gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName();
                textMessage = gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName();
            }
            winMessage += ApplicationConstants.GAME_OVER;
            textMessage += ApplicationConstants.GAME_OVER;
        }
        displayWinnerAlert(isGameOver);
        ((TextView)findViewById(R.id.winnerAnnounceMentTextView)).setText(textMessage);
        findViewById(R.id.winnerAnnounceMentTextView).setVisibility(View.VISIBLE);
        Toast.makeText(this, winMessage, Toast.LENGTH_LONG).show();
    }

    private void displayWinnerAlert(boolean start) {
        LottieAnimationView animationView = findViewById(R.id.winnerAnimation);
        if (start) {
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        }
        else {
            animationView.setVisibility(View.INVISIBLE);
            animationView.cancelAnimation();
        }
    }

    private void displayRoundOneResults() {
        findViewById(R.id.roundOneView).setVisibility(View.VISIBLE);
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
    }

    private void finishGameAndGoBackToMainPage() {
        if (saveGameAdUnit.isLoaded())
            saveGameAdUnit.show();
        Intent resetIntent = new Intent(GameSummaryActivity.this, MainActivity.class);
        resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(resetIntent);
        finish();
    }

    private void finishGameAndDoRematchPage() {
        if (saveGameAdUnit.isLoaded())
            saveGameAdUnit.show();
        Intent startIntent = new Intent(GameSummaryActivity.this, StartingPlayerActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntent.putExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERS_IN_A_GAME, gameDetails.getPlayersInAGame());
        startActivity(startIntent);
        finish();
    }

    private void handleSaveButton() {
        Game game = gameDetails.getGame();
        game.setRoundsCompleted(gameDetails.getRoundsCompleted());

        //Set player one score in Round 1~Round 2~Round 3
        StringBuffer playerScore = new StringBuffer();

        if (gameDetails.getPlayerOneRounds().containsKey(1))
            playerScore.append(gameDetails.getPlayerOneRounds().get(1).getScore());
        if (gameDetails.getPlayerOneRounds().containsKey(2))
            playerScore.append(ApplicationConstants.SEPARATOR_OF_VIEWS).append(gameDetails.getPlayerOneRounds().get(2).getScore());
        if (gameDetails.getPlayerOneRounds().containsKey(3))
            playerScore.append(ApplicationConstants.SEPARATOR_OF_VIEWS).append(gameDetails.getPlayerOneRounds().get(3).getScore());

        game.setPlayerOneScores(playerScore.toString());

        playerScore = new StringBuffer();

        if (gameDetails.getPlayerTwoRounds().containsKey(1))
            playerScore.append(gameDetails.getPlayerTwoRounds().get(1).getScore());
        if (gameDetails.getPlayerTwoRounds().containsKey(2))
            playerScore.append(ApplicationConstants.SEPARATOR_OF_VIEWS).append(gameDetails.getPlayerTwoRounds().get(2).getScore());
        if (gameDetails.getPlayerTwoRounds().containsKey(3))
            playerScore.append(ApplicationConstants.SEPARATOR_OF_VIEWS).append(gameDetails.getPlayerTwoRounds().get(3).getScore());

        game.setPlayerTwoScores(playerScore.toString());
        game.setGamePlayStatus("C");
        game.setTimeUpdated(System.currentTimeMillis());
        newGameViewModel.createAGame(game);

        Toast.makeText(this, getString(R.string.game_success), Toast.LENGTH_LONG).show();
    }

    private void handleResetButton() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setMessage(getString(R.string.alertdialog_backbutton_press_game_summary_msg));
        builder.setPositiveButton(getString(R.string.alertdialog_confirmation_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resetGameDetails();
                startRoundCalculation(ApplicationConstants.GAME_SUMMARY_TO_ROUND_SUMMARY_NORMAL_MODE, -1);
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

    private void resetGameDetails() {
        findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundOnePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundOnePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundOneView).setVisibility(View.INVISIBLE);


        ((TextView) findViewById(R.id.roundThreePlayerOneScoreTextView)).setText(getString(R.string.default_score));
        ((TextView) findViewById(R.id.roundThreePlayerTwoScoreTextView)).setText(getString(R.string.default_score));
        ((TextView) findViewById(R.id.roundTwoPlayerOneScoreTextView)).setText(getString(R.string.default_score));
        ((TextView) findViewById(R.id.roundTwoPlayerTwoScoreTextView)).setText(getString(R.string.default_score));
        ((TextView) findViewById(R.id.roundOnePlayerOneScoreTextView)).setText(getString(R.string.default_score));
        ((TextView) findViewById(R.id.roundOnePlayerTwoScoreTextView)).setText(getString(R.string.default_score));

        gameDetails.setPlayerOneRounds(new HashMap<>());
        gameDetails.setPlayerTwoRounds(new HashMap<>());
        gameDetails.setRoundWinners(new HashMap<>());
        gameDetails.setGoodsDetailsForARoundMap(new HashMap<>());
        gameDetails.setRoundInProgress(1);
        gameDetails.setRoundsCompleted(0);

        isGameOver = false;

        invalidateOptionsMenu();
    }

    private void shareScreenShot() {
        enableEditForTheRound(-1);
        findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
        Uri shareUri = GameUtils.getUriFromViewForScreenshot(findViewById(R.id.screenScrollView), this);
        findViewById(R.id.winnerAnnouncementView).setVisibility(View.VISIBLE);
        enableEditForTheRound(gameDetails.getRoundsCompleted());
        GameUtils.initiateShareActivity(shareUri, this);
    }

    private void takePhotoUsingCamera() {
        if (CheckForPermissionsState.requestStorageCameraPermissions(GameSummaryActivity.this)) {
            photoName = System.currentTimeMillis() + ".jpg";
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,GameUtils.getImageFile(getApplicationContext(), photoName));
            if (gamePhotoStatusEnum == GamePhotoStatusEnum.EMPTY)
                startActivityForResult(takePictureIntent,ApplicationConstants.GAME_SUMMARY_CAMERA_REQUEST_IMAGE);
            else if (gamePhotoStatusEnum == GamePhotoStatusEnum.EDIT)
                startActivityForResult(takePictureIntent,ApplicationConstants.GAME_SUMMARY_CAMERA_EDIT_REQUEST_IMAGE);
        }
        else {
            CheckForPermissionsState.deniedPermission(GameSummaryActivity.this);
        }
    }



    private void enableEditForTheRound(int roundNumber) {
        TextView roundOnePlayerOneTextView = findViewById(R.id.roundOnePlayerOneScoreTextView);
        TextView roundOnePlayerTwoTextView = findViewById(R.id.roundOnePlayerTwoScoreTextView);
        TextView roundTwoPlayerOneTextView = findViewById(R.id.roundTwoPlayerOneScoreTextView);
        TextView roundTwoPlayerTwoTextView = findViewById(R.id.roundTwoPlayerTwoScoreTextView);
        TextView roundThreePlayerOneTextView = findViewById(R.id.roundThreePlayerOneScoreTextView);
        TextView roundThreePlayerTwoTextView = findViewById(R.id.roundThreePlayerTwoScoreTextView);

        if (roundNumber == 1) {
            enableDisableIconAndClickableInTextView(roundOnePlayerOneTextView, true, roundNumber);
            enableDisableIconAndClickableInTextView(roundOnePlayerTwoTextView, true, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerTwoTextView, false, roundNumber);
        }
        else if (roundNumber == 2) {
            enableDisableIconAndClickableInTextView(roundOnePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundOnePlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerOneTextView, true, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerTwoTextView, true, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerTwoTextView, false, roundNumber);
        }
        else if (roundNumber == 3) {
            enableDisableIconAndClickableInTextView(roundOnePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundOnePlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerOneTextView, true, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerTwoTextView, true, roundNumber);
        }
        else if (roundNumber == -1) {
            enableDisableIconAndClickableInTextView(roundOnePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundOnePlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundTwoPlayerTwoTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerOneTextView, false, roundNumber);
            enableDisableIconAndClickableInTextView(roundThreePlayerTwoTextView, false, roundNumber);
        }
    }

    private void enableDisableIconAndClickableInTextView(TextView textView, boolean enable, int roundNumber) {
        if (enable) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mode_edit_black_24dp, 0, 0, 0);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRoundCalculation(ApplicationConstants.GAME_SUMMARY_TO_ROUND_SUMMARY_EDIT_MODE, roundNumber);
                }
            });
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            textView.setClickable(false);
        }
    }

    private void displayImage(Uri imageUri) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.layout_game_image_display, null);
        ImageView dialogImageView = dialogView.findViewById(R.id.gamePhotoImageView);
        int width = PlayerUtils.getWidthforImageViewByHalf(this);
        Glide.with(this).load(imageUri).override(width, width).into(dialogImageView);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        if (gamePhotoStatusEnum == GamePhotoStatusEnum.DISPLAYONLY) {
            closeButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);
        }
        else if (gamePhotoStatusEnum == GamePhotoStatusEnum.EDIT) {
            closeButton.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(v -> {
            dialog.dismiss();
            File image;
            try {
                image = PlayerUtils.getAvatarAbsolutePath(this, imageUri, getString(R.string.gameactivity_external_path));
                gameDetails.getGame().setGamePhotoLocation(image.getAbsolutePath());
                gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
                GameUtils.clearCache(getApplicationContext());
                Toast.makeText(this, getString(R.string.camera_success), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
            gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
        });

        closeButton.setOnClickListener(v -> {
            dialog.dismiss();
            gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
        });

        dialogImageView.setOnClickListener(v -> {
            dialog.dismiss();
            gamePhotoStatusEnum = GamePhotoStatusEnum.EDIT;
            takePhotoUsingCamera();
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image");
        builder.setView(dialogView);

        dialog = builder.create();
        dialog.show();
    }
}
