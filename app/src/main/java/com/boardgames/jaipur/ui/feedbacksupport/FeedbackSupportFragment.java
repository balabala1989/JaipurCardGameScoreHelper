package com.boardgames.jaipur.ui.feedbacksupport;

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

public class FeedbackSupportFragment extends Fragment {

    private FeedbackSupportViewModel feedbackSupportViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        feedbackSupportViewModel =
                ViewModelProviders.of(this).get(FeedbackSupportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feedback_support, container, false);
        final TextView textView = root.findViewById(R.id.text_feedbacksupport);
        feedbackSupportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
