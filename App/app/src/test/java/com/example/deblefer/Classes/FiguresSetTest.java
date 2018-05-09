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
        for(Figure f : allFiguresList) System.out.println(f);
        assertTrue(allFiguresList.size()==306);
        Set<Figure> allFiguresSet = new HashSet<>(allFiguresList);
        Set<String> allNamesSet = new HashSet<>();
        for(Figure f : allFiguresList){
            allNamesSet.add(f.toString());
        }
        assertTrue(allFiguresSet.size()==306);
        assertTrue(allNamesSet.size()==306);
        assertEquals(allFiguresList.get(0).toString(),"high_card(three)");
        assertEquals(allFiguresList.get(305).toString(),"straight_flush(ace)");
    }

}