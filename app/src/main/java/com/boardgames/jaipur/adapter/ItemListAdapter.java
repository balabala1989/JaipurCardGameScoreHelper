package com.boardgames.jaipur.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameDetails;
import com.boardgames.jaipur.utils.GameUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class ItemListViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;
        private TextView playerNameTextView;

        public ItemListViewHolder(View view) {
            super(view);
            itemImageView = view.findViewById(R.id.singleItemImageView);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
        }
    }

    private final LayoutInflater inflater;
    private ArrayList<String> itemsList;
    private Context contextObj;
    private String goodsInDisplay;
    private HashMap<String, Long> goodsToPlayer;
    private long selectedPlayerID;
    private GameDetails gameDetails;

    public ItemListAdapter(Context context, HashMap<String, Long> goodsToPlayer, ArrayList<String> itemsList, String goodsInDisplay, long selectedPlayerID, GameDetails gameDetails) {
        this.contextObj = context;
        this.goodsToPlayer = goodsToPlayer;
        this.itemsList = itemsList;
        this.goodsInDisplay = goodsInDisplay;
        this.selectedPlayerID = selectedPlayerID;
        this.gameDetails = gameDetails;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_goods_display_round_calculation, parent, false);
        ItemListViewHolder holder = new ItemListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String[] splitItemData = itemsList.get(position).split(ApplicationConstants.SEPARATOR_OF_VIEWS);
        if (holder instanceof ItemListViewHolder) {
            ItemListViewHolder itemListViewHolder = (ItemListViewHolder) holder;
            itemListViewHolder.itemImageView.setImageDrawable(contextObj
                    .getResources()
                    .getDrawable(GameUtils.goodsToItemsToImage.get(goodsInDisplay).get(splitItemData[0])));
            itemListViewHolder.itemImageView.setTag(splitItemData[0]);

            long playerID = Long.parseLong(splitItemData[1]);
            if (playerID == ApplicationConstants.DEFAULT_PLAYER_ID) {
                itemListViewHolder.playerNameTextView.setText("");
            }
            else if (playerID == gameDetails.getPlayersInAGame().getPlayerOne().getId()) {
                itemListViewHolder.playerNameTextView.setText(gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName());
            }
            else {
                itemListViewHolder.playerNameTextView.setText(gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName());
            }

            if (playerID != ApplicationConstants.DEFAULT_PLAYER_ID && playerID != selectedPlayerID) {
                itemListViewHolder.itemImageView.setClickable(false);
            }
            else {
                itemListViewHolder.itemImageView.setClickable(true);
                itemListViewHolder.itemImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tagValue = (String) v.getTag();
                        if (goodsToPlayer.get(tagValue) == ApplicationConstants.DEFAULT_PLAYER_ID) {
                            goodsToPlayer.put(tagValue, selectedPlayerID);
                            String playerName = selectedPlayerID == gameDetails.getPlayersInAGame().getPlayerOne().getId()
                                    ? gameDetails.getPlayersInAGame().getPlayerOne().getPlayerName()
                                    : gameDetails.getPlayersInAGame().getPlayerTwo().getPlayerName();
                            itemListViewHolder.playerNameTextView.setText(playerName);
                        }
                        else {
                            goodsToPlayer.put(tagValue, ApplicationConstants.DEFAULT_PLAYER_ID);
                            itemListViewHolder.playerNameTextView.setText("");
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (itemsList != null)
            return itemsList.size();
        return 0;
    }
}
