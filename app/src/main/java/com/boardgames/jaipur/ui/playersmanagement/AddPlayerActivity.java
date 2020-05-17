package com.boardgames.jaipur.ui.playersmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yalantis.ucrop.UCrop;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String ADD_PLAYER_REPLY = "com.boardgames.jaipur.ui.playersmanagement.addplayeractivity.REPLY";
    public static final int PICK_PICTURE_FOR_PROFILE_FROM_CAMERA = 3113;
    public static final int PICK_PICTURE_FOR_PROFILE_FROM_GALLERY = 7112;
    private AlertDialog dialog;
    private String imageFilename;
    private final int MAX_CROP_SIZE = 100;
    private boolean permissionsGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_add_player);

        permissionsGranted = CheckForPermissionsState.requestStorageCameraPermissions(AddPlayerActivity.this);

        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        playerAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionsGranted) {
                    Toast.makeText(AddPlayerActivity.this, "This app requires permissions for using this feature. Please provide permissions in Settings!!!!! ", Toast.LENGTH_LONG).show();
                    return;
                }
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(AddPlayerActivity.this).inflate(R.layout.layout_images_grid_view, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPlayerActivity.this);
                builder.setTitle(getResources().getString(R.string.alertdialog_title));
                builder.setView(dialogView);
                dialog = builder.create();

                ImageView cameraImageView = (ImageView) dialogView.findViewById(R.id.cameraImageView);
                cameraImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageFilename = System.currentTimeMillis() + ".jpg";
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,getImageFile(imageFilename));
                        startActivityForResult(takePictureIntent,PICK_PICTURE_FOR_PROFILE_FROM_CAMERA);
                    }
                });

                ImageView galleryImageView = (ImageView) dialogView.findViewById(R.id.galleryImageView);
                galleryImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_PICTURE_FOR_PROFILE_FROM_GALLERY);
                    }
                });

                Button cancelButton = (Button) dialogView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

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
        dialog.dismiss();
        ImageView playerAvatarImageView = (ImageView) findViewById(R.id.playerAvatarImageView);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PICTURE_FOR_PROFILE_FROM_CAMERA && resultCode == RESULT_OK) {
            if (data != null) {
                Uri sourceUri = getImageFile(imageFilename);
                cropImage(sourceUri);
            }
        }
        else if (requestCode == PICK_PICTURE_FOR_PROFILE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
               Uri sourceUri = data.getData();
               cropImage(sourceUri);
            }
        }
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri imageUri = UCrop.getOutput(data);
            Glide.with(AddPlayerActivity.this).load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .skipMemoryCache(true)
                    .into(playerAvatarImageView);
        }
    }

    private Uri getImageFile(String fileName) {
        File primaryStorageVolume = new File(getExternalFilesDir(null), "images");
        if (!primaryStorageVolume.exists())
            primaryStorageVolume.mkdir();
        File imageFile = new File(primaryStorageVolume, fileName);
        return FileProvider.getUriForFile(AddPlayerActivity.this, getPackageName() + ".provider", imageFile);
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getExternalFilesDir(null), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.colorAccent));
        options.withAspectRatio(1, 1);
        options.withMaxResultSize(100, 100);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    @Override
    public void onResume() {
        super.onResume();
        permissionsGranted = CheckForPermissionsState.requestStorageCameraPermissions(AddPlayerActivity.this);
    }


}
