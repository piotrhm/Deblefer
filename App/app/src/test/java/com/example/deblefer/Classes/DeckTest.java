package com.example.deblefer.Classes;

import com.example.deblefer.Cards.Deck;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeckTest {
    @Test
    public void deckTest(){
        assertTrue(Deck.getUnmodifableDeckAsSet().size() == 52);
        assertEquals("[deuce_of_clubs, deuce_of_diamonds, deuce_of_hearts, deuce_of_spades, three_of_clubs, three_of_diamonds, three_of_hearts, three_of_spades, four_of_clubs, four_of_diamonds, four_of_hearts, four_of_spades, five_of_clubs, five_of_diamonds, five_of_hearts, five_of_spades, six_of_clubs, six_of_diamonds, six_of_hearts, six_of_spades, seven_of_clubs, seven_of_diamonds, seven_of_hearts, seven_of_spades, eight_of_clubs, eight_of_diamonds, eight_of_hearts, eight_of_spades, nine_of_clubs, nine_of_diamonds, nine_of_hearts, nine_of_spades, ten_of_clubs, ten_of_diamonds, ten_of_hearts, ten_of_spades, jack_of_clubs, jack_of_diamonds, jack_of_hearts, jack_of_spades, queen_of_clubs, queen_of_diamonds, queen_of_hearts, queen_of_spades, king_of_clubs, king_of_diamonds, king_of_hearts, king_of_spades, ace_of_clubs, ace_of_diamonds, ace_of_hearts, ace_of_spades]",
                Deck.getModifableDeckAsList().toString());
        assertTrue(Deck.getUnmodifableDeckAsList().size() == 52);
        assertTrue(Deck.testHashesCode());
        System.out.println(Deck.getModifableFourCardsList());
        //assertEquals("c", Deck.getUnmodifableSuitAlternateDeck().toString());
    }

}