package com.example.deblefer.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class StatisticsGenerator {

    private int players;
    private Collection<Card> hand;
    private Collection<Card> table;
    private Collection<Card> unused;
    private List<Statistics> statistics = new ArrayList<>();


    private StatisticsGenerator(int players, Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        this.players = players;
        this.hand = new HashSet<>(hand);
        this.table = new HashSet<>(table);
        this.unused = new HashSet<>(unused);
    }

    public static List<Statistics> getStatistics(int players, Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        StatisticsGenerator generator = new StatisticsGenerator(players, hand, table, unused);
        generator.generateStatistics();
        return generator.statistics;
    }
    public static List<Statistics> getStatistics(Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        return getStatistics(2,hand,table,unused);
    }


    private void generateStatistics(){
        switch (table.size()){
            case 0: beforeFlop(); break;
            case 3: afterFlop(); break;
            case 4: afterTurn(); break;
            case 5: afterRiver();
        }
    }

    private void beforeFlop() {
        //previously generated statistics for every Hand
    }
    private void afterFlop() {
        Set<Card> playerCards = new HashSet<>(hand); playerCards.addAll(table);
        Set<Card> unusedCards = new HashSet<>(unused);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        Set<Hand> allHandsForPlayer = new HashSet<>(handGenerator(unusedCards));
        int allGettingCombinations = allHandsForPlayer.size();

        for(int figureIndex = 0 ;figureIndex<sortedFigures.size() && !allHandsForPlayer.isEmpty();figureIndex++){
            Figure playerFigure = sortedFigures.get(figureIndex);

            Set<Hand> neededHandsForPlayer = neededHands(playerCards,playerFigure,allHandsForPlayer);

            int gettingCombinations = neededHandsForPlayer.size();
            int allHandsCombinations = 0;
            int loosingCombinations = 0;
            int drawAndLooseCombinations = 0;

            if(gettingCombinations==0) continue;

            Iterator<Hand> it = neededHandsForPlayer.iterator();
            Set<Card> oneOfNeededHands = new HashSet<>(it.next().getCards());

            for(Hand neededHand : neededHandsForPlayer){

                playerCards.addAll(neededHand.getCards());

                unusedCards.removeAll(neededHand.getCards());
                Set<Hand> allHandsForOpponent = new HashSet<>(handGenerator(unusedCards));
                unusedCards.addAll(neededHand.getCards());
                allHandsCombinations += choose(allHandsForOpponent.size(),players-1);

                //wins
                for(int i = figureIndex-1; i>=0 && !allHandsForOpponent.isEmpty();i--){
                    Figure opponentFigure =sortedFigures.get(i);
                    Set<Card> opponentCards = new HashSet<>(table);
                    opponentCards.addAll(neededHand.getCards());
                    neededHandsRemove(opponentCards,opponentFigure,allHandsForOpponent);
                }

                //draws
                int draws = 0;
                int loosingAfterDraw = 0;
                Set<Card> opponentCards = new HashSet<>(table); opponentCards.addAll(neededHand.getCards());
                Set<Hand> neededHands = neededHands(opponentCards,playerFigure,allHandsForOpponent);
                if(playerFigure.getRanks().size()<5){

                    List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
                    for(Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
                    Collections.sort(drawPlayer,Collections.reverseOrder());

                    for(Hand Hand : neededHands){
                        List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(table));
                        drawOpponent.addAll(getRanks(Hand.getCards())); drawOpponent.addAll(getRanks(neededHand.getCards()));
                        for(Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                        Collections.sort(drawOpponent,Collections.reverseOrder());

                        if(playerFigure.getCategory()== Figure.Category.FLUSH){
                            int result = flushResult(playerCards,opponentCards);
                            if(result<0) loosingAfterDraw++;
                            continue;
                        }

                        int cardNumber = 0;
                        for(;cardNumber<5-playerFigure.getRanks().size();cardNumber++){
                            if(!drawPlayer.get(cardNumber).equals(drawOpponent.get(cardNumber))){
                                if(drawPlayer.get(cardNumber).getPower()>drawOpponent.get(cardNumber).getPower()){
                                    loosingAfterDraw++;
                                }
                                break;
                            }
                        }
                        if(cardNumber==5-playerFigure.getRanks().size()) draws++;
                    }
                }
                else draws = neededHands.size();
                //counting combinations
                int loosingHands = allHandsForOpponent.size()+loosingAfterDraw;
                int drawingHands = draws;
                loosingCombinations += choose(loosingHands,players-1);
                drawAndLooseCombinations += choose(loosingHands+drawingHands,players-1);
                playerCards.removeAll(neededHand.getCards());
            }
            //results
            double chanceGetting = ((double) gettingCombinations)/allGettingCombinations;
            double chanceOfWinning = ((double)loosingCombinations)/allHandsCombinations;
            double chanceOfDraw = ((double) drawAndLooseCombinations - loosingCombinations)/allHandsCombinations;
            Collection<Card> usedCards = usedCards(playerFigure,oneOfNeededHands,table,hand);
            statistics.add(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards));
        }
    }


    private void afterTurn() {

        Set<Card> playerCards = new HashSet<>(hand); playerCards.addAll(table);
        Set<Card> unusedCards = new HashSet<>(unused);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        Set<Card> allCardsForPlayer = new HashSet<>(unusedCards);
        int allGettingCombinations = allCardsForPlayer.size();

        for(int figureIndex=0;figureIndex<sortedFigures.size() && !allCardsForPlayer.isEmpty();figureIndex++){

            Figure playerFigure = sortedFigures.get(figureIndex);
            Set<Card> neededCardsForPlayer = neededCards(playerFigure,playerCards,allCardsForPlayer);

            int gettingCombinations = neededCardsForPlayer.size();
            int allHandsCombinations = 0;
            int loosingCombinations = 0;
            int drawAndLooseCombinations = 0;
            if(gettingCombinations==0) continue;

            Iterator<Card> it = neededCardsForPlayer.iterator();
            Set<Card> oneOfNeededCards = new HashSet<>();
            oneOfNeededCards.add(it.next());

            for(Card neededCard : neededCardsForPlayer){


                playerCards.add(neededCard);
                unusedCards.remove(neededCard);
                Set<Hand> allHandsForOpponent = handGenerator(unusedCards);
                unusedCards.add(neededCard);
                allHandsCombinations += choose(allHandsForOpponent.size(),players-1);

                //wins
                for(int i = 0; i<figureIndex && !allHandsForOpponent.isEmpty();i++){
                    Figure opponentFigure =sortedFigures.get(i);
                    Set<Card> opponentCards = new HashSet<>(table);
                    opponentCards.add(neededCard);
                    neededHandsRemove(opponentCards,opponentFigure,allHandsForOpponent);
                }

                //draws
                Set<Card> opponentCards = new HashSet<>(table); opponentCards.add(neededCard);
                Set<Hand> neededHands = neededHands(opponentCards,playerFigure,allHandsForOpponent);
                int draws = 0;
                int loosingAfterDraw = 0;

                if(sortedFigures.get(figureIndex).getRanks().size()<5){

                    List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
                    for(Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
                    Collections.sort(drawPlayer,Collections.reverseOrder());

                    for(Hand Hand : neededHands){
                        List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(table));
                        drawOpponent.add(Hand.getCard1().getRank()); drawOpponent.add(Hand.getCard2().getRank());
                        drawOpponent.add(neededCard.getRank());
                        if(playerFigure.getCategory()== Figure.Category.FLUSH){
                            int result = flushResult(playerCards,opponentCards);
                            if(result<0) loosingAfterDraw++;
                            continue;
                        }
                        for(Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                        Collections.sort(drawOpponent,Collections.reverseOrder());
                        int cardNumber = 0;
                        for(;cardNumber<5-playerFigure.getRanks().size();cardNumber++){
                            if(!drawPlayer.get(cardNumber).equals(drawOpponent.get(cardNumber))){
                                if(drawPlayer.get(cardNumber).getPower()>drawOpponent.get(cardNumber).getPower()){
                                    loosingAfterDraw++;
                                }
                                break;
                            }
                        }
                        if(cardNumber==5-playerFigure.getRanks().size()) draws++;
                    }
                }
                else draws = neededHands.size();
                //counting combinations
                int loosingHands = allHandsForOpponent.size() + loosingAfterDraw;
                int drawingHands = draws;
                loosingCombinations += choose(loosingHands,players-1);
                drawAndLooseCombinations += choose(loosingHands+drawingHands,players-1);
                playerCards.remove(neededCard);
            }
            //results
            double chanceGetting = ((double) gettingCombinations)/allGettingCombinations;
            double chanceOfWinning = ((double)loosingCombinations)/allHandsCombinations;
            double chanceOfDraw = ((double) drawAndLooseCombinations - loosingCombinations)/allHandsCombinations;
            Collection<Card> usedCards = usedCards(playerFigure,oneOfNeededCards,table,hand);
            statistics.add(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards));
        }
    }
    private void afterRiver() {


        Set<Card> playerCards = new HashSet<>(hand); playerCards.addAll(table);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        //determine figure
        int figureIndex = 0;
        for(; figureIndex < sortedFigures.size();figureIndex++){
            if(isFigure(sortedFigures.get(figureIndex),playerCards)) break;
        }
        if(figureIndex==sortedFigures.size()) throw new RuntimeException();
        Figure playerFigure = sortedFigures.get(figureIndex);

        Set<Hand> allHandsForOpponent = handGenerator(unused);

        int allHandsCombinations = choose(allHandsForOpponent.size(),players-1);

        //wins
        for(int i = 0; i<figureIndex && !allHandsForOpponent.isEmpty();i++){
            Figure opponentFigure = sortedFigures.get(i);
            Set<Card> opponentCards = new HashSet<>(table);
            neededHandsRemove(opponentCards,opponentFigure,allHandsForOpponent);
        }

        //draws
        Set<Card> opponentCards = new HashSet<>(table);
        Set<Hand> neededHands = neededHands(opponentCards,playerFigure,allHandsForOpponent);
        int draws = 0;
        int loosingAfterDraw = 0;


        if(sortedFigures.get(figureIndex).getRanks().size()<5){
            List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
            for(Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
            Collections.sort(drawPlayer,Collections.reverseOrder());
            for(Hand drawHand : neededHands){
                List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(table));
                drawOpponent.addAll(getRanks(drawHand.getCards()));
                if(playerFigure.getCategory()== Figure.Category.FLUSH){
                    int result = flushResult(playerCards,opponentCards);
                    if(result<0) loosingAfterDraw++;
                    continue;
                }
                for(Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                Collections.sort(drawOpponent,Collections.reverseOrder());
                int cardNumber = 0;
                for(;cardNumber<5-playerFigure.getRanks().size();cardNumber++){
                    if(!drawPlayer.get(cardNumber).equals(drawOpponent.get(cardNumber))){
                        if(drawPlayer.get(cardNumber).getPower()>drawOpponent.get(cardNumber).getPower()){
                            loosingAfterDraw++;
                        }
                        break;
                    }
                }
                if(cardNumber==5-playerFigure.getRanks().size())  draws++;
            }
        }
        else draws = neededHands.size();

        //results
        int loosingHands = allHandsForOpponent.size()+loosingAfterDraw;
        int drawingHands  = draws;
        int loosingCombinations = choose(loosingHands,players-1);
        double chanceOfWinning = ((double)loosingCombinations)/allHandsCombinations;
        int drawAndLooseCombinations = choose(loosingHands+drawingHands,players-1);
        double chanceOfDraw = (((double) drawAndLooseCombinations) -loosingCombinations)/allHandsCombinations;
        Collection<Card> usedCards = usedCards(playerFigure,new HashSet<>(),table,hand);
        statistics.add(new Statistics(playerFigure,1.0,chanceOfWinning,chanceOfDraw,usedCards));

    }


    //Removes card from allCards when card+cards form figure. Returns removed cards
    private Set<Card> neededCards(Figure figure, Set<Card> cards, Set<Card> allCards) {

        Set<Card> neededCards = new HashSet<>();

        if(figure.isSuitSignificant()) {
            List<Set<Card>> suitedCards = new ArrayList<>(4);
            List<List<Card.Rank>> suitedRanks = new ArrayList<>(4);
            Iterator<Card> iterator = allCards.iterator();
            for (int i = 0; i < 4; i++) {
                suitedCards.add(new HashSet<>());
                suitedRanks.add(new ArrayList<>());
            }

            for (Card card : cards) {
                suitedCards.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card);
                suitedRanks.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card.getRank());
            }

            for(int i=0; i<4; i++){
                if(suitedCards.get(i).size()<4) continue;
                Card.Suit suit = Card.Suit.values()[i];
                if(figure.getCategory()== Figure.Category.FLUSH){
                    switch(suitedCards.get(i).size()){
                        case 4:
                            while(iterator.hasNext()){
                                Card card = iterator.next();
                                if(card.getSuit()==suit){
                                    neededCards.add(card);
                                    iterator.remove();
                                }
                            }
                            break;
                        default:
                            neededCards.addAll(allCards);
                            allCards.clear();
                            return neededCards;
                    }
                }
                else{
                    List<Card.Rank> suitedFigureRanks = new ArrayList<>(figure.getRanks());
                    List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards.get(i)));
                    for(Card.Rank rank : suitedCardsRanks) suitedFigureRanks.remove(rank);
                    if(suitedFigureRanks.size()>1) continue;
                    switch (suitedFigureRanks.size()){
                        case 1:
                            Card card = new Card(suitedFigureRanks.get(0),suit);
                            if(!allCards.contains(card)) break;
                            neededCards.add(card);
                            allCards.remove(card);
                            break;
                        case 0:
                            neededCards.addAll(allCards);
                            allCards.clear();
                    }
                }
                return neededCards;
            }
            return neededCards;
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            Iterator<Card> iterator = allCards.iterator();
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            if(figureRanks.size()>1) return neededCards;
            switch (figureRanks.size()){
                case 0:
                    neededCards.addAll(allCards);
                    allCards.clear();
                    break;
                default:
                    while(iterator.hasNext()){
                        Card card = iterator.next();
                        if(card.getRank()==figureRanks.get(0)){
                            neededCards.add(card);
                            iterator.remove();
                        }
                    }
            }
            return neededCards;
        }
    }

    //Removes hand from allHands when hand+cards form figure. Returns removed hands
    private Set<Hand> neededHands(Set<Card> cards, Figure figure, Set<Hand> allHands) {

        Set<Hand> neededHands = new HashSet<>();
        Iterator<Hand> iterator = allHands.iterator();

        if(figure.isSuitSignificant()){

            ArrayList<Set<Card>> suitedCards = new ArrayList<>(4);
            ArrayList<List<Card.Rank>> suitedRanks = new ArrayList<>(4);

            for (int i = 0; i < 4; i++) {
                suitedCards.add(new HashSet<>());
                suitedRanks.add(new ArrayList<>());
            }

            for (Card card : cards) {
                suitedCards.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card);
                suitedRanks.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card.getRank());
            }

            for(int i=0;i<4;i++){
                if(suitedCards.get(i).size()<3) continue;
                Card.Suit suit = Card.Suit.values()[i];
                if(figure.getCategory()== Figure.Category.FLUSH){
                    switch (suitedCards.get(i).size()){

                        case 3:
                            while(iterator.hasNext()) {
                                Hand Hand = iterator.next();
                                if (Hand.getCard1().getSuit() == suit && Hand.getCard2().getSuit() == suit) {
                                    neededHands.add(Hand);
                                    iterator.remove();
                                }
                            }
                            break;
                        case 4:
                            while(iterator.hasNext()) {
                                Hand Hand = iterator.next();
                                if (Hand.getCard1().getSuit() == suit || Hand.getCard2().getSuit() == suit) {
                                    neededHands.add(Hand);
                                    iterator.remove();
                                }
                            }
                            break;
                        default:
                            neededHands.addAll(allHands);
                            allHands.clear();
                    }
                }
                else{
                    List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
                    List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards.get(i)));
                    for(Card.Rank rank : suitedCardsRanks) figureRanks.remove(rank);
                    if(suitedCardsRanks.size()>2) continue;

                    switch (suitedCardsRanks.size()){
                        case 0:
                            neededHands.addAll(allHands);
                            allHands.clear();
                            break;
                        case 1:
                            Card card = new Card(suitedCardsRanks.get(0),suit);
                            while(iterator.hasNext()){
                                Hand Hand = iterator.next();
                                if(Hand.getCard1().equals(card) || Hand.getCard2().equals(card)){
                                    neededHands.add(Hand);
                                    iterator.remove();
                                }
                            }
                            break;
                        default:
                            Card card1 = new Card(suitedCardsRanks.get(0),suit);
                            Card card2 = new Card(suitedCardsRanks.get(1),suit);
                            Hand newHand = new Hand(card1,card2);
                            if(!allHands.contains(newHand)) break;
                            neededHands.add(newHand);
                            allHands.remove(newHand);
                    }
                }
                return neededHands;
            }
            return neededHands;
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            switch (figureRanks.size()){
                case 0:
                    neededHands.addAll(allHands);
                    allHands.clear();
                    break;
                case 1:
                    while(iterator.hasNext()) {
                        Hand Hand = iterator.next();
                        if (Hand.getCard1().getRank()==figureRanks.get(0) || Hand.getCard2().getRank()==figureRanks.get(0)){
                            neededHands.add(Hand);
                            iterator.remove();
                        }
                    }
                    break;

                case 2:
                    Collections.sort(figureRanks,Collections.reverseOrder());
                    Card.Rank rank1 = figureRanks.get(0);
                    Card.Rank rank2 = figureRanks.get(1);
                    while(iterator.hasNext()) {
                        Hand Hand = iterator.next();
                        if (Hand.getCard1().getRank()==rank1 && Hand.getCard2().getRank()==rank2){
                            neededHands.add(Hand);
                            iterator.remove();
                        }
                    }
            }
            return neededHands;
        }
    }

    //Removes hand from allHands when hand+cards form figure
    private void neededHandsRemove(Set<Card> cards, Figure figure, Set<Hand> allHands) {
        if(figure.isSuitSignificant()){

            ArrayList<Set<Card>> suitedCards = new ArrayList<>(4);
            ArrayList<List<Card.Rank>> suitedRanks = new ArrayList<>(4);

            for (int i = 0; i < 4; i++) {
                suitedCards.add(new HashSet<>());
                suitedRanks.add(new ArrayList<>());
            }

            //sorting by suit
            for (Card card : cards) {
                suitedCards.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card);
                suitedRanks.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card.getRank());
            }

            for(int i=0;i<4;i++){
                if(suitedCards.get(i).size()<3) continue;
                Iterator<Hand> iterator = allHands.iterator();
                Card.Suit suit = Card.Suit.values()[i];
                if(figure.getCategory()== Figure.Category.FLUSH){
                    switch (suitedCards.get(i).size()){
                        case 3:
                            while(iterator.hasNext()) {
                                Hand Hand = iterator.next();
                                if (Hand.getCard1().getSuit() == suit && Hand.getCard2().getSuit() == suit)
                                    iterator.remove();
                            }
                            break;
                        case 4:
                            while(iterator.hasNext()) {
                                Hand Hand = iterator.next();
                                if (Hand.getCard1().getSuit() == suit || Hand.getCard2().getSuit() == suit)
                                    iterator.remove();
                            }
                            break;
                        default:
                            allHands.clear();
                    }
                }
                else{
                    List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
                    List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards.get(i)));
                    for(Card.Rank rank : suitedCardsRanks) figureRanks.remove(rank);

                    if(suitedCardsRanks.size()>2) continue;

                    switch (suitedCardsRanks.size()){
                        case 0:
                            allHands.clear();
                            break;
                        case 1:
                            Card card = new Card(suitedCardsRanks.get(0),suit);
                            while(iterator.hasNext()) {
                                Hand Hand = iterator.next();
                                if (Hand.getCard1().equals(card) || Hand.getCard2().equals(card))
                                    iterator.remove();
                            }
                            break;
                        default:
                            Card card1 = new Card(suitedCardsRanks.get(0),suit);
                            Card card2 = new Card(suitedCardsRanks.get(1),suit);
                            Hand HandToDelete = new Hand(card1,card2);
                            if(!allHands.contains(HandToDelete)) break;
                            allHands.remove(HandToDelete);
                    }
                }
                return;
            }
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            Iterator<Hand> iterator = allHands.iterator();
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            switch (figureRanks.size()){
                case 0:
                    allHands.clear();
                    break;
                case 1:
                    while(iterator.hasNext()) {
                        Hand Hand = iterator.next();
                        if (Hand.getCard1().getRank()==figureRanks.get(0) && Hand.getCard2().getRank()==figureRanks.get(0))
                            iterator.remove();
                    }
                    break;

                case 2:
                    Collections.sort(figureRanks,Collections.reverseOrder());
                    Card.Rank rank1 = figureRanks.get(0);
                    Card.Rank rank2 = figureRanks.get(1);
                    while(iterator.hasNext()) {
                        Hand Hand = iterator.next();
                        if (Hand.getCard1().getRank()==rank1 && Hand.getCard2().getRank()==rank2)
                            iterator.remove();
                    }
            }
        }
    }

    //Returns true if cards form figure
    private boolean isFigure(Figure figure, Collection<Card> cards) {

        if(figure.isSuitSignificant()) {

            ArrayList<List<Card>> suitedCards = new ArrayList<>(4);
            ArrayList<List<Card.Rank>> suitedRanks = new ArrayList<>(4);

            for (int i = 0; i < 4; i++) {
                suitedCards.add(new ArrayList<>());
                suitedRanks.add(new ArrayList<>());
            }

            //sorting by suit
            for (Card card : cards) {
                suitedCards.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card);
                suitedRanks.get(card.getSuit().getPower() - Card.Suit.values()[0].getPower()).add(card.getRank());
            }

            for(int i=0; i<4; i++){
                if(suitedCards.get(i).size()<3) continue;
                if(figure.getCategory()== Figure.Category.FLUSH){
                    return suitedCards.get(i).size()>=5;
                }
                else{
                    List<Card.Rank> suitedFigureRanks = new ArrayList<>(figure.getRanks());
                    List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards.get(i)));
                    for(Card.Rank rank : suitedCardsRanks) suitedFigureRanks.remove(rank);
                    if(suitedFigureRanks.isEmpty()) return true;
                }
            }
            return false;
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            return figureRanks.isEmpty();
        }
    }

    //Returns ranks from collection of cards
    static private Collection<Card.Rank> getRanks(Collection<Card> cards) {
        List<Card.Rank> cardsRanks = new ArrayList<>();
        for(Card card : cards) cardsRanks.add(card.getRank());
        return cardsRanks;
    }

    //Returns 1 if player wins, returns -1 if opponent wins (both have FLUSH)
    private int flushResult(Set<Card> player, Set<Card> opponent) {
        List<List<Card.Rank>> playerSorted = new ArrayList<>();
        for(int i = 0;i<4;i++){
            playerSorted.add(new ArrayList<>());
        }
        for(Card card : player){
            playerSorted.get(card.getSuit().getPower()-1).add(card.getRank());
        }
        int i;
        for(i=0;i<4;i++){
            if(playerSorted.get(i).size()>4){
                Card.Suit suit = Card.Suit.values()[i];
                Collections.sort(playerSorted.get(i));
                Iterator<Card.Rank> playerIterator = playerSorted.get(i).iterator();
                Iterator<Card> opponentIterator = opponent.iterator();
                while(true){
                    Card card = opponentIterator.next();
                    if(card.getSuit()==suit){
                        if(card.getRank().getPower()<playerIterator.next().getPower()) return 1;
                        else return -1;
                    }
                }
            }
        }
        
        return -1;

    }

    static private Collection<Card> usedCards(Figure figure,Collection<Card> neededCards,Collection<Card> hand, Collection<Card> table){
        Set<Card> cardSet = new HashSet<>();
        List<Card.Rank> ranks =  new ArrayList<>(figure.getRanks());
        @SuppressWarnings("unchecked")
        Iterator<Card>[] iterators = new Iterator[]{hand.iterator(),table.iterator(),neededCards.iterator()};
        int size = ranks.size();
        if(!figure.isSuitSignificant()){
            for(Iterator<Card> iterator : iterators) {
                while (iterator.hasNext()) {
                    Card card = iterator.next();
                    if (ranks.contains(card.getRank())) {
                        cardSet.add(card);
                        ranks.remove(card.getRank());
                        if(ranks.size()==size) return cardSet;
                    }
                }
            }
        }
        else{
            int[] countSuit = new int[4];
            for(Iterator<Card> iterator : iterators) {
                while (iterator.hasNext()) {
                    Card card = iterator.next();
                    countSuit[card.getSuit().getPower()-Card.Suit.values()[0].getPower()]++;
                }
            }
            Card.Suit suit = Card.Suit.CLUBS;
            for(int i= 0;i<4;i++){
                if(countSuit[i]>=5){
                    suit = Card.Suit.values()[i];break;
                }
            }
            if(figure.getCategory()== Figure.Category.FLUSH){
                List<Card> cardList = new ArrayList<>();
                for(Iterator<Card> iterator : iterators) {
                    while (iterator.hasNext()) {
                        Card card = iterator.next();
                        if (card.getSuit()==suit) {
                            cardList.add(card);
                        }
                    }
                }
                Collections.sort(cardList,Collections.reverseOrder());
                for(int i=0;i<5;i++) cardSet.add(cardList.get(i));
                return cardSet;
            }
            for(Iterator<Card> iterator : iterators) {
                while (iterator.hasNext()) {
                    Card card = iterator.next();
                    if (card.getSuit()==suit && ranks.contains(card.getRank())) {
                        cardSet.add(card);
                        ranks.remove(card.getRank());
                        if(ranks.size()==size) return cardSet;
                    }
                }
            }
        }
        return cardSet;
    }

    //Binomial coefficient
    static private int choose(int n, int k) {
        int result = 1;
        for(int i = 0; i < k ; i++) result *= (n-i);
        for(int i = 1 ; i<=k ; i++) result/=i;
        return result;
    }

    //generates all hands from collection of cards
    static private Set<Hand> handGenerator(Collection<Card> cards) {
        List<Card> cardsList = new ArrayList<>(cards);
        Collections.sort(cardsList,Collections.reverseOrder());
        Set<Hand> Hands = new HashSet<>();
        for(int i=0;i<cardsList.size();i++){
            Card card1 = cardsList.get(i);
            for(int j=i+1;j<cardsList.size();j++){
                Card card2 = cardsList.get(j);
                boolean suit = card1.getSuit()==cardsList.get(j).getSuit();
                Hands.add(new Hand(card1,card2,suit));
            }
        }
        return Hands;
    }

}
