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
    private Double chanceOfGet;
    private Double chanceToDraw;
    private List<Card> usedCards;

    public Double getChanceOfGetting() { return chanceToGetAsHighest; }

    public Double getChanceOfWinning() { return chanceToWin; }

    public Double getChanceOfDraw(){ return chanceToDraw; }

    public List<Card> getUsedCards(){return usedCards;}

    Statistics(Figure figure, double chanceToGetAsHighest, double chanceToWin, double chanceToDraw, Collection<Card> usedCards){
        this.figure = figure;
        this.chanceToGetAsHighest = chanceToGetAsHighest;
        this.chanceToWin = chanceToWin;
        this.chanceToDraw = chanceToDraw;
        this.usedCards = new ArrayList<>(usedCards);
        Collections.sort(this.usedCards);
    }

    public Figure getFigure() {
        return figure;
    }

    @Override
    public String toString() {
        return figure + " " + chanceToGetAsHighest + " " + chanceToWin + " " + chanceToDraw ;
    }

    @Override
    public int compareTo(@NonNull Statistics o) {
        if(Double.compare(chanceToWin* chanceToGetAsHighest, o.chanceToWin*o.chanceToGetAsHighest) == 0){
            if(Double.compare(chanceToGetAsHighest, o.chanceToGetAsHighest) == 0)
                return figure.compareTo(o.figure);
            return Double.compare(chanceToGetAsHighest, o.chanceToGetAsHighest);
        }
        return Double.compare(chanceToWin* chanceToGetAsHighest, o.chanceToWin*o.chanceToGetAsHighest);
    }


}