package com.example.deblefer.Statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StatisticsSettings {

    private double minChanceOfGetting;
    private boolean drawWhenSameFigure;

    public StatisticsSettings(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.minChanceOfGetting=((double)Integer.valueOf(prefs.getString("pref_chanceOfGetting_limit","5")))/100;
        this.drawWhenSameFigure=prefs.getBoolean("pref_drawsCheckBox", false);
    }

    public StatisticsSettings(Double minChanceOfGetting, boolean drawWhenSameFigure){

        this.minChanceOfGetting = minChanceOfGetting;
        this.drawWhenSameFigure = drawWhenSameFigure;
    }

    public double getMinChanceOfGetting(){ return minChanceOfGetting; }

    public boolean isDrawWhenSameFigure() { return drawWhenSameFigure; }

}
