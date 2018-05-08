package com.example.deblefer.Classes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FigureTest {
    @Test
    public void figureTest(){

        List<Card.Rank> ranks = new ArrayList<>(Arrays.asList(Card.Rank.DEUCE,Card.Rank.ACE,Card.Rank.THREE,Card.Rank.FOUR,Card.Rank.FIVE));
        Figure f = new Figure(ranks,true);
        assertEquals(f.toString(),"straight_flush(five)");

        ranks.clear();
        f = new Figure(ranks,true);
        assertEquals(f.toString(),"flush");

        ranks.addAll(new ArrayList<>(Arrays.asList(Card.Rank.QUEEN,Card.Rank.KING,Card.Rank.QUEEN,Card.Rank.KING,Card.Rank.KING)));
        f = new Figure(ranks,false);
        assertEquals(f.toString(),"full_house(king_queen)");
    }

}