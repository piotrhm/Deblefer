package com.example.deblefer.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.Activities.TexasModuleActivity;
import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.R;
import com.example.deblefer.Statistics.Statistics;
import com.example.deblefer.Statistics.StatisticsSettings;

import java.util.List;

public class StatsSmallDialog{

    private Statistics stats;
    private Activity activity;

    public StatsSmallDialog(Activity activity, Statistics statistics){
        this.activity = activity;
        this.stats = statistics;
    }

    private int getCardId(List<Card> listOfCards, int i){
        return listOfCards.size() <= i ? R.drawable.unused : Deck.getCardImageId(listOfCards.get(i));
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.closer_statistics_view, null);
        ((TextView)view.findViewById(R.id.textViewGetting))
                .setText(String.format("%.2f", stats.getChanceOfGetting()*100)+"%");
        ((TextView)view.findViewById(R.id.textViewAsHighest))
                .setText(String.format("%.2f", stats.getChanceOfGettingAsHighest()*100)+"%");
        ((TextView)view.findViewById(R.id.textViewWinning))
                .setText(String.format("%.2f", stats.getChanceOfWinning()*100)+"%");

        if(!new StatisticsSettings(activity).isDrawWhenSameFigure()){
            ((TextView)view.findViewById(R.id.textViewDrawing))
                    .setText("-");
        }
        else
            ((TextView)view.findViewById(R.id.textViewDrawing))
                    .setText(String.format("%.2f", stats.getChanceOfDrawing()*100)+"%");

        ((TextView)view.findViewById(R.id.textViewFigure))
                .setText(stats.toString());

        ((ImageView)view.findViewById(R.id.closerStatsImage1))
                .setImageResource(getCardId(stats.getUsedCards(), 0));
        ((ImageView)view.findViewById(R.id.closerStatsImage2))
                .setImageResource(getCardId(stats.getUsedCards(), 1));
        ((ImageView)view.findViewById(R.id.closerStatsImage3))
                .setImageResource(getCardId(stats.getUsedCards(), 2));
        ((ImageView)view.findViewById(R.id.closerStatsImage4))
                .setImageResource(getCardId(stats.getUsedCards(), 3));
        ((ImageView)view.findViewById(R.id.closerStatsImage5))
                .setImageResource(getCardId(stats.getUsedCards(), 4));

        builder.setView(view);
        return builder.create();
    }
}