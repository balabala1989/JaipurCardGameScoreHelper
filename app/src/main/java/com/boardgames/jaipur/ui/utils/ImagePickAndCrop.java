package com.boardgames.jaipur.ui.utils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.yalantis.ucrop.UCrop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImagePickAndCrop extends AppCompatActivity {

    private static final String TAG = ImagePickAndCrop.class.getSimpleName();
    public static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    public static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    public static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    public static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    public static final String INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality";
    public static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    public static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    public static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";

    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;
    public static String fileName;
    private boolean permissionsGranted = false;
    private static AlertDialog dialog;

    public interface PickerOptionListener {
        void onCameraSelected();
        void onGallerySelected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick_and_crop);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_activity_image_pick_crop);

        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.image_picker_issue), Toast.LENGTH_LONG).show();
            return;
        }

        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X);
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y);
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION);
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false);
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false);
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth);
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight);

        int requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1);
        if (requestCode == ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE) {
            takeCameraImage();
        } else {
            chooseImageFromGallery();
        }
    }

    public static void showOptions(Context context, PickerOptionListener listener, String titleForDialog) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_image_picker_alertdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleForDialog);
        builder.setView(dialogView);
        dialog = builder.create();

        ImageView cameraImageView = (ImageView) dialogView.findViewById(R.id.cameraImageView);
        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraSelected();
            }
        });

        ImageView galleryImageView = (ImageView) dialogView.findViewById(R.id.galleryImageView);
        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGallerySelected();
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

    private void takeCameraImage() {
        dialog.dismiss();
        //if (CheckForPermissionsState.requestStorageCameraPermissions(ImagePickAndCrop.this)) {
        fileName = System.currentTimeMillis() + ".jpg";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,getImageFile(fileName));
        startActivityForResult(takePictureIntent,ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE);
       /* }
        else {
            CheckForPermissionsState.deniedPermission(ImagePickAndCrop.this);
        }*/
    }

    private void chooseImageFromGallery() {
        dialog.dismiss();
        //if (CheckForPermissionsState.requestStorageCameraPermissions(ImagePickAndCrop.this)) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE);
        /*}
        else {
            CheckForPermissionsState.deniedPermission(ImagePickAndCrop.this);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_CAMERA_CAPTURE:
                if (resultCode == RESULT_OK)
                    cropImage(getImageFile(fileName));
                else
                    handleResultCancelled();
            break;

            case ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_REQUEST_FOR_GALLERY_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    cropImage(imageUri);
                }
                else
                    handleResultCancelled();
            break;

            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK)
                    handleUCropResult(data);
                else
                    handleResultCancelled();
            break;

            case UCrop.RESULT_ERROR:
                Throwable ucropError = UCrop.getError(data);
                Log.e(TAG,"Crop Error: " + ucropError);
                handleResultCancelled();
            break;

            default:
                handleResultCancelled();
        }
    }

    private void handleUCropResult(Intent data) {
        if (data == null)
            handleResultCancelled();
        final Uri uri = UCrop.getOutput(data);
        handleResultOK(uri);
    }

    private void handleResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void handleResultOK(Uri imageUri) {
        Intent intent = new Intent();
        intent.putExtra(ApplicationConstants.PLAYERACTIVITY_TO_IMAGEPICKANDCROPACTIVITY_URI_PATH_REPLY, imageUri);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private Uri getImageFile(String fileName) {
        File primaryStorageVolume = new File(getExternalCacheDir(), "jaipurImages");
        if (!primaryStorageVolume.exists())
            primaryStorageVolume.mkdir();
        File imageFile = new File(primaryStorageVolume, fileName);
        return FileProvider.getUriForFile(ImagePickAndCrop.this, getPackageName() + ".provider", imageFile);
    }

    private void cropImage(Uri sourceUri) {
        File primaryStorageVolume = new File(getExternalCacheDir(), "jaipurImages");
        if (!primaryStorageVolume.exists())
            primaryStorageVolume.mkdir();
        Uri destinationUri = Uri.fromFile(new File(primaryStorageVolume, System.currentTimeMillis() + ".jpg"));
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setActiveControlsWidgetColor(Color.RED);
        options.setToolbarWidgetColor(Color.WHITE);
        options.withAspectRatio(1, 1);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }
}
