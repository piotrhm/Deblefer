package com.example.deblefer.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class StatisticsGenerator {
    private Collection<Card> hand;
    private Collection<Card> table;
    private Collection<Card> unused;
    private Collection<SemiStatistics> semiStatistics = new ArrayList<>();
    private Collection<Statistics> fullStatistics = new ArrayList<>();


    private StatisticsGenerator(Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        this.hand = new ArrayList<>(hand);
        this.table = new ArrayList<>(table);
        this.unused = new HashSet<>(unused);
    }

    public static Collection<Statistics> getStatistics(Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        StatisticsGenerator generator = new StatisticsGenerator(hand, table, unused);
        generator.generateSemiStatistics();
        generator.generateFullStatistics();
        return generator.fullStatistics;
    }

    private void generateSemiStatistics(){
        // generuje ci szanse na zdobycie danych figur
        // semistatistics to bedzie superklasa statistics ale tylko z polem figure i chanceOfGetting
    }
    private void generateFullStatistics(){
        // dogenerowujesz korzystajac byc moze z semiStatistics watosci chanceOfWinningWithFigure
    }
}
