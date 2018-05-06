package com.example.deblefer.Classes;

import android.content.Context;
import android.util.SparseArray;

import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Deck {
    private static Collection<Card> cards = new HashSet<>();
    private static List<Card> suitAlternateCards = new ArrayList<>();
    private static SparseArray<Integer> cardsImagesId = null;
    static {
        int id = 1;
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit, id++));

        for (Card.Suit suit : Card.Suit.values())
            for (Card.Rank rank : Card.Rank.values())
                cards.add(new Card(rank, suit, getCardId(rank, suit)));
    }
    public static Collection<Card> getUnmodifableDeck(){
        return Collections.unmodifiableCollection(cards);
    }

    public static Collection<Card> getModifableDeck(){
        return new HashSet<>(cards);

    }

    public static List<Card> getUnmodifableSuitAlternateDeck(){
        return Collections.unmodifiableList(suitAlternateCards);
    }

    /*
    * moze sie przydac
    * */

    public static Card getCard(Card.Rank rank, Card.Suit suit){
        return new Card(rank, suit, getCardId(rank, suit));
    }

    public static class UninitializedArrayException extends RuntimeException{}

    public static int getCardImageId(Card card){
        if(cardsImagesId == null)
            throw new UninitializedArrayException();
        return cardsImagesId.get(card.getId());
    }

    public static void initializeCardsImagesIds(Context context){
        cardsImagesId = new SparseArray<>();
        for (Card card : cards){
            int id = context.getResources().getIdentifier(card.toString()+".png", "drawable", context.getPackageName());
            cardsImagesId.append(card.getId(), id);
        }

    }

    public static int getCardId(Card.Rank rank, Card.Suit suit){
        for (Card card : cards){
            if(card.getRank() == rank && card.getSuit() == suit)
                return card.getId();
        }
        return 0;
    }

}
