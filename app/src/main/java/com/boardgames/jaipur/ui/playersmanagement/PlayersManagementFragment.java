package com.boardgames.jaipur.ui.playersmanagement;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerListAdapater;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlayersManagementFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 100;
    private PlayersManagementViewModel playersManagementViewModel;
    private final int NUMBER_OF_COLUMNS = 3;
    public static final int ADD_PLAYER_ACTIVITY_REQUEST_CODE = 1;

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
                Intent intent = new Intent(getActivity(), AddPlayerActivity.class);
                startActivityForResult(intent,ADD_PLAYER_ACTIVITY_REQUEST_CODE);
            }
        });
        RecyclerView playerRecyclerView = root.findViewById(R.id.playersRecyclerView);
        final PlayerListAdapater adapater = new PlayerListAdapater(getContext());
        playerRecyclerView.setAdapter(adapater);
        playerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS));
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
        if (requestCode == ADD_PLAYER_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Player newPlayer = (Player) data.getExtras().getSerializable(AddPlayerActivity.ADD_PLAYER_REPLY);
            playersManagementViewModel.insert(newPlayer);
        }
        else {
            Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
        }
    }
}
