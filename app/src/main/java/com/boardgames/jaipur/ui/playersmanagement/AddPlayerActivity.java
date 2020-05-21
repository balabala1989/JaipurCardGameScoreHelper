package com.boardgames.jaipur.ui.playersmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.utils.ImagePickAndCrop;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String PROFILE_IMAGE_URI_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.PROFILE_URI";
    public static final String PLAYER_NAME_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.PLAYER_NAME";
    private boolean isPermissionGranted = false;
    private final int REQUEST_IMAGE = 100;
    private Player newPlayer;
    private String playerAvatarAbsolutePath;
    private Uri profileSelectedUri = null;

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
                EditText nameEditText = (EditText) findViewById(R.id.playerNameEditText);
                if (nameEditText.getText().toString().trim().equalsIgnoreCase("")) {
                    nameEditText.setError(getErrorMessageWithColor());
                    nameEditText.requestFocus();
                    return super.onOptionsItemSelected(item);
                }
                replyTent.putExtra(PLAYER_NAME_REPLY, nameEditText.getText().toString());
                replyTent.putExtra(PROFILE_IMAGE_URI_REPLY,profileSelectedUri);
                setResult(RESULT_OK, replyTent);
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
                profileSelectedUri = imageUri;
                Glide.with(this).load(imageUri).into(playerAvatarImageView);
                return;
            }
        }
        profileSelectedUri = null;
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

    private SpannableStringBuilder getErrorMessageWithColor() {
        String errorMessage = getString(R.string.edittext_playername_error);
        final int version = Build.VERSION.SDK_INT;
        int errorColor;

        if (version >= 23) {
            errorColor = ContextCompat.getColor(AddPlayerActivity.this, R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorMessage);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorMessage.length(), 0);
        return spannableStringBuilder;
    }
}
