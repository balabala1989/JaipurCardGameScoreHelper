package com.boardgames.jaipur.ui.newgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.bumptech.glide.Glide;

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

import java.util.Random;

public class StartingPlayerActivity extends AppCompatActivity {

    private Player playerOne;
    private Player playerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_player);

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

        new Thread(() -> {
            try {
                Thread.sleep(200);
                findStartingPlayer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
        Player startingPlayer = new Random().nextBoolean() ? playerOne : playerTwo;

        ImageView startingPlayerImageView = findViewById(R.id.startingPlayerDisplayImageView);
        TextView startingTextView = findViewById(R.id.startingPlayerTextView);

        for (int i = 0; i < 5; i++) {
            Player player = i%2 == 0 ? playerOne : playerTwo;
            if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equalsIgnoreCase(""))
                Glide.with(this).load(R.drawable.default_player_avatar).into(startingPlayerImageView);
            else {
                Bitmap imageMap = BitmapFactory.decodeFile(player.getPlayerAvatar());
                Glide.with(this).load(imageMap).into(startingPlayerImageView);
            }
            startingTextView.setText(player.getPlayerName());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (startingPlayer.getPlayerAvatar() != null && startingPlayer.getPlayerAvatar().equalsIgnoreCase(""))
            Glide.with(this).load(R.drawable.default_player_avatar).into(startingPlayerImageView);
        else {
            Bitmap imageMap = BitmapFactory.decodeFile(startingPlayer.getPlayerAvatar());
            Glide.with(this).load(imageMap).into(startingPlayerImageView);
        }
        startingTextView.setText(startingPlayer.getPlayerName());
    }
}
