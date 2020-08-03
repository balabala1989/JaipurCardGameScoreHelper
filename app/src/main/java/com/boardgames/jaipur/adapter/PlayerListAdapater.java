package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.ui.newgame.NewGameFragment;
import com.boardgames.jaipur.ui.playersmanagement.PlayerActivity;
import com.boardgames.jaipur.ui.playersmanagement.PlayersManagementFragment;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PlayerListAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerItemTextView;
        private final ImageView playerItemImageView;

        private PlayerViewHolder(View view) {
            super(view);
            playerItemTextView = view.findViewById(R.id.playersListTextView);
            playerItemImageView = view.findViewById(R.id.playersListImageView);
        }
    }

    class EmptyPlayerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView emptyPlayerImageView;
        private EmptyPlayerViewHolder(View view) {
            super(view);
            emptyPlayerImageView = view.findViewById(R.id.emptyPlayerImageView);
        }
    }

    private final LayoutInflater inflater;
    private List<Player> playersList;
    private Context contextObj;
    //private boolean isPermissionGranted;
    private Fragment parentFragment;
    public static final int EMPTY_VIEW = 10;
    private boolean isFloatingButtonVisible = true;
    private View rootView;

    public PlayerListAdapater(Fragment fragment, View rootView) {
        parentFragment = fragment;
        contextObj = fragment.getContext();
        inflater = LayoutInflater.from(contextObj);
        //isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(contextObj);
        this.rootView = rootView;

    }

   @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == EMPTY_VIEW) {
            view = inflater.inflate(R.layout.recycleview_empty_view, parent, false);
            return new EmptyPlayerViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.recyclerview_players_list, parent, false);
            return new PlayerViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlayerViewHolder) {
            if (playersList != null) {
              /* if (!isFloatingButtonVisible) {
                    FloatingActionButton addFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.addPlayerButton);
                    addFloatingActionButton.setVisibility(View.VISIBLE);
                    isFloatingButtonVisible = true;
                }*/
                PlayerViewHolder viewHolder = (PlayerViewHolder) holder;
                Player player = playersList.get(position);
                viewHolder.playerItemTextView.setText(player.getPlayerName());
                if (player.getId() == PlayerUtils.DEFAULT_PLAYER_ID) {
                    Glide.with(contextObj).load(R.drawable.add_player_icon).into(viewHolder.playerItemImageView);
                    viewHolder.playerItemTextView.setTextColor(contextObj.getResources().getColor(R.color.appBarColor));
                    viewHolder.playerItemImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (parentFragment instanceof NewGameFragment) {
                                NewGameFragment newGameFragment = (NewGameFragment) parentFragment;
                                newGameFragment.startActivityForPlayerHandling();
                            }
                            else if (parentFragment instanceof PlayersManagementFragment) {
                                PlayersManagementFragment playersManagementFragment = (PlayersManagementFragment) parentFragment;
                                playersManagementFragment.startActivityForPlayerHandling();
                            }
                        }
                    });
                }
                else {
                    viewHolder.playerItemTextView.setTextColor(Color.BLACK);
                    viewHolder.playerItemImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (parentFragment instanceof PlayersManagementFragment) {
                                PlayersManagementFragment playersManagementFragment = (PlayersManagementFragment) parentFragment;
                                playersManagementFragment.handleProfileImageClick(player);
                            } else if (parentFragment instanceof NewGameFragment) {
                                NewGameFragment newGameFragment = (NewGameFragment) parentFragment;
                                if (!(PlayerUtils.isPlayerOneSelected && PlayerUtils.isPlayerTwoSelected)) {
                                    newGameFragment.handleProfileImageClick(player);
                                }

                            }
                        }
                    });
                   /* if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equals("") || !isPermissionGranted) {
                        Glide.with(contextObj).load(R.drawable.default_player_avatar).into(viewHolder.playerItemImageView);
                    }*/
                    if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equals("")) {
                        Glide.with(contextObj).load(R.drawable.default_player_avatar).into(viewHolder.playerItemImageView);
                    }
                    else {
                        Bitmap imageMap = BitmapFactory.decodeFile(player.getPlayerAvatar());
                        Glide.with(contextObj).load(imageMap).into(viewHolder.playerItemImageView);
                    }
                }
            }
        }

        else if (holder instanceof EmptyPlayerViewHolder) {
            EmptyPlayerViewHolder viewHolder = (EmptyPlayerViewHolder) holder;
            Glide.with(contextObj).load(R.drawable.add_player_icon).into(viewHolder.emptyPlayerImageView);
           /* if (isFloatingButtonVisible) {
                FloatingActionButton addFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.addPlayerButton);
                addFloatingActionButton.setVisibility(View.INVISIBLE);
                isFloatingButtonVisible = false;
            }*/

            viewHolder.emptyPlayerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parentFragment instanceof NewGameFragment) {
                        NewGameFragment newGameFragment = (NewGameFragment) parentFragment;
                        newGameFragment.startActivityForPlayerHandling();
                    }
                    else if (parentFragment instanceof PlayersManagementFragment) {
                        PlayersManagementFragment playersManagementFragment = (PlayersManagementFragment) parentFragment;
                        playersManagementFragment.startActivityForPlayerHandling();
                    }
                }
            });
        }

    }

    public void setPlayersList(List<Player> players) {
        playersList = players;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (playersList != null) {
            return playersList.size() > 0 ? playersList.size()  : 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (playersList.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }



}
