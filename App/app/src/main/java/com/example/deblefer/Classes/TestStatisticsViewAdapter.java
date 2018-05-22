package com.example.deblefer.Classes;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.List;

public class TestStatisticsViewAdapter extends RecyclerView.Adapter<TestStatisticsViewAdapter.ViewHolder> {

    private List<Statistics> listOfStats;

    public TestStatisticsViewAdapter(List<Statistics> listOfStats) {
        this.listOfStats = listOfStats;
    }

    @NonNull
    @Override
    public TestStatisticsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull TestStatisticsViewAdapter.ViewHolder holder, int position) {
        Statistics stat = listOfStats.get(position);
        List<Card> listOfCards = stat.getUsedCards();
        holder.textView.setText(stat.getFigure().getCategory().toString());
        holder.textView2.setText(String.format("%.2f", stat.getChanceOfGetting()));
        holder.textView3.setText(String.format("%.2f", stat.getChanceOfWinning()));
        int i = 0;
        for(ImageView imageView : holder.cardImages)
            imageView.setImageResource(getCardId(listOfCards, i++));
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

        public ViewHolder(View itemView) {
            super(itemView);
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView));
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView2));
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView3));
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView4));
            cardImages.add((ImageView) itemView.findViewById(R.id.imageView5));
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.textView2 = (TextView) itemView.findViewById(R.id.textView2);
            this.textView3 = (TextView) itemView.findViewById(R.id.textView3);
        }
    }
}