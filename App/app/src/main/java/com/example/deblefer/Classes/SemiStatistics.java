package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

public class SemiStatistics{
    protected Figure figure;
    protected Double chanceOfGetting;

    public SemiStatistics(Figure figure, double chanceOfGetting){
        this.figure = figure;
        this.chanceOfGetting = chanceOfGetting;
    }

    public Double getChanceOfGetting() {
        return chanceOfGetting;
    }

    public Figure getFigure() {
        return figure;
    }

}
