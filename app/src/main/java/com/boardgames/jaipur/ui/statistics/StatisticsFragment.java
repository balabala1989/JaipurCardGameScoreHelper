package com.boardgames.jaipur.ui.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boardgames.jaipur.R;
import com.boardgames.jaipur.adapter.PlayerStatsListAdapter;
import com.boardgames.jaipur.utils.PlayerStatistics;
import com.boardgames.jaipur.utils.PlayerUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatisticsFragment extends Fragment implements OnChartValueSelectedListener {

    private StatisticsViewModel statisticsViewModel;
    private PieChart pieChart;
    private int totalGames = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        pieChart = (PieChart) root.findViewById(R.id.winPercentagePieChart);

       RecyclerView playerStatsRecyclerView = root.findViewById(R.id.playerStatsRecyclerView);
        final PlayerStatsListAdapter playerStatsListAdapter = new PlayerStatsListAdapter(this);
        playerStatsRecyclerView.setAdapter(playerStatsListAdapter);
        playerStatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        statisticsViewModel.getAllPlayersStats().observe(getViewLifecycleOwner(), playerStatistics -> {
            Collections.sort(playerStatistics, new Comparator<PlayerStatistics>() {
                @Override
                public int compare(PlayerStatistics o1, PlayerStatistics o2) {
                    return o1.getPlayer().getPlayerName().compareToIgnoreCase(o2.getPlayer().getPlayerName());
                }
            });
            playerStatsListAdapter.setPlayerStatisticsList(playerStatistics);
            setDataForChart(playerStatistics);
        });

        statisticsViewModel.getTotalGames().observe(getViewLifecycleOwner(), totalGamesPlayed -> {
            totalGames = totalGamesPlayed;
            pieChart.setCenterText(generateCenterSpannableText(String.valueOf(totalGamesPlayed)));
            pieChart.invalidate();
        } );

        defineInitialChart();
        return root;
    }

    private void defineInitialChart() {
        Typeface bioRhyme = ResourcesCompat.getFont(getContext(), R.font.biorhyme);
        Typeface cairoSemiBold = ResourcesCompat.getFont(getContext(), R.font.cairo_semibold);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(bioRhyme);
        pieChart.setCenterText(generateCenterSpannableText("0"));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(cairoSemiBold);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setOnChartValueSelectedListener(this);

        ;
    }

    private SpannableString generateCenterSpannableText(String numberOfGames) {

        SpannableString s = new SpannableString("Games\n" + numberOfGames);
        s.setSpan(new RelativeSizeSpan(2.1f), 0, 5, 0);
        s.setSpan(new RelativeSizeSpan(3.7f), 5, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 5, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 5, s.length(), 0);
        return s;
    }

    private void setDataForChart(List<PlayerStatistics> playerStatisticsList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

       for (PlayerStatistics playerStatistics : playerStatisticsList) {
           if (playerStatistics.getGamesPlayed() == 0 || totalGames == 0)
               continue;
            float winPercentage = (playerStatistics.getGamesPlayed() * 100) / totalGames;
            entries.add(new PieEntry(winPercentage, playerStatistics.getPlayer().getPlayerName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.quando));
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.animateXY(1400, 1400);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PlayerUtils.getHeightOfScreenByHalf(getActivity()));
        pieChart.setLayoutParams(layoutParams);
        pieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
