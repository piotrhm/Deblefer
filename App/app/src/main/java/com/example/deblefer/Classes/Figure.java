package com.example.deblefer.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Figure implements Comparable<Figure>{

    public enum Category { HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH }

    private final Collection<Card.Rank> ranks;
    private final boolean suitSignificant;
    private final String name;

    Figure(Collection<Card.Rank> ranks, boolean suitSignificant){
        this.ranks = new ArrayList<>(ranks);
        this.suitSignificant = suitSignificant;
        this.name = createName(ranks,suitSignificant);
    }


    public Collection<Card.Rank> getRanks() { return Collections.unmodifiableCollection(ranks); }

    public boolean isSuitSignificant() { return suitSignificant; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Figure o) {

        Category category1,category2;
        if(!this.name.equals("flush")) category1 = Category.valueOf(this.name.substring(0,this.name.lastIndexOf("(")).toUpperCase());
        else category1 = Category.FLUSH;
        if(!o.name.equals("flush")) category2 = Category.valueOf(o.name.substring(0,o.name.lastIndexOf("(")).toUpperCase());
        else category2 = Category.FLUSH;
        if(category1!=category2) return category1.compareTo(category2);

        String ranksList1 = this.name.substring(this.name.indexOf("(")+1,this.name.indexOf(")")).toUpperCase();
        String ranksList2 = (o).name.substring(o.name.indexOf("(")+1,o.name.indexOf(")")).toUpperCase();

        String[] ranks1 = ranksList1.split("_");
        String[] ranks2 = ranksList2.split("_");

        Card.Rank r1 = Card.Rank.valueOf(ranks1[0]);
        Card.Rank r2 = Card.Rank.valueOf(ranks2[0]);
        if(r1!=r2 || ranks1.length<2) return r1.compareTo(r2);
        r1 = Card.Rank.valueOf(ranks1[1]);
        r2 = Card.Rank.valueOf(ranks2[1]);
        return r1.compareTo(r2);

    }

    static private String createName(Collection<Card.Rank> ranks, boolean suitSignificant) {

        List<Card.Rank> ranksList = new ArrayList<>(ranks); Collections.sort(ranksList);
        Set<Card.Rank> ranksSet = new HashSet<>(ranks);

        if(ranksSet.size()==1){
            switch(ranksList.size()){
                case 1: return "high_card("+ranksList.get(0).toString().toLowerCase()+")";
                case 2: return "one_pair("+ranksList.get(0).toString().toLowerCase()+")";
                case 3: return "three_of_a_kind("+ranksList.get(0).toString().toLowerCase()+")";
                case 4: return "four_of_a_kind("+ranksList.get(0).toString().toLowerCase()+")";
            }
        }

        if(ranksSet.size()==5 && ranksList.get(0).ordinal()+4==ranksList.get(4).ordinal()){
            if(suitSignificant) return "straight_flush("+ ranksList.get(4).toString().toLowerCase()+")";
            else return "straight("+ ranksList.get(4).toString().toLowerCase()+")";
        }

        //Exception: 5 4 3 2 A
        if(ranksSet.size()==5 && ranksList.get(4)== Card.Rank.ACE && ranksList.get(3)==Card.Rank.FIVE){
            return "straight_flush(five)";
        }
        if(ranksSet.isEmpty() && suitSignificant){ return "flush"; }

        if(ranksList.size()==4) return "two_pair("+ranksList.get(2).toString().toLowerCase()+"_" +ranksList.get(0).toString().toLowerCase()+")";
        else{
            if(ranksList.get(2)==ranksList.get(1))
                return "full_house("+ranksList.get(2).toString().toLowerCase()+"_"+ranksList.get(3).toString().toLowerCase()+")";
            else return "full_house("+ranksList.get(2).toString().toLowerCase()+"_"+ranksList.get(1).toString().toLowerCase()+")";
        }
    }
}
