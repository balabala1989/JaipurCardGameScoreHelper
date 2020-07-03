package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.MainActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Game;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationSummaryActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
               if (menuItem.getItemId() == R.id.attachPhotoButton)
                   menuItem.setVisible(true);
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

        else if (item.getItemId() == R.id.shareButton) {
            shareScreenShot();
        }

        else if (item.getItemId() == R.id.attachPhotoButton) {
            takePhotoUsingCamera();
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
                image = PlayerUtils.getAvatarAbsolutePath(this, getImageFile(photoName), getString(R.string.gameactivity_external_path));
                gameDetails.getGame().setGamePhotoLocation(image.getAbsolutePath());
            } catch (IOException e) {
                handleException();
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

    private void startRoundCalculation() {
        Intent roundCalIntent = new Intent(GameSummaryActivity.this, RoundsCalculationSummaryActivity.class);
        roundCalIntent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
        startActivityForResult(roundCalIntent, ApplicationConstants.GAMESUMMARYACTIVITY_ROUNDCALCACTIVITY_REQUEST_CODE);
    }

    private void handleRoundCalculationResult(Intent data) {
        String winMessage = data.getStringExtra(ApplicationConstants.ROUND_CALC_SUMM_TO_GAME_SUMM_WIN_MESSAGE);
        String textMessage = "";
        gameDetails = data.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);
        if (gameDetails.getRoundsCompleted() == 1)
            displayRoundOneResults();
        else if (gameDetails.getRoundsCompleted() == 2)
            displayRoundTwoResults();
        else if (gameDetails.getRoundsCompleted() == 3)
            displayRoundThreeResults();

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
        ((TextView)findViewById(R.id.winnerAnnounceMentTextView)).setText(textMessage);
        Toast.makeText(this, winMessage, Toast.LENGTH_LONG).show();
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
        invalidateOptionsMenu();
    }

    private void finishGameAndGoBackToMainPage() {
        Intent resetIntent = new Intent(GameSummaryActivity.this, MainActivity.class);
        resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(resetIntent);
        finish();
    }

    private void finishGameAndDoRematchPage() {
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
        game.setTimeUpdated(System.currentTimeMillis()/100);
        newGameViewModel.updateAGame(game);
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
                startRoundCalculation();
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
        if (!CheckForPermissionsState.requestStorageCameraPermissions(GameSummaryActivity.this)) {
            CheckForPermissionsState.deniedPermission(GameSummaryActivity.this);
            return;
        }
        findViewById(R.id.winnerAnnounceMentTextView).setVisibility(View.VISIBLE);
        View screenView = getWindow().getDecorView().getRootView().findViewById(R.id.screenViewLayout);
        Bitmap screenBitMap = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitMap);
        canvas.drawColor(Color.WHITE);
        screenView.draw(canvas);

        findViewById(R.id.winnerAnnounceMentTextView).setVisibility(View.INVISIBLE);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        screenBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        Uri shareUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", GameUtils.getAbsolutePathForImage(getApplicationContext(), screenBitMap));
        Intent localIntent = new Intent();
        localIntent.setAction("android.intent.action.SEND");
        localIntent.putExtra("android.intent.extra.STREAM", shareUri);
        localIntent.setType("image/jpg");
        localIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(localIntent);

    }

    private void takePhotoUsingCamera() {
        if (CheckForPermissionsState.requestStorageCameraPermissions(GameSummaryActivity.this)) {
            photoName = System.currentTimeMillis() + ".jpg";
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,getImageFile(photoName));
            startActivityForResult(takePictureIntent,ApplicationConstants.GAME_SUMMARY_CAMERA_REQUEST_IMAGE);
        }
        else {
            CheckForPermissionsState.deniedPermission(GameSummaryActivity.this);
        }
    }

    private Uri getImageFile(String fileName) {
        File primaryStorageVolume = new File(getExternalCacheDir(), "jaipurImages");
        if (!primaryStorageVolume.exists())
            primaryStorageVolume.mkdir();
        File imageFile = new File(primaryStorageVolume, fileName);
        return FileProvider.getUriForFile(GameSummaryActivity.this, getPackageName() + ".provider", imageFile);
    }
}
