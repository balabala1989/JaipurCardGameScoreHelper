package com.boardgames.jaipur.ui.newgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerListAdapater;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.playersmanagement.PlayerActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NewGameFragment extends Fragment {

    private NewGameViewModel newGameViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        newGameViewModel = new ViewModelProvider(this).get(NewGameViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_game, container, false);
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
        RecyclerView playerRecyclerView = root.findViewById(R.id.playerListNewGameRecyclerView);
        final PlayerListAdapater adapater = new PlayerListAdapater(this, root);
        playerRecyclerView.setAdapter(adapater);
        playerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS));
        newGameViewModel.getAllPlayers().observe(this, new Observer<List<Player>>() {
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
                    PlayerUtils.handleResultOKResponseForNewPlayer(getContext(), newGameViewModel, data);
                    return;
                }
            }
        }
        Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
    }

    public void handleProfileImageClick(Player player) {

    }
}
