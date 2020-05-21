package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.entities.Player;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerListAdapater extends RecyclerView.Adapter<PlayerListAdapater.PlayerViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerItemTextView;
        private final ImageView playerItemImageView;
        public static final int THUMBNAIL_SIZE = 96;

        private PlayerViewHolder(View view) {
            super(view);
            playerItemTextView = view.findViewById(R.id.playersListTextView);
            playerItemImageView = view.findViewById(R.id.playersListImageView);
        }
    }

    private final LayoutInflater inflater;
    private List<Player> playersList;
    private Context contextObj;
    private boolean isPermissionGranted;

    public PlayerListAdapater(Context context) {
        inflater = LayoutInflater.from(context);
        contextObj = context;
        isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(context);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_players_list, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        if(playersList != null) {
            Player player = playersList.get(position);

            holder.playerItemTextView.setText(player.getPlayerName());
            if (player.getPlayerAvatar() != null && player.getPlayerAvatar().equals("") || !isPermissionGranted) {
                Glide.with(contextObj).load(R.drawable.default_player_avatar).into(holder.playerItemImageView);
            }
            else {
                Bitmap imageMap = BitmapFactory.decodeFile(player.getPlayerAvatar());
                Glide.with(contextObj).load(imageMap).into(holder.playerItemImageView);
            }


        }
        else {
            holder.playerItemTextView.setText("No Players Found....");
        }

    }

    public void setPlayersList(List<Player> players) {
        playersList = players;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (playersList != null) {
            return playersList.size();
        }
        return 0;
    }



}
