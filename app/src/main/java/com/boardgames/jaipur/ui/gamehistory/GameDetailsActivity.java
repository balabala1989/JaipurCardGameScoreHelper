package com.boardgames.jaipur.ui.gamehistory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Round;
import com.boardgames.jaipur.repository.GamesAndRoundsRepository;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GamePhotoStatusEnum;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GameDetailsActivity extends AppCompatActivity {

    private List<Round> roundsOfAGame;
    private GameDetails gameDetails;
    private GamesAndRoundsRepository gamesAndRoundsRepository;
    private String photoName;
    private GamePhotoStatusEnum gamePhotoStatusEnum;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.game_summary_activity_title));

        makeItemsVisibleAndInvisible();

        Intent receivedIntent = getIntent();
        if (receivedIntent == null) {
            handleException();
        }

        gameDetails = receivedIntent.getParcelableExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME);

        if (gameDetails == null || gameDetails.getPlayersInAGame() == null
                || gameDetails.getPlayersInAGame().getPlayerOne() == null
                || gameDetails.getPlayersInAGame().getPlayerTwo() == null)
            handleException();

        gamesAndRoundsRepository = new GamesAndRoundsRepository(getApplication());
        roundsOfAGame = gamesAndRoundsRepository.getRoundsForAGame(gameDetails.getGame().getId());

        setRoundsToGameDetails();

        GameUtils.loadUserDetailsInSummaryPage(GameDetailsActivity.this, getApplicationContext(), gameDetails);

        StringBuffer winnerMessage = new StringBuffer();
        if (gameDetails.getGame().getWinner() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
            findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
            findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
            winnerMessage.append(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());
        }
        else {
            findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
            findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
            winnerMessage.append(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());
        }

        ((TextView) findViewById(R.id.gameDateTextView)).setText(GameUtils.convertTimeToDate(gameDetails.getGame().getTimeCreated()));

        setScoreInRounds();

        findViewById(R.id.winnerAnnounceMentTextView).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.winnerAnnounceMentTextView)).setText(winnerMessage.append(ApplicationConstants.WON_MESSAGE).toString());
        //TODO check on playername coming on top of image
    }

    private void makeItemsVisibleAndInvisible() {
        findViewById(R.id.winnerAnnouncementView).setVisibility(View.INVISIBLE);
        findViewById(R.id.winnerPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.winnerPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundTwoView).setVisibility(View.INVISIBLE);
        findViewById(R.id.roundThreeView).setVisibility(View.INVISIBLE);
    }

    private void handleException() {
        Toast.makeText(getApplicationContext(), "Unable to load Game Details!!!!", Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void setRoundsToGameDetails() {
        if (gameDetails.getPlayerOneRounds() == null)
            gameDetails.setPlayerOneRounds(new HashMap<>());
        if (gameDetails.getPlayerTwoRounds() == null)
            gameDetails.setPlayerTwoRounds(new HashMap<>());

        for (Round round : roundsOfAGame) {
            if (round.getPlayerID() == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                gameDetails.getPlayerOneRounds().put(round.getRoundNumber(), round);
            }
            else if (round.getPlayerID() == gameDetails.getPlayersInAGame().getPlayerTwo().getId()) {
                gameDetails.getPlayerTwoRounds().put(round.getRoundNumber(), round);
            }
        }
    }

    private void setScoreInRounds() {
        Round playerOneRound;
        Round playerTwoRound;
        TextView playerOneScore;
        TextView playerTwoScore;

        //Round One
        if (gameDetails.getPlayerOneRounds().containsKey(1) && gameDetails.getPlayerTwoRounds().containsKey(1)) {
            findViewById(R.id.roundOneView).setVisibility(View.VISIBLE);
            playerOneRound = gameDetails.getPlayerOneRounds().get(1);
            playerTwoRound = gameDetails.getPlayerTwoRounds().get(1);
            playerOneScore = findViewById(R.id.roundOnePlayerOneScoreTextView);
            playerTwoScore = findViewById(R.id.roundOnePlayerTwoScoreTextView);

            playerOneScore.setText(String.valueOf(playerOneRound.getScore()));
            playerOneScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);
            playerTwoScore.setText(String.valueOf(playerTwoRound.getScore()));
            playerTwoScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);

            if (playerOneRound.getPlayerID() == playerOneRound.getWinner()) {
                findViewById(R.id.roundOnePlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
                findViewById(R.id.roundOnePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.roundOnePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.roundOnePlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
            }

            enableClickForTextView(findViewById(R.id.roundOneView), 1);
        }

        //Round Two
        if (gameDetails.getPlayerOneRounds().containsKey(2) && gameDetails.getPlayerTwoRounds().containsKey(2)) {
            findViewById(R.id.roundTwoView).setVisibility(View.VISIBLE);
            playerOneRound = gameDetails.getPlayerOneRounds().get(2);
            playerTwoRound = gameDetails.getPlayerTwoRounds().get(2);
            playerOneScore = findViewById(R.id.roundTwoPlayerOneScoreTextView);
            playerTwoScore = findViewById(R.id.roundTwoPlayerTwoScoreTextView);

            playerOneScore.setText(String.valueOf(playerOneRound.getScore()));
            playerOneScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);
            playerTwoScore.setText(String.valueOf(playerTwoRound.getScore()));
            playerTwoScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);

            if (playerOneRound.getPlayerID() == playerOneRound.getWinner()) {
                findViewById(R.id.roundTwoPlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
                findViewById(R.id.roundTwoPlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.roundTwoPlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.roundTwoPlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
            }
            enableClickForTextView(findViewById(R.id.roundTwoView), 2);
        }

        //Round three
        if (gameDetails.getPlayerOneRounds().containsKey(3) && gameDetails.getPlayerTwoRounds().containsKey(3)) {
            findViewById(R.id.roundThreeView).setVisibility(View.VISIBLE);
            playerOneRound = gameDetails.getPlayerOneRounds().get(3);
            playerTwoRound = gameDetails.getPlayerTwoRounds().get(3);
            playerOneScore = findViewById(R.id.roundThreePlayerOneScoreTextView);
            playerTwoScore = findViewById(R.id.roundThreePlayerTwoScoreTextView);

            playerOneScore.setText(String.valueOf(playerOneRound.getScore()));
            playerOneScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);
            playerTwoScore.setText(String.valueOf(playerTwoRound.getScore()));
            playerTwoScore.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp,0,0,0);

            if (playerOneRound.getPlayerID() == playerOneRound.getWinner()) {
                findViewById(R.id.roundThreePlayerOneSealOfExcellence).setVisibility(View.VISIBLE);
                findViewById(R.id.roundThreePlayerTwoSealOfExcellence).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.roundThreePlayerOneSealOfExcellence).setVisibility(View.INVISIBLE);
                findViewById(R.id.roundThreePlayerTwoSealOfExcellence).setVisibility(View.VISIBLE);
            }

            enableClickForTextView(findViewById(R.id.roundThreeView), 3);
        }
    }

    private void enableClickForTextView(View view, int roundNumber) {
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameDetailsActivity.this, RoundDetailsActivity.class);
                intent.putExtra(ApplicationConstants.STARTINGPLAYERACTIVITY_TO_ROUNDCALC_GAME, gameDetails);
                intent.putExtra(ApplicationConstants.GAME_SUMM_TO_ROUND_SUMM_ROUND_EDIT, roundNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAME_SUMMARY_CAMERA_REQUEST_IMAGE) {
            File image;
            try {
                image = PlayerUtils.getAvatarAbsolutePath(this, GameUtils.getImageFile(getApplicationContext(), photoName), getString(R.string.gameactivity_external_path));
                gameDetails.getGame().setGamePhotoLocation(image.getAbsolutePath());
                gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
                Toast.makeText(this, getString(R.string.camera_success), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_LONG).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == ApplicationConstants.GAME_SUMMARY_CAMERA_EDIT_REQUEST_IMAGE) {
            Uri imageUri = GameUtils.getImageFile(getApplicationContext(), photoName);
            gamePhotoStatusEnum = GamePhotoStatusEnum.EDIT;
            displayImage(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_summary_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                if (menuItem.getItemId() == R.id.calculateButton)
                    menuItem.setVisible(false);
                if (menuItem.getItemId() == R.id.shareButton)
                    menuItem.setVisible(true);
                if (menuItem.getItemId() == R.id.attachPhotoButton)
                    menuItem.setVisible(true);
            }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        else if (item.getItemId() == R.id.shareButton) {
            enableDisableIconsOfTextView(false);
            Uri shareUri = GameUtils.getUriFromViewForScreenshot(getWindow().getDecorView().getRootView().findViewById(R.id.screenViewLayout), this);
            enableDisableIconsOfTextView(true);
            GameUtils.initiateShareActivity(shareUri, this);
        }

        else if (item.getItemId() == R.id.attachPhotoButton) {
            if (gameDetails.getGame().getGamePhotoLocation() == null || gameDetails.getGame().getGamePhotoLocation().isEmpty()) {
                gamePhotoStatusEnum = GamePhotoStatusEnum.EMPTY;
                takePhotoUsingCamera();
            }
            else {
                Uri imageIUri = Uri.fromFile(new File(gameDetails.getGame().getGamePhotoLocation()));
                gamePhotoStatusEnum = GamePhotoStatusEnum.DISPLAYONLY;
                displayImage(imageIUri);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO need to call clearcache once the operatios is done
    private void takePhotoUsingCamera() {
        if (CheckForPermissionsState.requestStorageCameraPermissions(GameDetailsActivity.this)) {
            photoName = System.currentTimeMillis() + ".jpg";
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,GameUtils.getImageFile(getApplicationContext(), photoName));
            if (gamePhotoStatusEnum == GamePhotoStatusEnum.EMPTY)
                startActivityForResult(takePictureIntent,ApplicationConstants.GAME_SUMMARY_CAMERA_REQUEST_IMAGE);
            else if (gamePhotoStatusEnum == GamePhotoStatusEnum.EDIT)
                startActivityForResult(takePictureIntent,ApplicationConstants.GAME_SUMMARY_CAMERA_EDIT_REQUEST_IMAGE);
        }
        else {
            CheckForPermissionsState.deniedPermission(GameDetailsActivity.this);
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
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setView(dialogView);

        dialog = builder.create();
        dialog.show();
    }

    private void enableDisableIconsOfTextView(boolean enable) {
        if (enable) {
            ((TextView) findViewById(R.id.roundOnePlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
            ((TextView) findViewById(R.id.roundOnePlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
            ((TextView) findViewById(R.id.roundTwoPlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
            ((TextView) findViewById(R.id.roundTwoPlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
            ((TextView) findViewById(R.id.roundThreePlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
            ((TextView) findViewById(R.id.roundThreePlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pageview_black_24dp, 0, 0, 0);
        }
        else {
            ((TextView) findViewById(R.id.roundOnePlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((TextView) findViewById(R.id.roundOnePlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((TextView) findViewById(R.id.roundTwoPlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((TextView) findViewById(R.id.roundTwoPlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((TextView) findViewById(R.id.roundThreePlayerOneScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ((TextView) findViewById(R.id.roundThreePlayerTwoScoreTextView)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
}
