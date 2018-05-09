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
        assertTrue(allFiguresList.size()==228);
        Set<Figure> allFiguresSet = new HashSet<>(allFiguresList);
        Set<String> allNamesSet = new HashSet<>();
        for(Figure f : allFiguresList){
            allNamesSet.add(f.toString());
        }
        assertTrue(allFiguresSet.size()==228);
        assertTrue(allNamesSet.size()==228);
        assertEquals(allFiguresList.get(0).toString(),"high_card(three)");
        assertEquals(allFiguresList.get(227).toString(),"straight_flush(ace)");
    }

}