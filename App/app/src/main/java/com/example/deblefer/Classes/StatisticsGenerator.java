package com.example.deblefer.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticsGenerator {

    private int players;
    private Collection<Card> hand;
    private Collection<Card> table;
    private Collection<Card> unused;
    private Collection<Statistics> statistics = new ArrayList<>();


    private StatisticsGenerator(int players, Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        this.players = players;
        this.hand = new ArrayList<>(hand);
        this.table = new ArrayList<>(table);
        this.unused = new HashSet<>(unused);
    }

    public static Collection<Statistics> getStatistics(int players, Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        StatisticsGenerator generator = new StatisticsGenerator(players, hand, table, unused);
        generator.generateStatistics();
        return generator.statistics;
    }
    public static Collection<Statistics> getStatistics(Collection<Card> hand, Collection<Card> table, Collection<Card> unused){
        return getStatistics(2,hand,table,unused);
    }


    private void generateStatistics(){

        Set<Set<Card>> uncheckedCardsCombinations = getAllCombinations(5-table.size(),unused);
        int possibleCombinationsPlayer = uncheckedCardsCombinations.size();

        Set<Card> cardsPlayer = new HashSet<>(hand); cardsPlayer.addAll(table);

        Set<Card> unusedCards = new HashSet<>(unused);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        int allFigures = 0;

        for(int i=0;i<sortedFigures.size();i++){
            if(uncheckedCardsCombinations.size()==0 && cardsPlayer.size()<7) break;
            Figure figure = sortedFigures.get(i);

            int possibleCombinationsFigure = 0;
            int counterLoosing = 0;
            Set<Set<Card>> neededCombinations = neededCombinations(cardsPlayer,figure,unusedCards);

            if(neededCombinations.isEmpty()) continue;

            for(Set<Card> nC : neededCombinations){

                if(!uncheckedCardsCombinations.remove(nC)){ continue; }
                allFigures++;
                possibleCombinationsFigure++;
                unusedCards.removeAll(nC);

                Set<Set<Card>> checkedCombinationsOpponent = new HashSet<>();
                for(int j=0;j<=i;j++){
                    if(checkedCombinationsOpponent.size()==choose(2,unusedCards.size())) break;
                    Figure opponentFigure = sortedFigures.get(j);
                    Set<Card> opponentCards = new HashSet<>(table);  opponentCards.addAll(nC);
                    Set<Set<Card>> opponentNeeded = neededCombinations(opponentCards,opponentFigure,unusedCards);
                    checkedCombinationsOpponent.addAll(opponentNeeded);

                }
                int lost = 45*22-checkedCombinationsOpponent.size();
                counterLoosing+= choose(players-1,lost);
                unusedCards.addAll(nC);
            }

            if(possibleCombinationsFigure==0) continue;
            int allPossibilities = choose(players-1,45*22)*possibleCombinationsFigure;

            double chanceWinning = ((double)counterLoosing)/allPossibilities;

            double chanceGetting = ((double)possibleCombinationsFigure)/(possibleCombinationsPlayer);
            if(chanceGetting==0) continue;
            statistics.add(new Statistics(figure,chanceGetting,chanceWinning));
        }
    }


    //too slow, too long
    private Set<Set<Card>> neededCombinations(Set<Card> cards, Figure figure, Set<Card> unusedCards) {

        if(cards.size()<5 || cards.size()>7 ) throw new IllegalArgumentException(); //cards.size \in {2,5,6,7} but for 2 other method or DB

        Set<Set<Card>> neededCombinations = new HashSet<>();
        if(cards.size()==7){ return neededCombinations; }

        if(figure.isSuitSignificant()) {
            ArrayList<ArrayList<Card>> suitedCards = new ArrayList<>(4);
            ArrayList<ArrayList<Card.Rank>> suitedRanks = new ArrayList<>(4);
            for (int i = 0; i < 4; i++) {
                suitedCards.add(new ArrayList<>());
                suitedRanks.add(new ArrayList<>());
            }

            //sorting by suit
            for (Card card : cards) {
                suitedCards.get(card.getSuit().getPower() - Card.Suit.CLUBS.getPower()).add(card);
                suitedRanks.get(card.getSuit().getPower() - Card.Suit.CLUBS.getPower()).add(card.getRank());
            }
            for (int i = 0; i < 4; i++) {

                if (suitedRanks.get(i).size() < 3) {
                    continue;
                }
                List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
                for (Card.Rank r : suitedRanks.get(i)) figureRanks.remove(r);

                if (figureRanks.size() == 0) {
                    if (figure.getCategory() != Figure.Category.FLUSH) {
                        neededCombinations = getAllCombinations(7 - cards.size(), unusedCards);
                        return neededCombinations;
                    }
                    //exception FLUSH too long
                    else {
                        if (suitedCards.get(i).size() == 5) {
                            neededCombinations = getAllCombinations(7 - cards.size(), unusedCards);
                            return neededCombinations;
                        }
                        if (suitedCards.get(i).size() == 4 && cards.size() < 7) {
                            if (cards.size() == 5) {
                                Set<Card> missingCard1 = getAllSuited(unusedCards, suitedCards.get(i).get(0).getSuit());
                                Set<Card> missingCard2 = getAll(unusedCards);
                                for (Card card1 : missingCard1) {
                                    for (Card card2 : missingCard2) {
                                        if (card1.equals(card2)) continue;
                                        Set<Card> missingPair = new HashSet<>();
                                        missingPair.add(card1);
                                        missingPair.add(card2);
                                        neededCombinations.add(missingPair);
                                    }
                                }
                            }
                            if (cards.size() == 6) {
                                Set<Card> missingCard1 = getAllSuited(unusedCards, suitedCards.get(i).get(0).getSuit());
                                neededCombinations.addAll(getAllCombinations(1, missingCard1));
                            }
                            continue;
                        }
                        if (suitedCards.get(i).size() == 3 && cards.size() == 5) {
                            Set<Card> missingCard1 = getAllSuited(unusedCards, suitedCards.get(i).get(0).getSuit());
                            neededCombinations.addAll(getAllCombinations(2, missingCard1));
                        }
                        continue;
                    }
                }

                if (figureRanks.size() == 1 && cards.size() < 7) {
                    Set<Card> missingCard = getAllSuitedAndRanked(unusedCards, suitedCards.get(i).get(0).getSuit(), figureRanks.get(0));
                    if (7 - cards.size() - 1 == 0) {
                        neededCombinations.add(missingCard);
                        continue;
                    }
                    Set<Set<Card>> combinationsWithoutMissingCard = getAllCombinations(7 - cards.size() - 1, unusedCards);
                    for (Set<Card> set : combinationsWithoutMissingCard) {
                        for (Card card1 : missingCard) {
                            if (!set.add(card1)) continue;
                            neededCombinations.add(set);
                        }

                    }
                    continue;
                }

                if (figureRanks.size() == 2 && cards.size() < 6) {
                    Set<Card> missingCard1 = getAllSuitedAndRanked(unusedCards, suitedCards.get(i).get(0).getSuit(), figureRanks.get(0));
                    Set<Card> missingCard2 = getAllSuitedAndRanked(unusedCards, suitedCards.get(i).get(0).getSuit(), figureRanks.get(1));

                    for (Card card1 : missingCard1) {
                        for (Card card2 : missingCard2) {
                            if (card1.equals(card2)) continue;
                            Set<Card> missingPair = new HashSet<>();
                            missingPair.add(card1);
                            missingPair.add(card2);
                            neededCombinations.add(missingPair);
                        }
                    }
                }

            }
            return neededCombinations;
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>();
            for(Card card: cards) cardsRanks.add(card.getRank());

            for(Card.Rank r : cardsRanks) figureRanks.remove(r);

            if(figureRanks.size()+cards.size()>7) return neededCombinations;

            if(figureRanks.size()==0){
                neededCombinations = getAllCombinations(7-cards.size(),unusedCards);
                return neededCombinations;
            }

            if(figureRanks.size()==1){
                if(cards.size()==5){
                    Set<Card> missingCard1 = getAll(unusedCards);
                    Set<Card> missingCard2 = getAllRanked(unusedCards,figureRanks.get(0));
                    for(Card card1 : missingCard1){
                        for(Card card2 : missingCard2){
                            if(card1.equals(card2)) continue;
                            Set<Card> missingPair = new HashSet<>();
                            missingPair.add(card1); missingPair.add(card2);
                            neededCombinations.add(missingPair);
                        }
                    }
                    return neededCombinations;
                }
                if(cards.size()==6){
                    Set<Card> missingCard1 = getAllRanked(unusedCards,figureRanks.get(0));
                    for(Card card1 : missingCard1){
                        Set<Card> missing = new HashSet<>();
                        missing.add(card1);
                        neededCombinations.add(missing);
                    }
                    return neededCombinations;
                }
            }

            if(figureRanks.size()==2 && cards.size()==5){
                Set<Card> missingCard1 = getAllRanked(unusedCards,figureRanks.get(0));
                Set<Card> missingCard2 = getAllRanked(unusedCards,figureRanks.get(1));
                for(Card card1 : missingCard1){
                    for(Card card2 : missingCard2){
                        if(card1.equals(card2)) continue;
                        Set<Card> missingPair = new HashSet<>();
                        missingPair.add(card1); missingPair.add(card2);
                        neededCombinations.add(missingPair);
                    }
                }
                return neededCombinations;
            }
        }
        return neededCombinations;
    }

    private Set<Set<Card>> getAllCombinations(int size, Collection<Card> unusedCards) {
        //could be faster
        List<Set<Set<Card>>> sets = new ArrayList<>();
        sets.add(new HashSet<>());
        if(size==0) return sets.get(0);
        for(Card card1 : unusedCards){
            Set<Card> toAdd = new HashSet<>();
            toAdd.add(card1);
            sets.get(0).add(toAdd);
        }
        for(int i=1;i<size;i++){
            sets.add(new HashSet<>());
            for(Set<Card> set : sets.get(i-1)){
                for(Card card : unusedCards){
                    Set<Card> superSet = new HashSet<>(set);
                    if(!superSet.add(card)) continue;
                    sets.get(i).add(superSet);
                }
            }
            sets.get(i-1).clear();
        }
        return sets.get(size-1);
    }

    //getAll, getAllSuited, getAllRanked, getAllSuitedAndRanked could use steam
    private Set<Card> getAll(Collection<Card> unusedCards) {
        return new HashSet<>(unusedCards);
    }

    private Set<Card> getAllSuited(Collection<Card> unusedCards, Card.Suit suit){
        Set<Card> setToReturn = new HashSet<>();
        for(Card card : unusedCards){
            if(card.getSuit()==suit) setToReturn.add(card);
        }
        return setToReturn;
    }

    private Set<Card> getAllRanked(Collection<Card> unusedCards, Card.Rank rank){
        Set<Card> setToReturn = new HashSet<>();
        for(Card card : unusedCards){
            if(card.getRank()==rank) setToReturn.add(card);
        }
        return setToReturn;
    }

    private Set<Card> getAllSuitedAndRanked(Collection<Card> unusedCards, Card.Suit suit, Card.Rank rank){
        Set<Card> setToReturn = new HashSet<>();
        for(Card card : unusedCards){
            if(card.getRank()==rank && card.getSuit()==suit){
                setToReturn.add(card);
            }
        }
        return setToReturn;
    }


    static private int choose(int k,int n){
        int result = 1;
        for(int i=0;i<k;i++){ result=result*n;n--; }
        for(int i=1;i<k+1;i++) result = result/i;
        return result;
    }

}
