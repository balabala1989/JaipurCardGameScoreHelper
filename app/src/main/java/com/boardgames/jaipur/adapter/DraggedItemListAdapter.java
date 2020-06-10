package com.boardgames.jaipur.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.ui.newgame.DraggedItemsListViewModel;
import com.boardgames.jaipur.ui.rounds.RoundsCalculationActivity;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.CheckForPermissionsState;
import com.boardgames.jaipur.utils.GameUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DraggedItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class EmptyPlayerViewHolder extends RecyclerView.ViewHolder {
        private EmptyPlayerViewHolder(View view) {
            super(view);
        }
    }

    class DraggerItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView playerImageView;
        private ImageView draggedItemImageview;
        private ImageView deleteItemImageView;
        private TextView playerNameTextView;

        public DraggerItemViewHolder(View view) {
            super(view);
            playerImageView = view.findViewById(R.id.playerImageView);
            draggedItemImageview = view.findViewById(R.id.draggedItemImageView);
            deleteItemImageView = view.findViewById(R.id.deleteItemImageView);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
        }
    }

    private final LayoutInflater inflater;
    private ArrayList<String> dragAndDropOrder;
    private Context contextObj;
    private boolean isPermissionGranted;
    public static final int EMPTY_VIEW = 10;
    private View rootView;
    private DraggedItemsListViewModel draggedItemsListViewModel;
    private RoundsCalculationActivity roundsCalculationActivity;
    private String goodsInDisplay;

    public DraggedItemListAdapter(Activity activity, Context context, View rootView, DraggedItemsListViewModel draggedItemsListViewModel, String goodsInDisplay) {
        this.contextObj = context;
        inflater = LayoutInflater.from(context);
        isPermissionGranted = CheckForPermissionsState.requestStorageCameraPermissions(contextObj);
        this.rootView = rootView;
        this.draggedItemsListViewModel = draggedItemsListViewModel;
        roundsCalculationActivity = (RoundsCalculationActivity) activity;
        this.goodsInDisplay = goodsInDisplay;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == EMPTY_VIEW) {
            view = inflater.inflate(R.layout.recyclerview_dragged_item_list_empty_view, parent, false);
            return new EmptyPlayerViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.recyclerview_drag_dropped_list, parent, false);
            return new DraggerItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DraggerItemViewHolder) {
            DraggerItemViewHolder viewHolder = (DraggerItemViewHolder) holder;
            String[] splicedItem = dragAndDropOrder.get(position).split(ApplicationConstants.SEPARATOR_OF_VIEWS);
            if (position == 0) {
                viewHolder.deleteItemImageView.setVisibility(View.VISIBLE);
                viewHolder.deleteItemImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer playerScore = roundsCalculationActivity.getGoodsToPlayersScore().get(goodsInDisplay).get(Long.parseLong(splicedItem[0]));
                        Integer draggedItemValue = GameUtils.dragShadowResourceToValue.get(splicedItem[2]);
                        roundsCalculationActivity.getGoodsToPlayersScore().get(goodsInDisplay).put(Long.parseLong(splicedItem[0]), playerScore - draggedItemValue);
                        draggedItemsListViewModel.deleteSelectedItem();
                    }
                });
            }

            Glide.with(contextObj)
                    .load(roundsCalculationActivity.getPlayerToProfileUri().get(Long.parseLong(splicedItem[0])))
                    .into( viewHolder.playerImageView );
            viewHolder.playerNameTextView.setText(splicedItem[1]);
            Glide.with(contextObj)
                    .load(Integer.parseInt(splicedItem[2]))
                    .into(viewHolder.draggedItemImageview);
        }
    }

    @Override
    public int getItemCount() {
        if (dragAndDropOrder != null) {
            return dragAndDropOrder.size() > 0 ? dragAndDropOrder.size()  : 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (dragAndDropOrder.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setDragAndDropOrder(ArrayList<String> dragAndDropOrder) {
        this.dragAndDropOrder = dragAndDropOrder;
        notifyDataSetChanged();
    }
}
