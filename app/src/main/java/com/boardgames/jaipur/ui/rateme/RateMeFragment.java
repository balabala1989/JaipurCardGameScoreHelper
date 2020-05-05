package com.boardgames.jaipur.ui.rateme;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boardgames.jaipur.R;

public class RateMeFragment extends Fragment {

    private RateMeViewModel rateMeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rateMeViewModel =
                ViewModelProviders.of(this).get(RateMeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rate_me, container, false);
        final TextView textView = root.findViewById(R.id.text_rateme);
        rateMeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
