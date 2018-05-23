package com.example.deblefer.Classes;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HandTest {
    @Test
    public void TesHandTest(){
        List<Hand> list = new ArrayList<>();
        for(Card card1 : Deck.getModifableDeckAsList()){
            for (Card card2 : Deck.getModifableDeckAsList())
                list.add(new Hand(card1, card2));
        }
        System.out.println(list);
    }
}