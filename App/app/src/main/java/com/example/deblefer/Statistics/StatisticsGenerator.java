package com.example.deblefer.Statistics;

import android.support.v7.widget.RecyclerView;

import com.example.deblefer.Adapters.StatisticsViewAdapter;
import com.example.deblefer.Cards.Card;
import com.example.deblefer.Cards.Figure;
import com.example.deblefer.Cards.FiguresSet;
import com.example.deblefer.Cards.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class StatisticsGenerator {

    private volatile boolean interrupted = false;
    private int players;
    private Collection<Card> hand;
    private Collection<Card> table;
    private Collection<Card> unused;
    private List<Statistics> statistics = new ArrayList<>();
    private StatisticsSettings statisticsSettings;
    private RecyclerView recyclerView;

    public void interrupt(){
        interrupted = true;
    }

    public static class StatisticsGeneratorBox{
        private StatisticsGenerator generator;
        public StatisticsGeneratorBox(Collection<Card> hand, Collection<Card> table, Collection<Card> unused, int players,
                               StatisticsSettings statisticsSettings, RecyclerView statisticsRecyclerView){
            generator = new StatisticsGenerator(players, hand, table, unused, statisticsSettings, statisticsRecyclerView);
        }
        public List<Statistics> getStatistics() throws InterruptedException {
            generator.generateStatistics();
            return generator.statistics;
        }
        public void interrupt(){
            generator.interrupt();
        }
    }

    public static Double getChanceOfWinning(List<Statistics> statisticsList){
        Double result = 0.0;
        for(Statistics stat : statisticsList)
            result += stat.getChanceOfGettingAsHighest()*stat.getChanceOfWinning();
        return result;
    }

    private List<Integer> getCounts(Figure figure, Collection<Card> usedCards){
        List<Integer> counts = new ArrayList<>();
        if(figure.getCategory() == Figure.Category.STRAIGHT_FLUSH){
            Collection<Card> needed = new HashSet<>(usedCards);
            needed.retainAll(unused);
            for(Card ignored : needed)
                counts.add(1);
        }
        else if(figure.getCategory() == Figure.Category.FLUSH){
            Card.Suit suit = usedCards.iterator().next().getSuit();
            int count = 0;
            for (Card card : unused)
                if (card.getSuit().equals(suit))
                    count++;
            for (int i = 0; i < count - 8; i++)
                counts.add(count);
            if(counts.size() >= 2)
                counts.set(0, -counts.get(0));
        }
        else
        {
            /*
             * do streamów potrzebne jest wysokie API ;_;
             * */
            List<Card> playerCards = new ArrayList<>(hand);
            playerCards.addAll(table);
            List<Card.Rank> playersRanks = new ArrayList<>();
            List<Card.Rank> needed = new ArrayList<>();
            for (Card card : playerCards)
                playersRanks.add(card.getRank());
            for (Card.Rank rank : figure.getRanks()){
                if(!playersRanks.remove(rank))
                    needed.add(rank);
            }
            for (Card.Rank rank : needed)
                counts.add(getUnusedRankCount(rank));

            if(needed.size() == 2 && needed.get(0).equals(needed.get(1)))
                counts.set(0, -counts.get(0));
        }
        return counts;
    }

    private Double getChanceOfGettingFlop(Figure figure, Collection<Card> usedCards){
        List<Integer> counts = getCounts(figure, usedCards);

        if(counts.size() > 2)
            return 0.0;
        if(counts.size() == 0)
            return 1.0;

        boolean same = (counts.get(0) < 0);
        int k = counts.get(0);
        int s = unused.size();

        if(same)
            return (k*(k-1))/(double)(s*(s-1));
        if(counts.size() == 1)
            return 2*(k*(s-k))/(double)(s*(s-1)) + (k*(k-1))/(double)(s*(s-1));
        int l = counts.get(1);
        return 2*(k*l)/(double)(s*(s-1));
    }

    private Double getChanceOfGettingTurn(Figure figure, Collection<Card> usedCards){
        List<Integer> counts = getCounts(figure, usedCards);
        if(counts.size() > 1)
            return 0.0;
        if(counts.size() == 0)
            return 1.0;
        return (double)counts.get(0)/unused.size();
    }

    private int getUnusedRankCount(Card.Rank rank){
        int count = 0;
        for (Card card : unused)
            if(card.getRank().equals(rank))
                count++;
        return count;
    }

    private StatisticsGenerator(int players, Collection<Card> hand, Collection<Card> table,
                                Collection<Card> unused, StatisticsSettings statisticsSettings,
                                RecyclerView recyclerView){
        this.players = players;
        this.hand = new HashSet<>(hand);
        this.table = new HashSet<>(table);
        this.unused = new HashSet<>(unused);
        this.statisticsSettings=statisticsSettings;
        this.recyclerView = recyclerView;
    }

   /* public static List<Statistics> getStatistics(Collection<Card> hand, Collection<Card> table, Collection<Card> unused, int players,
                                                 StatisticsSettings statisticsSettings, RecyclerView statisticsRecyclerView) throws InterruptedException {
        StatisticsGenerator generator = new StatisticsGenerator(players, hand, table, unused, statisticsSettings, statisticsRecyclerView);
        generator.generateStatistics();
        return generator.statistics;
    }*/

    private void generateStatistics() throws InterruptedException {
        switch (table.size()){
            case 0: beforeFlop(); break;
            case 3: afterFlop(); break;
            case 4: afterTurn(); break;
            case 5: afterRiver();
        }
    }

    private void beforeFlop() {
        //previously generated statistics for every Pair
    }

    private void afterFlop() throws InterruptedException {
        Set<Card> playerCards = new HashSet<>(hand); playerCards.addAll(table);
        Set<Card> unusedCards = new HashSet<>(unused);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        Set<Pair> allPairsForPlayer = new HashSet<>(PairGenerator(unusedCards));
        long allGettingCombinations = allPairsForPlayer.size();

        for(int figureIndex = 0 ;figureIndex<sortedFigures.size() && !allPairsForPlayer.isEmpty();figureIndex++){
            Figure playerFigure = sortedFigures.get(figureIndex);
            if(interrupted)
                throw new InterruptedException();
            if( statisticsSettings.isDrawWhenSameFigure()){
                List<Card> handList = new ArrayList<>(hand);
                if(playerFigure.getCategory()!= Figure.Category.FLUSH){
                    if( !playerFigure.getRanks().contains(handList.get(0).getRank())
                            && !playerFigure.getRanks().contains(handList.get(1).getRank())){
                        neededPairsRemove(playerCards,playerFigure,allPairsForPlayer);
                        continue;
                    }
                }
                if(playerFigure.isSuitSignificant()) {
                    List<Card> suited = new ArrayList<>(hand); suited.addAll(table);
                    suited = suitedCards(suited);
                    if(suited.size() < 3) continue;
                    Card.Suit suit = suited.get(0).getSuit();
                    if (handList.get(0).getSuit() != suit && handList.get(1).getSuit() != suit ) {
                        neededPairsRemove(playerCards, playerFigure, allPairsForPlayer);
                        continue;
                    }
                }
            }

            Set<Pair> neededPairsForPlayer = neededPairs(playerCards,playerFigure,allPairsForPlayer);

            long gettingCombinations = neededPairsForPlayer.size();
            long allPairsCombinations = 0;
            long loosingCombinations = 0;
            long drawAndLooseCombinations = 0;
            double chanceGetting = ((double) gettingCombinations)/allGettingCombinations;

            if(chanceGetting==0 || chanceGetting<statisticsSettings.getMinChanceOfGetting()) continue;

            //for usedCards
            Iterator<Pair> it = neededPairsForPlayer.iterator();
            Set<Card> oneOfNeededPairs = new HashSet<>(it.next().getCards());

            for(Pair neededPair : neededPairsForPlayer){
                playerCards.addAll(neededPair.getCards());
                unusedCards.removeAll(neededPair.getCards());
                Set<Pair> allPairsForOpponent = new HashSet<>(PairGenerator(unusedCards));
                unusedCards.addAll(neededPair.getCards());
                allPairsCombinations += choose(allPairsForOpponent.size(),players - 1);

                //wins
                for(int i = 0 ; i< figureIndex && !allPairsForOpponent.isEmpty() ; i++){
                    if(interrupted)
                        throw new InterruptedException();
                    Figure opponentFigure =sortedFigures.get(i);
                    Set<Card> opponentCards = new HashSet<>(table);
                    opponentCards.addAll(neededPair.getCards());
                    neededPairsRemove(opponentCards,opponentFigure,allPairsForOpponent);
                }

                //draws
                long loosingAfterDraw = 0;
                long drawingPairs = 0;

                //FLUSH exception
                if(playerFigure.getCategory()== Figure.Category.FLUSH){
                    Set<Card> opponentCards = new HashSet<>(table);
                    opponentCards.addAll(neededPair.getCards());
                    Set<Pair> neededPairs = neededPairs(opponentCards, playerFigure, allPairsForOpponent);
                    for (Pair pair : neededPairs) {
                        if(interrupted)
                            throw new InterruptedException();
                        opponentCards.addAll(pair.getCards());
                        int result = flushResult(playerCards, opponentCards);
                        if (result < 0) loosingAfterDraw++;
                        if(result==0) drawingPairs++;
                        opponentCards.removeAll(pair.getCards());
                    }
                }

                else{
                    if(!statisticsSettings.isDrawWhenSameFigure()){

                        Set<Card> opponentCards = new HashSet<>(table);
                        opponentCards.addAll(neededPair.getCards());
                        Set<Pair> neededPairs = neededPairs(opponentCards, playerFigure, allPairsForOpponent);

                        if (playerFigure.getRanks().size() < 5) {
                            List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
                            for (Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
                            Collections.sort(drawPlayer, Collections.reverseOrder());

                            for (Pair pair : neededPairs) {
                                if(interrupted)
                                    throw new InterruptedException();
                                List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(opponentCards));
                                drawOpponent.addAll(getRanks(pair.getCards()));
                                for (Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                                Collections.sort(drawOpponent, Collections.reverseOrder());

                                int cardNumber = 0;
                                for (; cardNumber < 5 - playerFigure.getRanks().size(); cardNumber++) {
                                    if (!drawPlayer.get(cardNumber).equals(drawOpponent.get(cardNumber))) {
                                        if (drawPlayer.get(cardNumber).getPower() > drawOpponent.get(cardNumber).getPower()) {
                                            loosingAfterDraw++;
                                        }
                                        break;
                                    }
                                }
                                if (cardNumber == 5 - playerFigure.getRanks().size()) drawingPairs++;

                                drawOpponent.removeAll(getRanks(pair.getCards()));

                            }

                        }
                        else drawingPairs = neededPairs.size();
                    }
                }

                //counting combinations
                long loosingPairs = allPairsForOpponent.size()+loosingAfterDraw;
                loosingCombinations += choose(loosingPairs,players - 1);
                drawAndLooseCombinations += choose(loosingPairs+drawingPairs,players - 1);
                playerCards.removeAll(neededPair.getCards());
            }
            //results
            double chanceOfWinning = ((double)loosingCombinations)/allPairsCombinations;
            double chanceOfDraw = ((double) drawAndLooseCombinations - loosingCombinations)/allPairsCombinations;
            Collection<Card> usedCards = usedCards(playerFigure,oneOfNeededPairs,table,hand);
            statistics.add(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards));
            if(recyclerView != null)
                recyclerView.post(() -> {
                    StatisticsViewAdapter adapter = (StatisticsViewAdapter)recyclerView.getAdapter();
                    adapter.addItem(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards).setChanceToGet(getChanceOfGettingFlop(playerFigure, usedCards)));
                });
        }
    }

    private void afterTurn() throws InterruptedException {

        Set<Card> playerCards = new HashSet<>(hand); playerCards.addAll(table);
        Set<Card> unusedCards = new HashSet<>(unused);

        List<Figure> sortedFigures = new ArrayList<>(FiguresSet.getUnmodifableFigures());
        Collections.sort(sortedFigures,Collections.reverseOrder());

        Set<Card> allCardsForPlayer = new HashSet<>(unusedCards);
        long allGettingCombinations = allCardsForPlayer.size();

        for(int figureIndex=0;figureIndex<sortedFigures.size() && !allCardsForPlayer.isEmpty();figureIndex++){
            if(interrupted)
                throw new InterruptedException();
            Figure playerFigure = sortedFigures.get(figureIndex);

            if(statisticsSettings.isDrawWhenSameFigure() ){
                List<Card> handList = new ArrayList<>(hand);
                if(playerFigure.getCategory()!= Figure.Category.FLUSH){
                    if((!playerFigure.getRanks().contains(handList.get(0).getRank())
                            && !playerFigure.getRanks().contains(handList.get(1).getRank()))){
                        neededCards(playerFigure,playerCards,allCardsForPlayer);
                        continue;
                    }}
                if(playerFigure.isSuitSignificant()) {
                    List<Card> suited = new ArrayList<>(hand);
                    suited.addAll(table);
                    suited = suitedCards(suited);
                    if(suited.size() < 4) continue;
                    Card.Suit suit = suited.get(0).getSuit();
                    if (handList.get(0).getSuit() != suit && handList.get(1).getSuit() != suit){
                        neededCards(playerFigure,playerCards,allCardsForPlayer);
                        continue;
                    }
                }
            }

            Set<Card> neededCardsForPlayer = neededCards(playerFigure,playerCards,allCardsForPlayer);

            long gettingCombinations = neededCardsForPlayer.size();
            long allPairsCombinations = 0;
            long loosingCombinations = 0;
            long drawAndLooseCombinations = 0;
            double chanceGetting = ((double) gettingCombinations)/allGettingCombinations;

            if(chanceGetting==0 || chanceGetting<statisticsSettings.getMinChanceOfGetting()) continue;

            Iterator<Card> it = neededCardsForPlayer.iterator();
            Set<Card> oneOfNeededCards = new HashSet<>();
            oneOfNeededCards.add(it.next());

            for(Card neededCard : neededCardsForPlayer){
                playerCards.add(neededCard);
                unusedCards.remove(neededCard);
                Set<Pair> allPairsForOpponent = PairGenerator(unusedCards);
                unusedCards.add(neededCard);
                allPairsCombinations += choose(allPairsForOpponent.size(),players - 1);

                //wins
                for(int i = 0; i<figureIndex && !allPairsForOpponent.isEmpty();i++){
                    if(interrupted)
                        throw new InterruptedException();
                    Figure opponentFigure =sortedFigures.get(i);
                    Set<Card> opponentCards = new HashSet<>(table);
                    opponentCards.add(neededCard);
                    neededPairsRemove(opponentCards,opponentFigure,allPairsForOpponent);
                }

                //draws
                Set<Card> opponentCards = new HashSet<>(table); opponentCards.add(neededCard);
                Set<Pair> neededPairs = neededPairs(opponentCards,playerFigure,allPairsForOpponent);
                long loosingAfterDraw = 0;
                long drawingPairs = 0;

                //FLUSH exeception
                if(playerFigure.getCategory()== Figure.Category.FLUSH){
                    for (Pair pair : neededPairs) {
                        if(interrupted)
                            throw new InterruptedException();
                        opponentCards.addAll(pair.getCards());
                        int result = flushResult(playerCards, opponentCards);
                        if (result < 0) loosingAfterDraw++;
                        if(result == 0) drawingPairs++;
                        opponentCards.removeAll(pair.getCards());
                    }
                }
                else{
                    if(statisticsSettings.isDrawWhenSameFigure()) {
                        if (sortedFigures.get(figureIndex).getRanks().size() < 5) {

                            List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
                            for (Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
                            Collections.sort(drawPlayer, Collections.reverseOrder());

                            for (Pair pair : neededPairs) {
                                if(interrupted)
                                    throw new InterruptedException();
                                List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(table));
                                drawOpponent.add(neededCard.getRank());
                                drawOpponent.addAll(getRanks(pair.getCards()));
                                for (Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                                Collections.sort(drawOpponent, Collections.reverseOrder());

                                int cardNumber = 0;
                                for (; cardNumber < 5 - playerFigure.getRanks().size(); cardNumber++) {
                                    if(interrupted)
                                        throw new InterruptedException();
                                    if (!drawPlayer.get(cardNumber).equals(drawOpponent.get(cardNumber))) {
                                        if (drawPlayer.get(cardNumber).getPower() > drawOpponent.get(cardNumber).getPower()) {
                                            loosingAfterDraw++;
                                        }
                                        break;
                                    }
                                }
                                if (cardNumber == 5 - playerFigure.getRanks().size()) drawingPairs++;

                                drawOpponent.removeAll(getRanks(pair.getCards()));

                            }
                        } else drawingPairs = neededPairs.size();
                    }}
                //counting combinations
                long loosingPairs = allPairsForOpponent.size() + loosingAfterDraw;
                loosingCombinations += choose(loosingPairs,players - 1);
                drawAndLooseCombinations += choose(loosingPairs+drawingPairs,players - 1);
                playerCards.remove(neededCard);
            }
            //results
            double chanceOfWinning = ((double)loosingCombinations)/allPairsCombinations;
            double chanceOfDraw = ((double) drawAndLooseCombinations - loosingCombinations)/allPairsCombinations;
            Collection<Card> usedCards = usedCards(playerFigure,oneOfNeededCards,table,hand);
            statistics.add(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards));
            if(recyclerView != null)
                recyclerView.post(() -> {
                    StatisticsViewAdapter adapter = (StatisticsViewAdapter)recyclerView.getAdapter();
                    adapter.addItem(new Statistics(playerFigure,chanceGetting,chanceOfWinning,chanceOfDraw,usedCards).setChanceToGet(getChanceOfGettingTurn(playerFigure, usedCards)));
                });

        }
    }

    private void afterRiver() throws InterruptedException {

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

        if(statisticsSettings.isDrawWhenSameFigure() ){
            List<Card> handList = new ArrayList<>(hand);
            if(playerFigure.getCategory()!= Figure.Category.FLUSH) {
                if (!playerFigure.getRanks().contains(handList.get(0).getRank())
                        && !playerFigure.getRanks().contains(handList.get(1).getRank())) {
                    return;
                }
            }
            if(playerFigure.isSuitSignificant()) {
                List<Card> suited = new ArrayList<>(hand); suited.addAll(table);
                suited = suitedCards(suited);
                if(suited.size() < 5) return;
                Card.Suit suit = suited.get(0).getSuit();
                if (handList.get(0).getSuit() != suit
                        && handList.get(1).getSuit() != suit) {
                    return;
                }
            }
        }

        Set<Pair> allPairsForOpponent = PairGenerator(unused);
        long allPairsCombinations = choose(allPairsForOpponent.size(),players - 1);

        //wins
        for(int i = 0; i<figureIndex && !allPairsForOpponent.isEmpty();i++){
            if(interrupted)
                throw new InterruptedException();
            Figure opponentFigure = sortedFigures.get(i);
            Set<Card> opponentCards = new HashSet<>(table);
            neededPairsRemove(opponentCards,opponentFigure,allPairsForOpponent);
        }

        //draws
        Set<Card> opponentCards = new HashSet<>(table);
        Set<Pair> neededPairs = neededPairs(opponentCards,playerFigure,allPairsForOpponent);
        long draws = 0;
        long loosingAfterDraw = 0;

        //FLUSH exception
        if(playerFigure.getCategory()== Figure.Category.FLUSH){
            for (Pair pair : neededPairs) {
                opponentCards.addAll(pair.getCards());
                int result = flushResult(playerCards, opponentCards);
                if (result < 0) loosingAfterDraw++;
                if(result == 0) draws++;
                opponentCards.removeAll(pair.getCards());
            }
        }
        else{
            if(statisticsSettings.isDrawWhenSameFigure()) {
                if (sortedFigures.get(figureIndex).getRanks().size() < 5) {
                    List<Card.Rank> drawPlayer = new ArrayList<>(getRanks(playerCards));
                    for (Card.Rank rank : playerFigure.getRanks()) drawPlayer.remove(rank);
                    Collections.sort(drawPlayer, Collections.reverseOrder());

                    for (Pair drawPair : neededPairs) {
                        List<Card.Rank> drawOpponent = new ArrayList<>(getRanks(table));
                        drawOpponent.addAll(getRanks(drawPair.getCards()));
                        for (Card.Rank rank : playerFigure.getRanks()) drawOpponent.remove(rank);
                        Collections.sort(drawOpponent, Collections.reverseOrder());
                        int cardNumber = 0;
                        for (; cardNumber < 5 - playerFigure.getRanks().size(); cardNumber++) {
                            if (drawPlayer.get(cardNumber)!=drawOpponent.get(cardNumber)) {
                                if (drawPlayer.get(cardNumber).getPower() > drawOpponent.get(cardNumber).getPower()) {
                                    loosingAfterDraw++;
                                }
                                break;
                            }
                        }
                        if (cardNumber == 5 - playerFigure.getRanks().size()) draws++;
                        drawOpponent.removeAll(getRanks(drawPair.getCards()));
                    }
                }
                else draws = neededPairs.size();
            }
        }
        //results
        long loosingPairs = allPairsForOpponent.size()+loosingAfterDraw;
        long drawingPairs  = draws;
        long loosingCombinations = choose(loosingPairs,players-1);
        double chanceOfWinning = ((double)loosingCombinations)/allPairsCombinations;
        long drawAndLooseCombinations = choose(loosingPairs+drawingPairs,players-1);
        double chanceOfDraw = (((double) drawAndLooseCombinations) -loosingCombinations)/allPairsCombinations;
        Collection<Card> usedCards = usedCards(playerFigure,new HashSet<>(),table,hand);
        statistics.add(new Statistics(playerFigure,1.0,chanceOfWinning,chanceOfDraw,usedCards));
        if(recyclerView != null)
            recyclerView.post(() -> {
                StatisticsViewAdapter adapter = (StatisticsViewAdapter)recyclerView.getAdapter();
                adapter.addItem(new Statistics(playerFigure,1.0,chanceOfWinning,chanceOfDraw,usedCards).setChanceToGet(1.0));
            });
    }

    //Removes card from allCards when card+cards form figure. Returns removed cards
    private Set<Card> neededCards(Figure figure, Set<Card> cards, Set<Card> allCards) {
        Set<Card> toReturn = new HashSet<>();
        if(figure.isSuitSignificant()) {
            List<Card> suitedCards = suitedCards(cards);
            Card.Suit suit = suitedCards.get(0).getSuit();
            if(figure.getCategory()== Figure.Category.FLUSH){
                if(suitedCards.size()<4) return toReturn;
                switch(suitedCards.size()){
                    case 4:
                        if(allCards.size()>26){
                            for(Card.Rank rank : Card.Rank.values()){
                                Card card = new Card(rank,suit);
                                if(allCards.remove(card)) toReturn.add(card);
                            }
                        }
                        else{
                            Iterator<Card> it4 = allCards.iterator();
                            while(it4.hasNext()){
                                Card card = it4.next();
                                if(card.getSuit()==suit){
                                    it4.remove();
                                    toReturn.add(card);
                                }
                            }
                        }
                        return toReturn;
                    default:
                        toReturn = new HashSet<>(allCards);
                        allCards.clear();
                        return toReturn;
                }
            }
            else{
                List<Card.Rank> suitedFigureRanks = new ArrayList<>(figure.getRanks());
                List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards));
                for(Card.Rank rank : suitedCardsRanks) suitedFigureRanks.remove(rank);
                if(suitedFigureRanks.size()>1) return toReturn;
                switch (suitedFigureRanks.size()){
                    case 1:
                        Card card = new Card(suitedFigureRanks.get(0),suit);
                        if(allCards.remove(card)) toReturn.add(card);
                        return toReturn;
                    case 0:
                        toReturn = new HashSet<>(allCards);
                        allCards.clear();
                        return toReturn;
                }
            }
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            if(figureRanks.size()>1) return toReturn;
            switch (figureRanks.size()){
                case 0:
                    toReturn = new HashSet<>(allCards);
                    allCards.clear();
                    return toReturn;
                default:
                    for(Card.Suit suit : Card.Suit.values()){
                        Card card = new Card(figureRanks.get(0),suit);
                        if(allCards.remove(card)) toReturn.add(card);
                    }
                    return toReturn;
            }
        }
        throw new RuntimeException();
    }

    //Removes Pair from allPairs when Pair+cards form figure. Returns removed Pairs
    private Set<Pair> neededPairs(Set<Card> cards, Figure figure, Set<Pair> allPairs) {
        Set<Pair> toReturn = new HashSet<>();
        if(figure.isSuitSignificant()){
            List<Card> suitedCards = suitedCards(cards);
            Iterator<Pair> iterator;
            Card.Suit suit = suitedCards.get(0).getSuit();
            if(figure.getCategory()== Figure.Category.FLUSH){
                if(suitedCards.size()<3) return toReturn;
                switch (suitedCards.size()){
                    case 3:

                        if(allPairs.size()>500){
                            Card.Rank[] ranks = Card.Rank.values();
                            for(int r1=0;r1<ranks.length;r1++){
                                for(int r2=r1+1;r2<ranks.length;r2++){
                                    Pair pair = new Pair(new Card(ranks[r2],suit),new Card(ranks[r1],suit));
                                    if(allPairs.remove(pair)) toReturn.add(pair);
                                }
                            }
                            return toReturn;
                        }
                        else{
                            Iterator<Pair> it3 = allPairs.iterator();
                            while(it3.hasNext()){
                                Pair pair = it3.next();
                                if(pair.getCard1().getSuit()==suit && pair.getCard2().getSuit()==suit){
                                    it3.remove();
                                    toReturn.add(pair);
                                }
                            }
                            return toReturn;
                        }
                    case 4:
                        iterator = allPairs.iterator();
                        while(iterator.hasNext()) {
                            Pair pair = iterator.next();
                            if (pair.getCard1().getSuit() == suit || pair.getCard2().getSuit() == suit) {
                                iterator.remove();
                                toReturn.add(pair);
                            }
                        }
                        return toReturn;
                    default:
                        toReturn = new HashSet<>(allPairs);
                        allPairs.clear();
                        return toReturn;
                }
            }
            else{
                List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
                List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards));
                for(Card.Rank rank : suitedCardsRanks) figureRanks.remove(rank);
                if(suitedCards.size()>2) return toReturn;
                switch (suitedCardsRanks.size()){
                    case 0:
                        toReturn = new HashSet<>(allPairs);
                        allPairs.clear();
                        return toReturn;
                    case 1:
                        Card card = new Card(suitedCardsRanks.get(0),suit);
                        if(allPairs.size()<200){

                            Iterator<Pair> it1 = allPairs.iterator();
                            while(it1.hasNext()) {
                                Pair pair = it1.next();
                                if (pair.getCard1().equals(card) || pair.getCard2().equals(card)) {
                                    it1.remove();
                                    toReturn.add(pair);
                                }
                            }
                            return toReturn;
                        }
                        else{
                            for(Card card2 : unused){
                                Pair pair;
                                if(card.compareTo(card2)<0){
                                    pair = new Pair(card2,card);
                                }
                                else pair = new Pair(card,card2);
                                if(allPairs.remove(pair)) toReturn.add(pair);
                            }
                            return toReturn;
                        }
                    default:
                        Card card1 = new Card(suitedCardsRanks.get(0),suit);
                        Card card2 = new Card(suitedCardsRanks.get(1),suit);
                        Pair pair = new Pair(card1,card2);
                        if(allPairs.remove(pair)) toReturn.add(pair);
                        return toReturn;
                }
            }
        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            Iterator<Pair> iterator;
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            if(figureRanks.size()>2) return toReturn;
            switch (figureRanks.size()){
                case 0:
                    toReturn = new HashSet<>(allPairs);
                    allPairs.clear();
                    return toReturn;
                case 1:
                    iterator = allPairs.iterator();
                    while(iterator.hasNext()) {
                        Pair pair = iterator.next();
                        if (pair.getCard1().getRank()==figureRanks.get(0) || pair.getCard2().getRank()==figureRanks.get(0)) {
                            iterator.remove();
                            toReturn.add(pair);
                        }
                    }
                    return toReturn;

                case 2:
                    Collections.sort(figureRanks,Collections.reverseOrder());
                    Card.Rank rank1 = figureRanks.get(0);
                    Card.Rank rank2 = figureRanks.get(1);
                    if(allPairs.size()>50) {
                        Card.Suit[] suits = Card.Suit.values();
                        for (Card.Suit suit1 : suits) {
                            for (Card.Suit suit2 : suits) {
                                Pair pair = new Pair(new Card(rank1, suit2), new Card(rank2, suit1));
                                if(allPairs.remove(pair)) toReturn.add(pair);
                            }
                        }
                    }
                    else{
                        Iterator<Pair> it2 = allPairs.iterator();
                        while(it2.hasNext()){
                            Pair pair = it2.next();
                            if(pair.getCard1().getRank()==rank1 && pair.getCard2().getRank()==rank2){
                                it2.remove();
                                toReturn.add(pair);
                            }
                        }
                    }
                    return toReturn;
            }
        }
        throw new RuntimeException();
    }

    //Removes Pair from allPairs when Pair+cards form figure
    private void neededPairsRemove(Set<Card> cards, Figure figure, Set<Pair> allPairs) {
        if(figure.isSuitSignificant()){
            List<Card> suitedCards = suitedCards(cards);
            Card.Suit suit = suitedCards.get(0).getSuit();
            if(suitedCards.size()<3) return;
            if(figure.getCategory()== Figure.Category.FLUSH){
                switch (suitedCards.size()){
                    case 3:
                        if(allPairs.size()>500){
                            Card.Rank[] ranks = Card.Rank.values();
                            for(int r1=0;r1<ranks.length;r1++){
                                for(int r2=r1+1;r2<ranks.length;r2++){
                                    Pair pair = new Pair(new Card(ranks[r2],suit),new Card(ranks[r1],suit));
                                    allPairs.remove(pair);
                                }
                            }
                            return;
                        }
                        else{
                            Iterator<Pair> it3 = allPairs.iterator();
                            while(it3.hasNext()){
                                Pair pair = it3.next();
                                if(pair.getCard1().getSuit()==suit && pair.getCard2().getSuit()==suit){
                                    it3.remove();
                                }
                            }
                            return ;
                        }
                    case 4:
                        Iterator<Pair> iterator = allPairs.iterator();
                        while(iterator.hasNext()) {
                            Pair Pair = iterator.next();
                            if (Pair.getCard1().getSuit() == suit || Pair.getCard2().getSuit() == suit) {
                                iterator.remove();
                            }
                        }
                        return;
                    default:
                        allPairs.clear();
                        return;
                }
            }
            else{
                List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
                List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards));
                for(Card.Rank rank : suitedCardsRanks) figureRanks.remove(rank);
                if(suitedCardsRanks.size()>2) return;
                switch (suitedCardsRanks.size()){
                    case 0:
                        allPairs.clear();
                        return;
                    case 1:
                        Card card = new Card(suitedCardsRanks.get(0),suit);
                        if(allPairs.size()<200) {
                            Iterator<Pair> it1 = allPairs.iterator();
                            while (it1.hasNext()) {
                                Pair Pair = it1.next();
                                if (Pair.getCard1().equals(card) || Pair.getCard2().equals(card)) {
                                    it1.remove();
                                }
                            }
                            return;
                        }
                        else{
                            for(Card card2 : unused){
                                Pair pair;
                                if(card.compareTo(card2)<0){
                                    pair = new Pair(card2,card);
                                }
                                else pair = new Pair(card,card2);
                                allPairs.remove(pair);
                            }
                            return;
                        }
                    default:
                        Collections.sort(suitedCardsRanks,Collections.reverseOrder());
                        Card card1 = new Card(suitedCardsRanks.get(0),suit);
                        Card card2 = new Card(suitedCardsRanks.get(1),suit);
                        Pair pair = new Pair(card1,card2);
                        allPairs.remove(pair);
                        return;
                }
            }

        }
        else{
            List<Card.Rank> figureRanks = new ArrayList<>(figure.getRanks());
            List<Card.Rank> cardsRanks = new ArrayList<>(getRanks(cards));
            Iterator<Pair> iterator;
            for(Card.Rank rank : cardsRanks) figureRanks.remove(rank);
            if(figureRanks.size()>2) return;
            switch (figureRanks.size()){
                case 0:
                    allPairs.clear();
                    return;
                case 1:
                    iterator = allPairs.iterator();
                    while(iterator.hasNext()) {
                        Pair Pair = iterator.next();
                        if (Pair.getCard1().getRank()==figureRanks.get(0) || Pair.getCard2().getRank()==figureRanks.get(0)) {
                            iterator.remove();
                        }
                    }
                    return;

                case 2:
                    Collections.sort(figureRanks,Collections.reverseOrder());
                    Card.Rank rank1 = figureRanks.get(0);
                    Card.Rank rank2 = figureRanks.get(1);
                    if(allPairs.size()>50) {
                        Card.Suit[] suits = Card.Suit.values();
                        for (Card.Suit suit1 : suits) {
                            for (Card.Suit suit2 : suits) {
                                Pair pair = new Pair(new Card(rank1, suit2), new Card(rank2, suit1));
                                allPairs.remove(pair);
                            }
                        }
                    }
                    else{
                        Iterator<Pair> it2 = allPairs.iterator();
                        while(it2.hasNext()){
                            Pair pair = it2.next();
                            if(pair.getCard1().getRank()==rank1 && pair.getCard2().getRank()==rank2){
                                it2.remove();
                            }
                        }
                    }
                    return;
            }
        }
        throw new RuntimeException();
    }

    //Returns true if cards form figure
    private boolean isFigure(Figure figure, Collection<Card> cards) {

        if(figure.isSuitSignificant()) {
            List<Card> suitedCards = suitedCards(cards);
            if(figure.getCategory()== Figure.Category.FLUSH){
                return suitedCards.size()>=5;
            }
            else{
                List<Card.Rank> suitedFigureRanks = new ArrayList<>(figure.getRanks());
                List<Card.Rank> suitedCardsRanks = new ArrayList<>(getRanks(suitedCards));
                for(Card.Rank rank : suitedCardsRanks) suitedFigureRanks.remove(rank);
                return suitedFigureRanks.isEmpty();
            }
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

    //Returns 1 if player wins, returns -1 if opponent wins, draw 0 (both have FLUSH)
    private int flushResult(Set<Card> playerCards, Set<Card> opponentCards) {
        List<Card> player = suitedCards(playerCards);
        List<Card> opponent = suitedCards(opponentCards);
        Collections.sort(player,Collections.reverseOrder());
        Collections.sort(opponent,Collections.reverseOrder());
        for(int i=0;i<5;i++){
            Card playerCard = player.get(i);
            Card opponentCard = opponent.get(i);
            if(playerCard.getRank()!=opponentCard.getRank()){
                return playerCard.getRank().getPower().compareTo(opponentCard.getRank().getPower());
            }
        }
        return 0;
    }

    //returns used cards from hand,table,other
    static private Collection<Card> usedCards(Figure figure,Collection<Card> other,Collection<Card> table, Collection<Card> hand){
        Set<Card> setToReturn = new HashSet<>();
        List<Card.Rank> ranks =  new ArrayList<>(figure.getRanks());
        List<Card> list = new ArrayList<>();
        list.addAll(hand); list.addAll(table); list.addAll(other);
        if(!figure.isSuitSignificant()){
            for(Card card : list) {
                if (ranks.contains(card.getRank())) {
                    setToReturn.add(card);
                    ranks.remove(card.getRank());
                    if(ranks.isEmpty()) return setToReturn;
                }
            }
        }
        else{
            List<Card> suitedList = suitedCards(list);
            if(figure.getCategory()== Figure.Category.FLUSH){

                Collections.sort(suitedList,Collections.reverseOrder());
                for(int i=0;i<5;i++) setToReturn.add(suitedList.get(i));
                return setToReturn;
            }
            for(Card card : suitedList) {
                if (ranks.contains(card.getRank())) {
                    setToReturn.add(card);
                    ranks.remove(card.getRank());
                    if(ranks.isEmpty()) return setToReturn;
                }

            }
        }
        throw new RuntimeException();
    }

    //returns list of cards with most common suit
    private static List<Card> suitedCards(Collection<Card> cards) {
        List<List<Card>> suitedCards = new ArrayList<>();
        for(int i=0;i<4;i++) suitedCards.add(new ArrayList<>());
        for(Card card : cards){
            suitedCards.get(card.getSuit().getPower()-1).add(card);
        }
        int max = 0;
        for(int i=0;i<4;i++){
            if(suitedCards.get(i).size()>suitedCards.get(max).size()) max=i;
        }
        return suitedCards.get(max);
    }

    //Binomial coefficient
    static private long choose(long n, int k) {
        long result = 1;
        for(int i = 0; i < k ; i++){
            result *= (n-i); result/=(i+1);
        }
        return result;
    }

    //generates all Pairs from collection of cards
    static private Set<Pair> PairGenerator(Collection<Card> cards) {
        List<Card> cardsList = new ArrayList<>(cards);
        Collections.sort(cardsList,Collections.reverseOrder());
        Set<Pair> Pairs = new HashSet<>();
        for(int i=0;i<cardsList.size();i++){
            Card card1 = cardsList.get(i);
            for(int j=i+1;j<cardsList.size();j++){
                Card card2 = cardsList.get(j);
                Pairs.add(new Pair(card1,card2));
            }
        }
        return Pairs;
    }

}
