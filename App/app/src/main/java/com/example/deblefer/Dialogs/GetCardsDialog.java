package com.example.deblefer.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.deblefer.Activities.TexasModuleActivity;
import com.example.deblefer.Adapters.CardRecyclerViewAdapter;
import com.example.deblefer.Adapters.CardRecyclerViewAdapterImproved;
import com.example.deblefer.Cards.CardInDialog;

public class GetCardsDialog{

    private RecyclerView recyclerView;
    private CardRecyclerViewAdapterImproved adapter;
    private TexasModuleActivity activity;

    public GetCardsDialog(TexasModuleActivity activity, CardRecyclerViewAdapterImproved adapter){
        this.activity = activity;
        this.adapter = adapter;
    }

    public class CacheGridManager extends GridLayoutManager{

        public CacheGridManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        @Override
        protected int getExtraLayoutSpace(RecyclerView.State state) {
            return 3500;
        }
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("SELECT CARDS");
        recyclerView = new RecyclerView(builder.getContext());
        GridLayoutManager manager = new CacheGridManager(activity, 4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(52);
        recyclerView.setNestedScrollingEnabled(false);
        builder.setView(recyclerView);
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            activity.rejectChosenCardsInDialog();
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(adapter.getChosenCount() > 0)
                activity.approveChosenCardsInDialog();
        });
        return builder.create();
    }
}
