package com.example.deblefer.Classes;

import android.content.Context;
import android.util.SparseArray;

import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Deck {
    private static Set<Card> cards = new HashSet<>();
    private static Map<Face, Card> anticards = new TreeMap<>();
    private static List<Card> listOfCards = new ArrayList<>();
    private static SparseArray<Integer> cardsImagesId = null;

    static {
        int id = 1;
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit, id++));

        for (Card card : cards){
            anticards.put(card.getMyFace(), card);
        }

        listOfCards.addAll(cards);
        Collections.sort(listOfCards);
    }
    /*
    * it's likely not to be sorted (#android misteries)
    * */
    public static Set<Card> getUnmodifableDeckAsSet(){
        return Collections.unmodifiableSet(cards);
    }
    /*
     * it's likely not to be sorted (#android misteries)
     * */
    public static Set<Card> getModifableDeckAsSet(){
        return new HashSet<>(cards);
    }
    /*
     * it's sorted as sorted array can be
     * */
    public static List<Card> getUnmodifableDeckAsList(){
        return Collections.unmodifiableList(listOfCards);
    }
    /*
     * it's sorted as sorted array can be
     * */
    public static List<Card> getModifableDeckAsList(){
        return new ArrayList<>(listOfCards);
    }

    /*
    * moze sie przydac
    * */
    public static Card getCard(Card.Rank rank, Card.Suit suit){
        return anticards.get(new Face(rank, suit));
    }

    public static Card getCard(Face face){
        return anticards.get(face);
    }

    private static class UninitializedArrayException extends RuntimeException{}

    public static int getCardImageId(Card card){
        if(cardsImagesId == null)
            throw new UninitializedArrayException();
        return cardsImagesId.get(card.getId());
    }

    public static void initializeCardsImagesIds(Context context){
        cardsImagesId = new SparseArray<>();
        for (Card card : cards){
            int id = context.getResources().getIdentifier(card.toString(), "drawable", context.getPackageName());
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

    public static SparseArray<Integer> getCardsImagesId() {
        return cardsImagesId;
    }
}
