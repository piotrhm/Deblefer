package com.example.deblefer.Statistics;

import android.support.v7.widget.RecyclerView;

import com.example.deblefer.Cards.Card;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StatisticsGeneratorBox{
    private StatisticsGeneratorEngine generator;
    public StatisticsGeneratorBox(Collection<Card> hand, Collection<Card> table, Collection<Card> unused, int players, StatisticsSettings statisticsSettings, RecyclerView statisticsRecyclerView){
        generator = new StatisticsGeneratorEngine(players, hand, table, unused, statisticsSettings, statisticsRecyclerView);
    }
    public List<Statistics> getStatistics() throws InterruptedException {
        generator.generateStatistics();
        return generator.getStatistics();
    }

    public void interrupt(){
        generator.interrupt();
    }

    public static Double getChanceOfWinning(List<Statistics> statisticsList){
        return StatisticsGeneratorEngine.getChanceOfWinning(statisticsList);
    }
}

