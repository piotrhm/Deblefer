package com.example.deblefer.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.deblefer.Activities.TexasModuleActivity;
import com.example.deblefer.Adapters.CardRecyclerViewAdapterSingle;
import com.example.deblefer.Cards.CardInDialog;

import java.util.List;

public class GetCardsDialog{

    private CardRecyclerViewAdapterSingle adapter;
    private TexasModuleActivity activity;

    public GetCardsDialog(TexasModuleActivity activity, List<CardInDialog> listOfCardsInDialog, int maxCardsCount){
        this.activity = activity;
        this.adapter = new CardRecyclerViewAdapterSingle(activity, listOfCardsInDialog , maxCardsCount);
    }

    static class CacheGridManager extends GridLayoutManager{

        CacheGridManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        protected int getExtraLayoutSpace(RecyclerView.State state) {
            return 3500;
        }
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        RecyclerView recyclerView = new RecyclerView(builder.getContext());
        GridLayoutManager manager = new CacheGridManager(activity, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(52);
        recyclerView.setNestedScrollingEnabled(false);
        builder.setTitle("SELECT CARDS")
                .setView(recyclerView)
                .setNegativeButton("CANCEL",
                        (dialog, which) -> activity.rejectChosenCardsInDialog())
                .setPositiveButton("OK", (dialog, which) -> {
                    if(adapter.getChosenCount() > 0)
                        activity.approveChosenCardsInDialog();
                });
        return builder.create();
    }
}
