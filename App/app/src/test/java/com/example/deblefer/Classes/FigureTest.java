package com.example.deblefer.Classes;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Figure;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FigureTest {
    @Test
    public void figureTest() {

        Card.Rank[] r = new Card.Rank[]{Card.Rank.FIVE};
        List<Card.Rank> ranks = new ArrayList<>(Arrays.asList(r));
        Figure f = new Figure(ranks, true, Figure.Category.STRAIGHT_FLUSH, r);
        assertEquals(f.toString(), "straight_flush(five)");

        ranks.clear();
        r = new Card.Rank[]{Card.Rank.KING,Card.Rank.QUEEN};
        ranks.addAll(new ArrayList<>(Arrays.asList(r)));
        f = new Figure(ranks, false, Figure.Category.FULL_HOUSE, r);
        assertEquals(f.toString(), "full_house(king_queen)");

        ranks.clear();
        r = new Card.Rank[]{};
        f = new Figure(ranks, true, Figure.Category.FLUSH, r);
        assertEquals(f.toString(), "flush");
    }
}

