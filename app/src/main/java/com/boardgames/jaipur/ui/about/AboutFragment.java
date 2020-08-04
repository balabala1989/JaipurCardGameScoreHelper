package com.boardgames.jaipur.ui.about;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boardgames.jaipur.BuildConfig;
import com.boardgames.jaipur.R;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class AboutFragment extends Fragment {

    private String iconText = "Icons made by <a href=\"https://www.flaticon.com/authors/popcorns-arts\" title=\"Icon Pond\" style=\"color:black\">Icon Pond</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\"> www.flaticon.com</a>";
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        int width = PlayerUtils.getWidthforImageViewByOneThird(getActivity());
        Glide.with(getContext()).load(R.mipmap.ic_launcher_foreground).override(width).into((ImageView) root.findViewById(R.id.iconImageView));
        ((TextView) root.findViewById(R.id.appNameTextView)).setText(getString(R.string.app_name).replaceAll("-","\n"));
        ((TextView) root.findViewById(R.id. versionTextView)).setText("Version:- " + BuildConfig.VERSION_NAME);

        SpannableString spannableString = new SpannableString("developed by  Balaji Rajagopal\nboardgamesdeveloper@gmail.com");
        spannableString.setSpan(new RelativeSizeSpan(.8f), 0, 14, 0);
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, 14, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 14, 0);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 14, 30, 0);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 14, 30, 0);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.appBarColor)), 14, 30, 0);
        spannableString.setSpan(new RelativeSizeSpan(.8f), 31, spannableString.length(), 0);
        spannableString.setSpan(new UnderlineSpan(), 31, spannableString.length(), 0);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 31, spannableString.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 31, spannableString.length(), 0);

        ((TextView) root.findViewById(R.id. developerTextView)).setText(spannableString);

        TextView iconCourtsey = root.findViewById(R.id.iconCourtseyTextView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            iconCourtsey.setText(Html.fromHtml(iconText, Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            iconCourtsey.setText(Html.fromHtml(iconText));
        }
        iconCourtsey.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.icon_url)));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = root.findViewById(R.id.aboutAppAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return root;
    }
}
