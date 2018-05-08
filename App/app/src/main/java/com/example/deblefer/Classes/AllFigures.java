package com.example.deblefer.Classes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class AllFigures {
    private static Collection<Figure> figures = new HashSet<>();
    static {

        List<Card.Rank> ranks = new ArrayList<>();

        //straight flush && straight
        Card.Rank[] allRanks = Card.Rank.values();
        for(int i = 12;i>=8;i--){ ranks.add(allRanks[i]); }
        figures.add(new Figure(ranks,true));
        figures.add(new Figure(ranks,false));
        while(ranks.get(0)!= Card.Rank.SIX){
            ranks.remove(0);
            ranks.add(allRanks[ranks.get(3).ordinal()-1]);
            figures.add(new Figure(ranks,true));
            figures.add(new Figure(ranks,false));
        }
        ranks.remove(0);
        ranks.add(Card.Rank.ACE);
        figures.add(new Figure(ranks,true));
        figures.add(new Figure(ranks,false));

        //flush
        ranks.clear();
        figures.add(new Figure(ranks,true));

        //full house
        for(Card.Rank r1 : Card.Rank.values()){
            ranks.add(r1); ranks.add(r1); ranks.add(r1);
            for(Card.Rank r2 : Card.Rank.values()){
                if(r1==r2) continue;
                ranks.add(r2); ranks.add(r2);
                figures.add(new Figure(ranks,false));
                ranks.remove(4); ranks.remove(3);
            }
            ranks.clear();
        }

        //four, three, two of a kind and high card
        for(Card.Rank r : Card.Rank.values()){
            for(int i=0;i<4;i++){
                ranks.add(r);
                figures.add(new Figure(ranks,false));
            }
            ranks.clear();
        }

    }

    public static Collection<Figure> getUnmodifableFigures(){ return Collections.unmodifiableCollection(figures); }

    public static Collection<Figure> getModifableFigures(){
        return new HashSet<>(figures);
    }

}
