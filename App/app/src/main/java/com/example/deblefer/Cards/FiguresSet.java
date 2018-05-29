package com.example.deblefer.Cards;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FiguresSet {
    private static List<Figure> figuresList = new ArrayList<>();
    static {

        //straight flush && straight
        List<Card.Rank> ranks = new ArrayList<>(Arrays.asList(Card.Rank.ACE,Card.Rank.KING,Card.Rank.QUEEN,Card.Rank.JACK,Card.Rank.TEN));
        List<Card.Rank> allRanks = new ArrayList<>(Arrays.asList(Card.Rank.values()));
        Collections.sort(allRanks);
        Card.Rank[] vitalRanks = new Card.Rank[]{Card.Rank.ACE};
        figuresList.add(new Figure(ranks,true, Figure.Category.STRAIGHT_FLUSH,vitalRanks));
        figuresList.add(new Figure(ranks,false,Figure.Category.STRAIGHT,vitalRanks));

        while(ranks.get(0)!= Card.Rank.SIX){
            ranks.remove(0);
            vitalRanks = new Card.Rank[]{ranks.get(0)};
            ranks.add(allRanks.get(ranks.get(3).getPower()-3));
            figuresList.add(new Figure(ranks,true,Figure.Category.STRAIGHT_FLUSH,vitalRanks));
            figuresList.add(new Figure(ranks,false,Figure.Category.STRAIGHT,vitalRanks));
        }
        ranks.remove(0);
        vitalRanks = new Card.Rank[]{ranks.get(0)};
        ranks.add(Card.Rank.ACE);
        figuresList.add(new Figure(ranks,true,Figure.Category.STRAIGHT_FLUSH,vitalRanks));
        figuresList.add(new Figure(ranks,false,Figure.Category.STRAIGHT,vitalRanks));

        //flush
        ranks.clear();
        vitalRanks = new Card.Rank[0];
        figuresList.add(new Figure(ranks,true, Figure.Category.FLUSH,vitalRanks));

        //full house
        vitalRanks = new Card.Rank[2];
        for(Card.Rank r1 : Card.Rank.values()){
            vitalRanks[0] = r1;
            ranks.add(r1); ranks.add(r1); ranks.add(r1);
            for(Card.Rank r2 : Card.Rank.values()){
                vitalRanks[1] = r2;
                if(r1==r2) continue;
                ranks.add(r2); ranks.add(r2);
                figuresList.add(new Figure(ranks,false, Figure.Category.FULL_HOUSE,vitalRanks));
                ranks.remove(4); ranks.remove(3);
            }
            ranks.clear();
        }

        //four, three, two of a kind and high card
        vitalRanks = new Card.Rank[1];
        Figure.Category[] categories = new Figure.Category[]{Figure.Category.HIGH_CARD, Figure.Category.ONE_PAIR,Figure.Category.THREE_OF_A_KIND, Figure.Category.FOUR_OF_A_KIND};
        for(Card.Rank r1 : Card.Rank.values()){
            vitalRanks[0] = r1;
            for(int i=0;i<4;i++){
                ranks.add(r1);
                if(i==0 && r1==Card.Rank.DEUCE) continue;
                figuresList.add(new Figure(ranks,false,categories[i],vitalRanks));
            }
            ranks.clear();
        }

        //two pair

        ranks.clear();
        Collections.sort(figuresList);
        vitalRanks = new Card.Rank[2];
        for(Card.Rank r1 : Card.Rank.values()){
            vitalRanks[0]=r1;
            ranks.add(r1); ranks.add(r1);
            for(Card.Rank r2 : Card.Rank.values()){
                if(r2.getPower()>=r1.getPower()) continue;
                ranks.add(r2); ranks.add(r2);
                vitalRanks[1]=r2;
                figuresList.add(new Figure(ranks,false, Figure.Category.TWO_PAIR,vitalRanks));
                ranks.remove(r2); ranks.remove(r2);
            }
            ranks.remove(r1); ranks.remove(r1);

        }
    }

    public static List<Figure> getUnmodifableFigures(){ return Collections.unmodifiableList(figuresList); }

    public static List<Figure> getModifableFigures(){
        return new ArrayList<>(figuresList);
    }

}

