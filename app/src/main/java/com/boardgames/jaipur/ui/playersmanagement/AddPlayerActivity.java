package com.boardgames.jaipur.ui.playersmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.utils.ImagePickAndCrop;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String ADD_PLAYER_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.REPLY";
    private boolean isPermissionGranted = false;
    private final int REQUEST_IMAGE = 100;
    private Player newPlayer;
    private String playerAvatarAbsolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_add_player);

        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        playerAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(AddPlayerActivity.this);
                if (isPermissionGranted)
                    ImagePickAndCrop.showOptions(AddPlayerActivity.this,
                            new ImagePickAndCrop.PickerOptionListener() {
                        @Override
                        public void onCameraSelected() {
                            launchCameraIntent();
                        }

                        @Override
                        public void onGallerySelected() {
                            launchGalleryIntent();
                        }
                    },
                            getString(R.string.alertdialog_title_add_profile_photo));
                else
                    CheckForPermissionsState.deniedPermission(AddPlayerActivity.this);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_player_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent replyTent = new Intent();
        switch(item.getItemId()) {
            case android.R.id.home: {
                setResult(RESULT_CANCELED, replyTent);
                finish();
                return true;
            }

            case R.id.addPlayerSaveButton: {
                newPlayer = new Player();
                setResult(RESULT_FIRST_USER, replyTent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getParcelableExtra("path");
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(playerAvatarImageView);
                return;
            }
        }
        Glide.with(this).load(R.drawable.default_player_avatar).into(playerAvatarImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(AddPlayerActivity.this);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(AddPlayerActivity.this, ImagePickAndCrop.class);
        intent.putExtra(ImagePickAndCrop.INTENT_IMAGE_PICKER_OPTION, ImagePickAndCrop.FROM_CAMERA);

        // setting aspect ratio
        intent.putExtra(ImagePickAndCrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickAndCrop.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickAndCrop.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickAndCrop.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(AddPlayerActivity.this, ImagePickAndCrop.class);
        intent.putExtra(ImagePickAndCrop.INTENT_IMAGE_PICKER_OPTION, ImagePickAndCrop.FROM_GALLERY);

        // setting aspect ratio
        intent.putExtra(ImagePickAndCrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
}
