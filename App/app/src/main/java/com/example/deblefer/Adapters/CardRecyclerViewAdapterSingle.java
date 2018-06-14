package com.example.deblefer.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deblefer.Cards.CardInDialog;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardRecyclerViewAdapterSingle extends  RecyclerView.Adapter<CardRecyclerViewAdapterSingle.ViewHolder>{

    private List<CardInDialog> listOfCards;
    private Activity activity;
    private int maxCardsCount;
    private int chosen = 0;

    public int getChosenCount(){
        return chosen;
    }

    public CardRecyclerViewAdapterSingle(Activity activity, List<CardInDialog> listOfCardsInDialog, int maxCardsCount){
        this.activity = activity;
        this.listOfCards = listOfCardsInDialog;
        this.maxCardsCount = maxCardsCount;
    }

    @NonNull
    @Override
    public CardRecyclerViewAdapterSingle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.card_image_row, parent, false);
        return new CardRecyclerViewAdapterSingle.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecyclerViewAdapterSingle.ViewHolder holder, int position) {
        CardInDialog cardInDialog = listOfCards.get(position);
        ImageView cardImage = holder.cardImage;
        if (cardInDialog.isUsed()){
            Picasso.with(activity).load(R.drawable.unused).noFade().into(cardImage);
//            cardImage.setImageResource(R.drawable.unused);
        }
        else {
            Picasso.with(activity).load(Deck.getCardImageId(cardInDialog.getCard())).noFade().into(cardImage);
//            cardImage.setImageResource(Deck.getCardImageId(cardInDialog.getCard()));
            cardImage.setOnClickListener(v -> onClick(cardInDialog, cardImage));
        }
    }

    private synchronized void onClick(CardInDialog cardInDialog, ImageView cardImage){
        if(!cardInDialog.isChosen() && chosen == maxCardsCount){
            Toast.makeText(activity, "too many cards!", Toast.LENGTH_SHORT).show();
            return;
        }
        float scale = 1.0f;
        cardInDialog.setChosen(!cardInDialog.isChosen());
        if(cardInDialog.isChosen()){
            scale = 1.15f;
            chosen++;
        }
        else
            chosen--;
        cardImage.setScaleX(scale);
        cardImage.setScaleY(scale);
    }

    @Override
    public int getItemCount() {
        return listOfCards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;

        ViewHolder(View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.justCard);
        }
    }
}
