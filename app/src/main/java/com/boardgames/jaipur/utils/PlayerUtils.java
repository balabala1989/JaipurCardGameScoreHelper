package com.boardgames.jaipur.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.newgame.NewGameViewModel;
import com.boardgames.jaipur.ui.playersmanagement.PlayersManagementViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayerUtils {

    public static boolean isPlayerOneSelected = false;
    public static boolean isPlayerTwoSelected = false;

    public static void handleResultOKResponseForNewPlayer(Context context, AndroidViewModel androidViewModel, Intent dataIntent) {
        Uri uri = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY);
        String playerName = dataIntent.getStringExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY);
        Player newPlayer = new Player();
        try {
            if (uri != null) {
                File imageFile = getAvatarAbsolutePath(context, uri);
                newPlayer.setPlayerAvatar(imageFile.getAbsolutePath());
            }
            else {
                newPlayer.setPlayerAvatar("");
            }
            newPlayer.setPlayerName(playerName);
            newPlayer.setTimeCreated(System.currentTimeMillis()/100);
            newPlayer.setTimeUpdated(System.currentTimeMillis()/100);
            if (androidViewModel instanceof PlayersManagementViewModel) {
                PlayersManagementViewModel playersManagementViewModel = (PlayersManagementViewModel) androidViewModel;
                playersManagementViewModel.insert(newPlayer);
            }
            else if (androidViewModel instanceof NewGameViewModel) {
                NewGameViewModel newGameViewModel = (NewGameViewModel) androidViewModel;
                newGameViewModel.insert(newPlayer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(context, R.string.success_player_added, Toast.LENGTH_LONG).show();
    }

    public static File getAvatarAbsolutePath(Context context, Uri uri) throws IOException {
        //Reducing the scale and size of the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        options.inSampleSize = calculateInSampleSize(options, 200, 200);
        options.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

        File storageDir = new File (context.getExternalFilesDir(null), context.getString(R.string.playeractivity_external_path));
        if (!storageDir.exists()) {storageDir.mkdir();}
        File imageFile = new File(storageDir, System.currentTimeMillis() + ".jpg");
        imageFile.createNewFile();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByteArray = outputStream.toByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
        fileOutputStream.write(imageByteArray);
        fileOutputStream.flush();
        fileOutputStream.close();

        return imageFile;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
