package com.example.deblefer.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Game {
    /*
    * talia kart
    * */
    private Collection<Card> deck = Deck.getModifableDeck();
    /*
    * to co jest na stole
    * */
    private Collection<Card> table = new HashSet<>();
    /*
    * to co jest w ręku
    * */
    private Collection<Card> hand = new ArrayList<>();

    private int playersNumber = 2;

    public Game(int playerNumber){
        this.playersNumber = playerNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public Collection<Card> getGameDeck(){
        return Collections.unmodifiableCollection(deck);
    }

    public Collection<Card> getTable() {
        return Collections.unmodifiableCollection(table);
    }

    public Collection<Card> getHand() {
        return Collections.unmodifiableCollection(hand);
    }

    public void setHand(Card card1, Card card2){
        //TODO niech ktoś rzuci wyjątki jeśli dano złe karty
        deck.remove(card1);
        deck.remove(card2);
        hand.add(card1);
        hand.add(card2);
    }

    public void setFlop(Card card1, Card card2, Card card3){
        deck.remove(card1);
        deck.remove(card2);
        deck.remove(card3);
        table.add(card1);
        table.add(card2);
        table.add(card3);
    }

    public void setTurn(Card card1){
        deck.remove(card1);
        table.add(card1);
    }

    public void setRiver(Card card1){
        deck.remove(card1);
        table.add(card1);
    }

    public float getResult(){
        return 0;
    }

    @Override
    public String toString() {
        return "HAND" + hand.toString() + "\n" +
                "TABLE" + table.toString() + "\n" +
                "DECK" + deck.toString() + "\n";
    }


}
