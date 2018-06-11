package com.example.deblefer.Statistics;

import android.support.annotation.NonNull;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * nie nadaje sie do hashowania
 * */
public class Statistics implements Comparable<Statistics>{

    private Figure figure;
    private Double chanceToWin;
    private Double chanceToGetAsHighest;
    private Double chanceToGet;
    private Double chanceToDraw;
    private List<Card> usedCards;

    public Double getChanceOfGettingAsHighest() { return chanceToGetAsHighest; }

    public Double getChanceOfGetting() {
        return chanceToGet;
    }

    public Double getChanceOfWinning() { return chanceToWin; }

    public Double getChanceOfDrawing(){ return chanceToDraw; }

    public List<Card> getUsedCards(){return usedCards;}

    Statistics(Figure figure, double chanceToGetAsHighest, double chanceToWin, double chanceToDraw, Collection<Card> usedCards){
        this.figure = figure;
        this.chanceToGetAsHighest = chanceToGetAsHighest;
        this.chanceToGet = 0d;
        this.chanceToWin = chanceToWin;
        this.chanceToDraw = chanceToDraw;
        this.usedCards = new ArrayList<>(usedCards);
        Collections.sort(this.usedCards);
    }

    public Statistics setChanceToGet(Double chanceToGet) {
        this.chanceToGet = chanceToGet;
        return this;
    }

    public Figure getFigure() {
        return figure;
    }

    /*@Override
    public String toString() {
        return figure + " " + chanceToGetAsHighest + " " + chanceToWin + " " + chanceToDraw ;
    }*/

    @Override
    public String toString() {
        Figure figure = getFigure();
        Card.Rank[] vitalRanks = figure.getVitalRanks();
        String figureName = figure.getCategory().toString();
        switch (vitalRanks.length) {
            case 0: return figureName+
                    " of "+usedCards.get(0).getSuit().toString();
            case 1: return figureName+
                    " of "+usedCards.get(usedCards.size()-1).getRank().toString();
            default: return figureName+
                    " of "+vitalRanks[0].toString()+
                    " and "+vitalRanks[1].toString();
        }
    }

    @Override
    public int compareTo(@NonNull Statistics o) {
        if(Double.compare(chanceToWin*chanceToGetAsHighest, o.chanceToWin*o.chanceToGetAsHighest) == 0){
            if(Double.compare(chanceToGetAsHighest, o.chanceToGetAsHighest) == 0)
                return figure.compareTo(o.figure);
            return Double.compare(chanceToGetAsHighest, o.chanceToGetAsHighest);
        }
        return Double.compare(chanceToWin*chanceToGetAsHighest, o.chanceToWin*o.chanceToGetAsHighest);
    }


}