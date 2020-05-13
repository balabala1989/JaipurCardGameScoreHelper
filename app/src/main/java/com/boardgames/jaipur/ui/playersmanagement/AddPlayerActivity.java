package com.boardgames.jaipur.ui.playersmanagement;

import androidx.appcompat.app.AppCompatActivity;
import com.boardgames.jaipur.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String ADD_PLAYER_REPLY = "com.boardgames.jaipur.ui.playersmanagement.AddPlayerActivity.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_player_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addPlayerButton) {

        }
        return super.onOptionsItemSelected(item);
    }
}


