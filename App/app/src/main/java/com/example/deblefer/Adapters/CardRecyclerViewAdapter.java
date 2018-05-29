package com.example.deblefer.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.CardInDialog;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardRecyclerViewAdapter extends  RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder>{

    private List<List<CardInDialog>> listOfFours;
    private Activity activity;
    private int maxCardsCount;
    private int chosen = 0;

    public CardRecyclerViewAdapter(Activity activity, List<List<CardInDialog>> listOfFours, int maxCardsCount){
        this.activity = activity;
        this.listOfFours = listOfFours;
        this.maxCardsCount = maxCardsCount;
    }

    @NonNull
    @Override
    public CardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cards_row, parent, false);
        return new CardRecyclerViewAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecyclerViewAdapter.ViewHolder holder, int position) {
        List<CardInDialog> cards = listOfFours.get(position);
        int i = 0;
        for (ImageView cardImage : holder.cardImages){
            CardInDialog cardInDialog = cards.get(i++);

            if (cardInDialog.isUsed()){
                Picasso.with(activity).load(R.drawable.unused).into(cardImage);
                continue;
            }

            Picasso.with(activity).load(Deck.getCardImageId(cardInDialog.getCard())).noFade().into(cardImage);
            cardImage.setOnClickListener((View v) -> {
                if(!cardInDialog.isChosen() && chosen == maxCardsCount){
                    Toast.makeText(activity, "too many cards!", Toast.LENGTH_SHORT).show();
                    return;
                }
                float scale = 1.0f;
                cardInDialog.setChosen(!cardInDialog.isChosen());
                if(cardInDialog.isChosen()){
                    scale = 1.1f;
                    chosen++;
                }
                else
                    chosen--;
                cardImage.setScaleX(scale);
                cardImage.setScaleY(scale);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listOfFours.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        List<ImageView> cardImages = new ArrayList<>();

        public ViewHolder(View itemView) {
            super(itemView);
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow1));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow2));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow3));
            cardImages.add((ImageView) itemView.findViewById(R.id.cardInRow4));
        }
    }
}
