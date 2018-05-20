package com.example.deblefer.Classes;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Figure implements Comparable<Figure>{

    public enum Category {
        HIGH_CARD(0), ONE_PAIR(1), TWO_PAIR(2), THREE_OF_A_KIND(3), STRAIGHT(4), FLUSH(5), FULL_HOUSE(6), FOUR_OF_A_KIND(7), STRAIGHT_FLUSH(8);

        private int power;
        Category(int power){
            this.power = power;
        }

        public Integer getPower() {
            return power;
        }


        @Override
        public String toString() {
            switch (this){
                case HIGH_CARD: return "HIGH CARD";
                case ONE_PAIR: return "ONE PAIR";
                case TWO_PAIR: return "TWO PAIR";
                case THREE_OF_A_KIND: return "THREE OF A KIND";
                case STRAIGHT: return "STRAIGHT";
                case FLUSH: return "FLUSH";
                case FULL_HOUSE: return "FULL HOUSE";
                case FOUR_OF_A_KIND: return "FOUR OF A KIND";
                case STRAIGHT_FLUSH: return "STRAIGHT FLUSH";
            }
            return null;
        }
    }

    private final Collection<Card.Rank> ranks;
    private final boolean suitSignificant;
    private final Category category;
    private final Card.Rank[] vitalRanks;

    Figure(Collection<Card.Rank> ranks, boolean suitSignificant, Category category,Card.Rank[] vitalRanks){
        this.ranks = new ArrayList<>(ranks);
        this.suitSignificant = suitSignificant;
        this.category = category;
        this.vitalRanks = Arrays.copyOfRange(vitalRanks,0,vitalRanks.length);
    }


    public Collection<Card.Rank> getRanks() { return Collections.unmodifiableCollection(ranks); }

    public boolean isSuitSignificant() { return suitSignificant; }

    public Category getCategory(){return category;}

    public Card.Rank[] getVitalRanks(){ return vitalRanks;}

    @Override
    public String toString() {
        switch(vitalRanks.length){
            case 0: return this.getCategory().name().toLowerCase();
            case 1: return this.getCategory().name().toLowerCase()+"("+this.getVitalRanks()[0].name().toLowerCase()+")";
            default: return this.getCategory().name().toLowerCase()+"("+this.getVitalRanks()[0].name().toLowerCase()+"_"+this.getVitalRanks()[1].name().toLowerCase()+")";
        }
    }

    @Override
    public int compareTo(@NonNull Figure o) {
        int compare = this.getCategory().getPower().compareTo(o.category.getPower());
        if(compare!=0) return compare;
        for(int i=0;i<this.getVitalRanks().length;i++){
            compare = this.getVitalRanks()[i].getPower().compareTo(o.getVitalRanks()[i].getPower());
            if(compare!=0) return compare;
        }
        return 0;

    }

}
