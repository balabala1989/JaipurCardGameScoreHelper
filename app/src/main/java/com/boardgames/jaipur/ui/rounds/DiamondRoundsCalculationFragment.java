package com.boardgames.jaipur.ui.rounds;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boardgames.jaipur.R;


public class DiamondRoundsCalculationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_diamond_rounds_calculation, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.color_activity_actionbar))));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.diamond_calculation_fragment_label));
        return root;
    }
}
