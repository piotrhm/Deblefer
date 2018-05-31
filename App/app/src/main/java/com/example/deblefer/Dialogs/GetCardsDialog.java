package com.example.deblefer.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.deblefer.Activities.TexasModuleActivity;
import com.example.deblefer.Adapters.CardRecyclerViewAdapter;
import com.example.deblefer.Adapters.CardRecyclerViewAdapterImproved;
import com.example.deblefer.Cards.Card;
import com.example.deblefer.R;

import java.util.Collection;

public class GetCardsDialog{

    private RecyclerView recyclerView;
    private CardRecyclerViewAdapterImproved adapter;
    private TexasModuleActivity activity;
    private Collection<Card> cards;

    public GetCardsDialog(TexasModuleActivity activity, CardRecyclerViewAdapterImproved adapter){
        this.activity = activity;
        this.adapter = adapter;
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("SELECT CARDS");
        recyclerView = new RecyclerView(builder.getContext());
        GridLayoutManager manager = new GridLayoutManager(activity, 4);
        manager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        builder.setView(recyclerView);
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            activity.rejectChosenCardsInDialog();
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(adapter.getChosenCount() > 0)
                activity.approveChosenCardsInDialog();
        });
        /*LayoutInflater layoutInflater = activity.getLayoutInflater();
        View scrollView = layoutInflater.inflate(R.layout.getcarddialog, null);
        recyclerView = scrollView.findViewById(R.id.recyclerViewCards);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new CardRecyclerViewAdapter(activity);
        recyclerView.setAdapter(adapter);
        builder.setView(R.layout.getcarddialog);*/
        return builder.create();
    }
}
