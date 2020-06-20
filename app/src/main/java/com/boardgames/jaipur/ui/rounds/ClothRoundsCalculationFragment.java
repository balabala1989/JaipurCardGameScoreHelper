package com.boardgames.jaipur.ui.rounds;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.ApplicationConstants;
import com.boardgames.jaipur.utils.GameUtils;
import com.boardgames.jaipur.utils.ImageDragShadowBuilder;

import java.util.ArrayList;


public class ClothRoundsCalculationFragment extends Fragment implements View.OnDragListener, View.OnTouchListener {

    private int trackCoinsUsed = 0;
    private ImageView playerOneImageView;
    private ImageView playerTwoImageView;
    private ImageView clothImageView;
    private int[] dragShadowResources = {R.drawable.a4_2_cloth_5_drag_shadow,
            R.drawable.a4_4_cloth_3_drag_shadow,
            R.drawable.a4_4_cloth_3_drag_shadow,
            R.drawable.a4_7_cloth_2_drag_shadow,
            R.drawable.a4_7_cloth_2_drag_shadow,
            R.drawable.a4_10_cloth_1_drag_shadow,
            R.drawable.a4_10_cloth_1_drag_shadow};
    private int[] displayImageViewResources = {R.drawable.a4_3_cloth_33_22_11_imageview,
            R.drawable.a4_5_cloth_3_22_11_imageview,
            R.drawable.a4_6_cloth_22_11_imageview,
            R.drawable.a4_8_cloth_2_11_imageview,
            R.drawable.a4_9_cloth_11_imageview,
            R.drawable.a4_10_cloth_1_drag_shadow,
            -1};
    private ArrayList<String> dragAndDropOrder;
    private RoundsCalculationActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cloth_rounds_calculation, container, false);


        mainActivity = (RoundsCalculationActivity) getActivity();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mainActivity.getRoundTitle() + getString(R.string.cloth_calculation_fragment_label));

        GameUtils.loadPlayerDetailsInDisplay(ClothRoundsCalculationFragment.this, root, mainActivity.getGameDetails());
        View roundCalUserLayout = root.findViewById(R.id.roundUsersDisplay);
        playerOneImageView = roundCalUserLayout.findViewById(R.id.playerOneImageView);
        playerTwoImageView = roundCalUserLayout.findViewById(R.id.playerTwoImageView);
        clothImageView = root.findViewById(R.id.clothImageView);

        clothImageView.setOnTouchListener(this);
        playerOneImageView.setOnDragListener(this);
        playerTwoImageView.setOnDragListener(this);

        dragAndDropOrder = new ArrayList<>();


        playerOneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleProfileImageClick();
            }
        });

        playerTwoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleProfileImageClick();
            }
        });

        setHasOptionsMenu(true);

        GameUtils.resetOrInitializeActivityVars(mainActivity, ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);

        return root;

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_EXITED: {
                ((ImageView) v).setBackgroundResource(R.drawable.image_border_drag_started);
                v.invalidate();
                return true;
            }

            case DragEvent.ACTION_DRAG_ENTERED: {
                ((ImageView) v).setBackgroundResource(R.drawable.image_border_drag_entered);
                v.invalidate();
                return true;
            }

            case DragEvent.ACTION_DRAG_LOCATION: {
                return true;
            }

            case DragEvent.ACTION_DROP: {
                String playerName;
                long playerId;
                if (v.getId() == playerOneImageView.getId()) {
                    playerName = mainActivity.getGameDetails().getPlayersInAGame().getPlayerOne().getPlayerName();
                    playerId = mainActivity.getGameDetails().getPlayersInAGame().getPlayerOne().getId();
                }
                else {
                    playerName = mainActivity.getGameDetails().getPlayersInAGame().getPlayerTwo().getPlayerName();
                    playerId = mainActivity.getGameDetails().getPlayersInAGame().getPlayerTwo().getId();
                }
                GameUtils.computeScoreForDraggedItem(getActivity(),
                        playerId,
                        dragShadowResources[trackCoinsUsed],
                        playerName,
                        ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS,
                        dragAndDropOrder);

                if (displayImageViewResources[trackCoinsUsed] == -1)
                    clothImageView.setVisibility(View.INVISIBLE);
                else
                    clothImageView.setImageResource(displayImageViewResources[trackCoinsUsed]);

                trackCoinsUsed++;
                v.invalidate();
                return true;
            }

            case DragEvent.ACTION_DRAG_ENDED: {
                if (!event.getResult()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.drag_drop_error), Toast.LENGTH_SHORT).show();
                }
                ((ImageView) v).setBackgroundResource(R.drawable.image_border);
                return true;
            }

            default:
                return false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ClipData data = ClipData.newPlainText("", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(data, ImageDragShadowBuilder.fromResource(getContext(), dragShadowResources[trackCoinsUsed]), null, 0);
        } else {
            v.startDrag(data, ImageDragShadowBuilder.fromResource(getContext(), dragShadowResources[trackCoinsUsed]), null, 0);
        }
        return false;
    }

    private void handleProfileImageClick() {
        GameUtils.displayDraggedItemsForRemoval(getActivity(), getContext(),
                ClothRoundsCalculationFragment.this, dragAndDropOrder, ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);
    }

    public void handleAlertDialogCloseButton() {
        trackCoinsUsed = dragAndDropOrder.size() - 1;

        if (clothImageView.getVisibility() == View.INVISIBLE)
            clothImageView.setVisibility(View.VISIBLE);
        if (trackCoinsUsed == -1)
            clothImageView.setImageResource(R.drawable.a4_1_cloth_full_imageview);
        else {
            if (displayImageViewResources[trackCoinsUsed] == -1) {
                clothImageView.setVisibility(View.INVISIBLE);
            }
            else {
                clothImageView.setImageResource(displayImageViewResources[trackCoinsUsed]);
            }
        }

        trackCoinsUsed++;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_round_cal_next_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ((RoundsCalculationActivity)getActivity()).getNavController().navigate(R.id.action_clothRoundsCalculationFragment_to_silverRoundsCalculationFragment);
            return true;
        }
        else if (item.getItemId() == R.id.nextRoundsCalculationFragment) {
            GameUtils.handleNextButtonClick(getActivity(), ApplicationConstants.ROUNDS_CALC_CLOTH_GOODS);

            mainActivity.getNavController().navigate(R.id.action_silverRoundsCalculationFragment_to_goldRoundsCalculationFragment);
        }
        return super.onOptionsItemSelected(item);
    }
}
