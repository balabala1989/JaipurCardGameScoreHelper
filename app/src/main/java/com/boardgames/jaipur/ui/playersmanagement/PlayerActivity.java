package com.boardgames.jaipur.ui.playersmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.utils.ImagePickAndCrop;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity {

    private boolean isPermissionGranted = false;
    private Uri profileSelectedUri = null;
    private int requestType;
    private Player updatePlayer;
    private boolean isProfileImageChanged = false;
    private boolean isDeleteConfirmed;
    private AlertDialog dialog;

    //TODO for update profile give option to remove the profile and keep default value
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO when name is typed, out of click of the text box, the qwerty keyboard still displays. Remove it
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent receivedIntent = getIntent();
        String activityTitle;

        if (receivedIntent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.player_update_issue), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        requestType = receivedIntent.getIntExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE, -1);
        activityTitle = requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER ?
                getString(R.string.playeractivity_title_for_add_player) :
                getString(R.string.playeractivity_title_for_edit_player);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(activityTitle);

        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        playerAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(PlayerActivity.this);
                if (isPermissionGranted)
                    ImagePickAndCrop.showOptions(PlayerActivity.this,
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
                    CheckForPermissionsState.deniedPermission(PlayerActivity.this);
            }
        });

        if (requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER)
            handleUpdatePlayerRequest(receivedIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER)
            getMenuInflater().inflate(R.menu.activity_player_add_menu, menu);
        else
            getMenuInflater().inflate(R.menu.activity_player_update_menu, menu);

        for(int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.updatePlayerDeleteButton)
                menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent replyTent = new Intent();
        replyTent.putExtra(ApplicationConstants.PLAYERACTIVITY_REQUEST_TYPE_REPLY, requestType);

        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED, replyTent);
            finish();
            return true;
        }

        replyTent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS, (Parcelable) updatePlayer);
        if (item.getItemId() == R.id.updatePlayerDeleteButton) {
            alertUserForDeleteOperation(replyTent);
            return super.onOptionsItemSelected(item);
        }

        EditText nameEditText = (EditText) findViewById(R.id.playerNameEditText);
        if (nameEditText.getText().toString().trim().equalsIgnoreCase("")) {
            nameEditText.setError(getErrorMessageWithColor());
            nameEditText.requestFocus();
            return super.onOptionsItemSelected(item);
        }

        replyTent.putExtra(ApplicationConstants.PLAYERACTIVITY_DELETE_OPERATION_REQUESTED, false);
        replyTent.putExtra(ApplicationConstants.PLAYERACTIVITY_UPDATE_OPERATION_PROFILE_IMAGE_CHANGED, isProfileImageChanged);
        replyTent.putExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY, nameEditText.getText().toString());
        replyTent.putExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY,profileSelectedUri);

        setResult(RESULT_OK, replyTent);
        finish();
        return true;
    }

    private void alertUserForDeleteOperation(Intent replyIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alertdialog_confirmation_title));
        builder.setMessage(getString(R.string.alertdialog_confirmation_message));

        builder.setPositiveButton(getString(R.string.alertdialog_confirmation_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                replyIntent.putExtra(ApplicationConstants.PLAYERACTIVITY_DELETE_OPERATION_REQUESTED, true);
                setResult(RESULT_OK, replyIntent);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getParcelableExtra(ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_URI_PATH_REPLY);
            if (imageUri != null) {
                isProfileImageChanged = requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER ? true : false;
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
        isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(PlayerActivity.this);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(PlayerActivity.this, ImagePickAndCrop.class);
        intent.putExtra(ImagePickAndCrop.INTENT_IMAGE_PICKER_OPTION, ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickAndCrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickAndCrop.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickAndCrop.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickAndCrop.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(PlayerActivity.this, ImagePickAndCrop.class);
        intent.putExtra(ImagePickAndCrop.INTENT_IMAGE_PICKER_OPTION, ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickAndCrop.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickAndCrop.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_IMAGE);
    }

    private SpannableStringBuilder getErrorMessageWithColor() {
        String errorMessage = getString(R.string.edittext_playername_error);
        final int version = Build.VERSION.SDK_INT;
        int errorColor;

        if (version >= 23) {
            errorColor = ContextCompat.getColor(PlayerActivity.this, R.color.errorColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
        }
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorMessage);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorMessage.length(), 0);
        return spannableStringBuilder;
    }

   private void handleUpdatePlayerRequest(Intent receivedIntent) {
       updatePlayer = receivedIntent.getParcelableExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS);
       ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
       EditText playerNameEditText = (EditText) findViewById(R.id.playerNameEditText);
       Bitmap imageMap;

        playerNameEditText.setText(updatePlayer.getPlayerName());
        if (updatePlayer.getPlayerAvatar() == null || updatePlayer.getPlayerAvatar().equalsIgnoreCase("")) {
            imageMap = BitmapFactory.decodeResource(getResources(),R.drawable.default_player_avatar);
        }
        else {
            imageMap = BitmapFactory.decodeFile(updatePlayer.getPlayerAvatar());
        }


       int width = PlayerUtils.getWidthforImageViewByHalf(this);
       Glide.with(this).load(imageMap).override(width, width).into(playerAvatarImageView);
    }


}
