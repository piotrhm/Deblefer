package com.example.deblefer.Classes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class FiguresSetTest {
    @Test
    public void allFiguresTest(){
        List<Figure> allFiguresList = FiguresSet.getModifableFigures();
        Collections.sort(allFiguresList);
        assertTrue(allFiguresList.size()==229);
        Set<Figure> allFiguresSet = new HashSet<>(allFiguresList);
        assertTrue(allFiguresSet.size()==229);
        assertEquals(allFiguresList.get(0).toString(),"high_card(deuce)");
        assertEquals(allFiguresList.get(228).toString(),"straight_flush(ace)");
    }

}