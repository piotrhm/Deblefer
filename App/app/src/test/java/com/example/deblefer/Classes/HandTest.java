package com.example.deblefer.Classes;

import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Deck;
import com.example.deblefer.Cards.Hand;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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