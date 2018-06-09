package com.example.deblefer.Cards;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Deck {
    private static Set<Card> cards = new HashSet<>();
    private static List<Card> listOfCards = new ArrayList<>();
    private static Map<Card, Integer> cardsImagesId = null;
    private static List<List<Card>> fourCardsList = new ArrayList<>();

    static {
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit));

        listOfCards.addAll(cards);
        Collections.sort(listOfCards);
    }

    public static Set<Card> getUnmodifableDeckAsSet(){
        return Collections.unmodifiableSet(cards);
    }

    public static Set<Card> getModifableDeckAsSet(){
        return new HashSet<>(cards);
    }

    public static List<Card> getUnmodifableDeckAsList(){
        return Collections.unmodifiableList(listOfCards);
    }

    public static List<Card> getModifableDeckAsList(){
        return new ArrayList<>(listOfCards);
    }

    static class UninitializedArrayException extends RuntimeException{}

    public static int getCardImageId(Card card){
        return cardsImagesId.get(card);
    }

    public static void initializeCardsImagesIds(Context context){
        cardsImagesId = new HashMap<>();
        for (Card card : cards){
            int id = context.getResources().getIdentifier(card.toString(), "drawable", context.getPackageName());
            cardsImagesId.put(card, id);
        }

    }

}
