package com.example.deblefer.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.deblefer.Activities.TexasModuleActivity;
import com.example.deblefer.Adapters.CardRecyclerViewAdapter;
import com.example.deblefer.Cards.Card;
import com.example.deblefer.R;

import java.util.Collection;

public class GetCardsDialog{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TexasModuleActivity activity;
    private Collection<Card> cards;

    public GetCardsDialog(TexasModuleActivity activity, CardRecyclerViewAdapter adapter){
        this.activity = activity;
        this.adapter = adapter;
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("SELECT CARDS");
        recyclerView = new RecyclerView(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        builder.setView(recyclerView);
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("OK", (dialog, which) -> activity.approveChosenCardsInDialog());
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
