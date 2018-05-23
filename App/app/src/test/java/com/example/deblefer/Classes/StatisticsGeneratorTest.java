/*
package com.example.deblefer.Classes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class StatisticsGeneratorTest {
    @Test
    public void statisticsGeneratorTest(){
        long startTime = System.currentTimeMillis();
        List<Card> unused = new ArrayList<>(Deck.getModifableDeckAsList());
        List<Card> hand = new ArrayList<>();
        List<Card> table = new ArrayList<>();
        Collections.sort(unused);

        hand.add(unused.get(19)); unused.remove(19);
        hand.add(unused.get(50)); unused.remove(50);
        table.add(unused.get(34)); unused.remove(34);
        table.add(unused.get(unused.size()-45)); unused.remove(unused.size()-45);
       // table.add(unused.get(15)); unused.remove(15);
       // table.add(unused.get(10)); unused.remove(10);
        table.add(unused.get(unused.size()-20)); unused.remove(unused.size()-20);

        StatisticsSettings statisticsSettings = new StatisticsSettings(0.2,true);

        List<Statistics> stats = new ArrayList<>(StatisticsGenerator.getStatistics(2,hand,table,unused,statisticsSettings));
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time: "+elapsedTime);
        double one = 0;
        System.out.println();
        System.out.println("STATISTICS:");
            for(Statistics s : stats){
            System.out.println(s);
            System.out.println(s.getUsedCards());
            one+=s.getChanceOfGetting();
        }
        System.out.println();
        System.out.println("TABLE:"+table);
        System.out.println("HAND: "+hand);
//        assertTrue(one<1.001 && one>.999);

    }
}*/
