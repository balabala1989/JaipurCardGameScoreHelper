package com.boardgames.jaipur.ui.newgame;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
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
import com.boardgames.jaipur.utils.PlayersInAGame;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

public class NewGameFragment extends Fragment {

    private NewGameViewModel newGameViewModel;
    private View root;
    private Player playerOne, playerTwo;
    private ImageView playerOneImageView, playerTwoImageView;
    private TextView playerOneTexView, playerTwoTextView;
    private MenuItem startMenuItem;

    //TODO on click of the select player, remove him from the selected option and make select player empty
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PlayerUtils.isPlayerOneSelected = false;
        PlayerUtils.isPlayerTwoSelected = false;
        newGameViewModel = new ViewModelProvider(this).get(NewGameViewModel.class);
        root = inflater.inflate(R.layout.fragment_new_game, container, false);

        playerOneImageView = root.findViewById(R.id.playerOneImageView);
        playerTwoImageView = root.findViewById(R.id.playerTwoImageView);
        playerOneTexView = root.findViewById(R.id.playerOneTextView);
        playerTwoTextView = root.findViewById(R.id.playerTwoTextView);

        playerOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayerUtils.isPlayerOneSelected)
                    return;
                synchronized (this) {
                    PlayerUtils.isPlayerOneSelected = false;
                }
                playerOne = null;
                handleUnSelectPlayers(playerOneImageView, playerOneTexView);
            }
        });

        playerTwoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayerUtils.isPlayerTwoSelected)
                    return;
                synchronized (this) {
                    PlayerUtils.isPlayerTwoSelected = false;
                }
                playerTwo = null;
                handleUnSelectPlayers(playerTwoImageView, playerTwoTextView);
            }
        });

        /*FloatingActionButton addPlayerButton = root.findViewById(R.id.addPlayerButton);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE,
                        ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER);
                intent.putExtra(ApplicationConstants.PLAYERACTIVITY_TITLE, getString(R.string.playeractivity_title_for_add_player));
                startActivityForResult(intent,ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE);
            }
        });*/
        RecyclerView playerRecyclerView = root.findViewById(R.id.playerListNewGameRecyclerView);
        final PlayerListAdapater adapter = new PlayerListAdapater(this, root);
        playerRecyclerView.setAdapter(adapter);
        playerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_GRIDLAYOUT_NUMBER_OF_COLUMNS));
        newGameViewModel.getAllPlayers().observe(getViewLifecycleOwner(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                players.add(PlayerUtils.defaultPlayer);
                adapter.setPlayersList(players);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.activity_new_game_menu, menu);
        startMenuItem = menu.findItem(R.id.startNewGameButton);
        startMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.startNewGameButton) {
            if (playerOne == null || playerTwo == null) {
                Toast.makeText(getContext(), getString(R.string.new_game_fragment_select_player_error_message), Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            Intent startIntent = new Intent(getActivity(), StartingPlayerActivity.class);
            PlayersInAGame playersInAGame = new PlayersInAGame(playerOne, playerTwo);

            if (playerOne.getPlayerAvatar() == null || playerOne.getPlayerAvatar().equalsIgnoreCase(""))
                playersInAGame.setPlayerOneProfile(PlayerUtils.getUriForDrawable(getContext(), R.drawable.default_player_avatar));
            else {
                playersInAGame.setPlayerOneProfile(Uri.fromFile(new File(playerOne.getPlayerAvatar())));
            }

            if (playerTwo.getPlayerAvatar() == null || playerTwo.getPlayerAvatar().equalsIgnoreCase(""))
                playersInAGame.setPlayerTwoProfile(PlayerUtils.getUriForDrawable(getContext(), R.drawable.default_player_avatar));
            else {
                playersInAGame.setPlayerTwoProfile(Uri.fromFile(new File(playerTwo.getPlayerAvatar())));
            }
            startIntent.putExtra(ApplicationConstants.NEWGAMEFRAGMENT_TO_STARTINGPLAYERACTIVITY_PLAYERS_IN_A_GAME, playersInAGame);
            startActivity(startIntent);
        }
        return super.onOptionsItemSelected(item);
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
        else if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        Toast.makeText(getContext(), R.string.error_player_not_added, Toast.LENGTH_LONG).show();
    }

    public void handleProfileImageClick(Player player) {
       if (!PlayerUtils.isPlayerOneSelected) {

            if (PlayerUtils.isPlayerTwoSelected) {
                if (playerTwo != null && playerTwo.getId() == player.getId())
                    return;
            }

            if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equalsIgnoreCase("")) {
                Glide.with(getContext()).load(R.drawable.default_player_avatar).into(playerOneImageView);
            }
            else {
                Bitmap imageMap = BitmapFactory.decodeFile(player.getPlayerAvatar());
                Glide.with(getContext()).load(imageMap).into(playerOneImageView);
            }
            playerOneTexView.setText(player.getPlayerName());
            synchronized (this) {
                playerOne = player;
                PlayerUtils.isPlayerOneSelected = true;
            }
        }
        else if (!PlayerUtils.isPlayerTwoSelected) {

            if (PlayerUtils.isPlayerOneSelected) {
                if (playerOne != null && playerOne.getId() == player.getId())
                    return;
            }

           if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equalsIgnoreCase("")) {
                Glide.with(getContext()).load(R.drawable.default_player_avatar).into(playerTwoImageView);
            }
            else {
                Bitmap imageMap = BitmapFactory.decodeFile(player.getPlayerAvatar());
                Glide.with(getContext()).load(imageMap).into(playerTwoImageView);
            }
            playerTwoTextView.setText(player.getPlayerName());
            synchronized (this) {
                playerTwo = player;
                PlayerUtils.isPlayerTwoSelected = true;
            }
        }

        if (PlayerUtils.isPlayerOneSelected && PlayerUtils.isPlayerTwoSelected)
            startMenuItem.setVisible(true);
    }

    private void handleUnSelectPlayers(ImageView playerImageView, TextView playerTextView) {
        startMenuItem.setVisible(false);
        Glide.with(getContext()).load(R.drawable.player_question).into(playerImageView);
        playerTextView.setText(getString(R.string.new_game_fragment_select_player_textView));
    }

    public void startActivityForPlayerHandling() {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_TYPE,
                ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_FOR_NEW_PLAYER);
        intent.putExtra(ApplicationConstants.PLAYERACTIVITY_TITLE, getString(R.string.playeractivity_title_for_add_player));
        startActivityForResult(intent,ApplicationConstants.PLAYERMANAGEMENTFRAGMENT_TO_PLAYERACTIVITY_REQUEST_CODE);
    }
}
