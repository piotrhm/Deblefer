package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

/*
* nie nadaje sie do hashowania
* */
public class Statistics implements Comparable<Statistics>{

    private Figure figure;
    private Double chanceOfWinning;
    private Double chanceOfGetting;

    public Double getChanceOfGetting() { return chanceOfGetting; }

    public Double getChanceOfWinning() { return chanceOfWinning; }

    Statistics(Figure figure, double chanceOfGetting, double chanceOfWinning){
        this.figure = figure;
        this.chanceOfGetting = chanceOfGetting;
        this.chanceOfWinning = chanceOfWinning;
    }


    @Override
    public String toString() {
        return figure + " " + chanceOfGetting+ " " + chanceOfWinning;
    }

    @Override
    public int compareTo(@NonNull Statistics o) {
        if(Double.compare(chanceOfWinning, o.chanceOfWinning) == 0){
            if(Double.compare(chanceOfGetting, o.chanceOfWinning) == 0)
                return figure.compareTo(o.figure);
            return Double.compare(chanceOfGetting, o.chanceOfGetting);
        }
        return Double.compare(chanceOfWinning, o.chanceOfWinning);
    }

}
