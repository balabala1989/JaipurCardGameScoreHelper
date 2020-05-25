package com.boardgames.jaipur.ui.playersmanagement;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerListAdapater;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PlayersManagementFragment extends Fragment {

    private PlayersManagementViewModel playersManagementViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        playersManagementViewModel =
                new ViewModelProvider(this).get(PlayersManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_players_management, container, false);
        FloatingActionButton addPlayerButton = root.findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE,
                        ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER);
                intent.putExtra(ApplicationConstants.PLAYERACTIVITY_TITLE, getString(R.string.playeractivity_title_for_add_player));
                startActivityForResult(intent,ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE);
            }
        });
        RecyclerView playerRecyclerView = root.findViewById(R.id.playersRecyclerView);
        final PlayerListAdapater adapater = new PlayerListAdapater(this, root);
        playerRecyclerView.setAdapter(adapater);
        playerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS));
        playersManagementViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                adapater.setPlayersList(players);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                int requestType = data.getIntExtra(ApplicationConstants.PLAYERACTIVITY_REQUEST_TYPE_REPLY, -1);

                //New Player response processing
                if (requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER) {
                    handleResultOKResponseForNewPlayer(data);
                    return;
                }
                //Update/Delete response processing
                else if (requestType == ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER) {
                    boolean deleteOperation = data.getBooleanExtra(ApplicationConstants.PLAYERACTIVITY_DELETE_OPERATION_REQUESTED, false);
                    if (deleteOperation) {
                        handleResultOKResponseForDeletePlayer(data);
                        return;
                    }
                    else {
                        handleResultOKResponseForUpdatePlayer(data);
                        return;
                    }
                }
            }
        }
        Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
    }

    private void handleResultOKResponseForNewPlayer(Intent dataIntent) {
        Uri uri = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY);
        String playerName = dataIntent.getStringExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY);
        Player newPlayer = new Player();
        try {
            if (uri != null) {
                File imageFile = getAvatarAbsolutePath(uri);
                newPlayer.setPlayerAvatar(imageFile.getAbsolutePath());
            }
            else {
                newPlayer.setPlayerAvatar("");
            }
            newPlayer.setPlayerName(playerName);
            newPlayer.setTimeCreated(System.currentTimeMillis()/100);
            newPlayer.setTimeUpdated(System.currentTimeMillis()/100);
            playersManagementViewModel.insert(newPlayer);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
            return;
        }


        Toast.makeText(getContext(), R.string.success_player_added, Toast.LENGTH_LONG).show();
    }

    private void handleResultOKResponseForDeletePlayer(Intent dataIntent) {
        Player player = (Player) dataIntent.getSerializableExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS);

        if (player == null){
            Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            playersManagementViewModel.delete(player);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getContext(), R.string.success_player_deleted, Toast.LENGTH_LONG).show();
    }

    private void handleResultOKResponseForUpdatePlayer(Intent dataIntent) {
        boolean isProfileChanged = dataIntent.getBooleanExtra(ApplicationConstants.PLAYERACTIVITY_UPDATE_OPERATION_PROFILE_IMAGE_CHANGED, false);
        Player player = (Player) dataIntent.getSerializableExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS);

        if (isProfileChanged) {
            Uri uri = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY);
            if (uri != null) {
                File imageFile;
                try {
                    imageFile = getAvatarAbsolutePath(uri);
                    player.setPlayerAvatar(imageFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
                    return;
                }

            }
            else {
                player.setPlayerAvatar("");
            }
        }

        player.setPlayerName(dataIntent.getStringExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PLAYER_NAME_REPLY));
        player.setTimeUpdated(System.currentTimeMillis()/100);

        try {
            playersManagementViewModel.update(player);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getContext(), R.string.success_player_updated, Toast.LENGTH_LONG).show();
    }

    private File getAvatarAbsolutePath(Uri uri) throws IOException {
        //Reducing the scale and size of the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, options);
        options.inSampleSize = calculateInSampleSize(options, 200, 200);
        options.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, options);

        File storageDir = new File (getContext().getExternalFilesDir(null), getString(R.string.playeractivity_external_path));
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

    private int calculateInSampleSize(
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
