package com.example.deblefer.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.Dialogs.StatsSmallDialog;
import com.example.deblefer.Statistics.Statistics;
import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsViewAdapter extends RecyclerView.Adapter<StatisticsViewAdapter.ViewHolder> {

    private List<Statistics> listOfStats;
    private Activity activity;

    public StatisticsViewAdapter(List<Statistics> listOfStats, Activity activity) {
        this.activity = activity;
        this.listOfStats = listOfStats;
    }

    public void addItem(Statistics stat){
        int pos = listOfStats.size();
        for (int i = 0; i < listOfStats.size(); i++) {
            if(listOfStats.get(i).compareTo(stat) < 0){
                pos = i;
                break;
            }
        }
        listOfStats.add(pos, stat);
        notifyItemInserted(pos);
    }

    public void clearItems(){
        int count = listOfStats.size();
        listOfStats.clear();
        notifyItemRangeRemoved(0, count);
    }

    @NonNull
    @Override
    public StatisticsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull StatisticsViewAdapter.ViewHolder holder, int position) {
        Statistics stat = listOfStats.get(position);
        List<Card> listOfCards = stat.getUsedCards();
        holder.textView.setText(stat.getFigure().getCategory().toString());
        holder.textView2.setText(String.format("%.2f", stat.getChanceOfGetting()*100)+"%");
        holder.textView3.setText(String.format("%.2f", stat.getChanceOfWinning()*100)+"%");
        int i = 0;
        for(ImageView imageView : holder.cardImages)
            imageView.setImageResource(getCardId(listOfCards, i++));
        holder.itemView.setOnClickListener(v -> new StatsSmallDialog(activity, stat).getDialog().show());
    }

    @Override
    public int getItemCount() {
        return listOfStats.size();
    }

    private int getCardId(List<Card> listOfCards, int i){
        return listOfCards.size() <= i ? R.drawable.unused : Deck.getCardImageId(listOfCards.get(i));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        List<ImageView> cardImages = new ArrayList<>();
        public TextView textView;
        public TextView textView2;
        public TextView textView3;

        ViewHolder(View itemView) {
            super(itemView);
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow1));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow2));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow3));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow4));
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView5));
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textView3);
        }
    }
}