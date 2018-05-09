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
    private static List<Card> listOfCards = new ArrayList<>();
    private static Map<Card, Integer> cardsImagesId = null;

    static {
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit));

        listOfCards.addAll(cards);
        Collections.sort(listOfCards);

        if(!testHashesCode())
            throw new RuntimeException("Card hashes are bad");
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

    static class UninitializedArrayException extends RuntimeException{}

    public static int getCardImageId(Card card){
        if(cardsImagesId == null)
            throw new UninitializedArrayException();
        return cardsImagesId.get(card);
    }

    public static void initializeCardsImagesIds(Context context){
        cardsImagesId = new HashMap<>();
        for (Card card : cards){
            int id = context.getResources().getIdentifier(card.toString(), "drawable", context.getPackageName());
            cardsImagesId.put(card, id);
        }

    }

    public static Map<Card, Integer> getCardsImagesId() {
        return cardsImagesId;
    }

    static boolean testHashesCode(){
        ArrayList<Integer> hashes = new ArrayList<>();
        for(Card.Rank rank : Card.Rank.values()){
            for(Card.Suit suit : Card.Suit.values()){
                Card card = new Card(rank, suit);
                if(hashes.contains(card.hashCode()))
                    return false;
                hashes.add(card.hashCode());
            }
        }
        return true;
    }
}
