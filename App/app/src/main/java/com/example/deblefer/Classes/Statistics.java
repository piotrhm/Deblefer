package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

/*
* nie nadaje sie do hashowania
* */
public class Statistics extends SemiStatistics implements Comparable<Statistics>{

    private Double chanceOfWinningWithFigure;
    private Double chanceOfWinning;

    public Double getChanceOfWinningWithFigure() {
        return chanceOfWinningWithFigure;
    }

    public Statistics(Figure figure, double chanceOfGetting, double chanceOfWinning){
        super(figure, chanceOfGetting);
        this.chanceOfWinningWithFigure = chanceOfWinning;
        this.chanceOfWinning = chanceOfWinning*chanceOfGetting;
    }

    public Statistics(SemiStatistics semiStats, double chanceOfWinning){
        this(semiStats.figure, semiStats.chanceOfGetting, chanceOfWinning);
    }

    @Override
    public String toString() {
        return figure + " " + chanceOfGetting + " " + chanceOfWinningWithFigure;
    }

    @Override
    public int compareTo(@NonNull Statistics o) {
        if(Double.compare(chanceOfWinning, o.chanceOfWinning) == 0){
            if(Double.compare(chanceOfGetting, o.chanceOfGetting) == 0)
                return figure.compareTo(o.figure);
            return Double.compare(chanceOfGetting, o.chanceOfGetting);
        }
        return Double.compare(chanceOfWinning, o.chanceOfWinning);
    }
}
