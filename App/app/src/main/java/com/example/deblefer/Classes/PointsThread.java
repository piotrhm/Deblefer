package com.example.deblefer.Classes;

public class PointsThread extends Thread{
    private PointsLoadingRunner runner;

    public PointsThread(PointsLoadingRunner runner){
        super(runner);
        this.runner = runner;
    }

    public void setFinalText(String text){
        runner.setFinalText(text);
    }
}
