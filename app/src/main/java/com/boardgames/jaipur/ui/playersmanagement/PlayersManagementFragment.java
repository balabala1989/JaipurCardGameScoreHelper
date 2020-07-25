package com.boardgames.jaipur.ui.playersmanagement;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerListAdapater;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayersManagementFragment extends Fragment {

    private PlayersManagementViewModel playersManagementViewModel;
    private AdView mAdView;

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
        playersManagementViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                adapater.setPlayersList(players);
            }
        });
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = root.findViewById(R.id.managePlayermainActivityAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                    PlayerUtils.handleResultOKResponseForNewPlayer(getContext(), playersManagementViewModel, data);
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

    private void handleResultOKResponseForDeletePlayer(Intent dataIntent) {
        Player player = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS);

        if (player == null){
            Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            playersManagementViewModel.deleteAPlayerWithGamesRounds(player).observe(getViewLifecycleOwner(), deletedRowsCount ->  {
                Log.e("Rows deleted - " + String.valueOf(deletedRowsCount), "DELETE_PLAYER");
            });
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
        Player player = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS);

        if (isProfileChanged) {
            Uri uri = dataIntent.getParcelableExtra(ApplicationConstants.PLAYERACTIVITY_TO_PLAYERSMANAGEMENTFRAGMENT_ADD_PLAYER_PROFILE_IMAGE_URI_REPLY);
            if (uri != null) {
                File imageFile;
                try {
                    imageFile = PlayerUtils.getAvatarAbsolutePath(getContext(), uri, getString(R.string.playeractivity_external_path));
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
        player.setTimeUpdated(System.currentTimeMillis());

        try {
            playersManagementViewModel.update(player);
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.error_player_not_saved, Toast.LENGTH_LONG).show();
            return;
        }

        GameUtils.clearCache(getContext());
        Toast.makeText(getContext(), R.string.success_player_updated, Toast.LENGTH_LONG).show();
    }

    public void handleProfileImageClick(Player player) {
        Intent updateIntent = new Intent(getContext(), PlayerActivity.class);
        updateIntent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE,
                ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_UPDATE_PLAYER);
        updateIntent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_PLAYER_DETAILS, (Parcelable) player);
        updateIntent.putExtra(ApplicationConstants.PLAYERACTIVITY_TITLE, getString(R.string.playeractivity_title_for_edit_player));
        startActivityForResult(updateIntent, ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE);
    }
}
