package com.boardgames.jaipur.ui.gamerule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class GameRuleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_rule, container, false);
       /* TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        ViewPager viewPager = root.findViewById(R.id.viewPager);
        TabAdapter tabAdapter = new TabAdapter(getParentFragmentManager());*/


        return root;
    }
}
