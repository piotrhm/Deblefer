package com.example.deblefer.Classes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class StatisticsGeneratorTest {
    @Test
    public void statisticsGeneratorTest(){
        List<Card> unused = new ArrayList<>(Deck.getModifableDeckAsList());
        List<Card> hand = new ArrayList<>();
        List<Card> table = new ArrayList<>();
        Collections.sort(unused);

        hand.add(unused.get(unused.size()-51)); unused.remove(unused.size()-51);
        hand.add(unused.get(unused.size()-50)); unused.remove(unused.size()-50);
        table.add(unused.get(unused.size()-32)); unused.remove(unused.size()-32);
        table.add(unused.get(unused.size()-45)); unused.remove(unused.size()-45);
        table.add(unused.get(unused.size()-20)); unused.remove(unused.size()-20);
       // table.add(unused.get(unused.size()-20)); unused.remove(unused.size()-20);


        List<Statistics> stats = new ArrayList<>(StatisticsGenerator.getStatistics(3,hand,table,unused));
        double one = 0;
        System.out.println();
        System.out.println("STATISTICS:");
            for(Statistics s : stats){
            System.out.println(s);
            one+=s.getChanceOfGetting();
        }
        System.out.println();
        System.out.println("TABLE:"+table);
        System.out.println("HAND: "+hand);
        assertTrue(one<1.001 && one>.999);

    }
}