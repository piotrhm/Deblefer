package com.example.deblefer.Classes;

public class StatisticsSettings {

    private double minChanceOfGetting;
    private boolean drawWhenSameFigure;

    public StatisticsSettings(double minChanceOfGetting, boolean drawWhenSameFigure){
        this.minChanceOfGetting=minChanceOfGetting;
        this.drawWhenSameFigure=drawWhenSameFigure;
    }

    public double getMinChanceOfGetting(){ return minChanceOfGetting; }

    public boolean isDrawWhenSameFigure() { return drawWhenSameFigure; }

}
