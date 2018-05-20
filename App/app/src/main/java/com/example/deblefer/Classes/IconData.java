package com.example.deblefer.Classes;

import com.example.deblefer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class IconData {
    private String figure;
    private String stat1;
    private String stat2;
    private List<Card> cards = new ArrayList<>();

    public IconData(Collection<Card> usedCards, Figure f, Double chanceOfWinning, Double chanceOfGetting) {
        cards.addAll(usedCards);
        Collections.sort(cards);
        figure = f.getCategory().toString();
        stat1 = String.format( "%.2f", chanceOfWinning );
        stat2 = String.format( "%.2f", chanceOfGetting );
    }

    public String getFigure() { return figure; }

    public String getStat1() {
        return stat1;
    }

    public String getStat2() {
        return stat2;
    }

    public int getImgId() {
        if(0 < cards.size())
            return Deck.getCardImageId(cards.get(0));
        return R.drawable.unused;
    }

    public int getImgId2() {
        if(1 < cards.size())
            return Deck.getCardImageId(cards.get(1));
        return R.drawable.unused;
    }

    public int getImgId3() {
        if(2 < cards.size())
            return Deck.getCardImageId(cards.get(2));
        return R.drawable.unused;
    }

    public int getImgId4() {
        if(3 < cards.size())
            return Deck.getCardImageId(cards.get(3));
        return R.drawable.unused;
    }

    public int getImgId5() {
        if(4 < cards.size())
            return Deck.getCardImageId(cards.get(4));
        return R.drawable.unused;
    }

}