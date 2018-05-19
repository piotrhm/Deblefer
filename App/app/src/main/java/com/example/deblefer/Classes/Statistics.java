package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

import java.util.Collection;

/*
 * nie nadaje sie do hashowania
 * */
public class Statistics implements Comparable<Statistics>{

    private Figure figure;
    private Double chanceToWin;
    private Double chanceToGet;
    private Double chanceToDraw;
    private Collection<Card> usedCards;

    public Double getChanceOfGetting() { return chanceToGet; }

    public Double getChanceOfWinning() { return chanceToWin; }

    public Double getChanceOfDraw(){ return chanceToDraw; }

    public Collection<Card> getUsedCards(){return usedCards;}

    Statistics(Figure figure, double chanceToGet, double chanceToWin, double chanceToDraw, Collection<Card> usedCards){
        this.figure = figure;
        this.chanceToGet = chanceToGet;
        this.chanceToWin = chanceToWin;
        this.chanceToDraw = chanceToDraw;
        this.usedCards = usedCards;
    }
    @Override
    public String toString() {
        return figure + " " + chanceToGet+ " " + chanceToWin + " " + chanceToDraw ;
    }

    @Override
    public int compareTo(@NonNull Statistics o) {
        if(Double.compare(chanceToWin*chanceToGet, o.chanceToWin*o.chanceToGet) == 0){
            if(Double.compare(chanceToGet, o.chanceToGet) == 0)
                return figure.compareTo(o.figure);
            return Double.compare(chanceToGet, o.chanceToGet);
        }
        return Double.compare(chanceToWin*chanceToGet, o.chanceToWin*o.chanceToGet);
    }


}