package scoring;
import ch.aplu.jcardgame.*;

import cribbage.Cribbage;
import cribbage.Cribbage.Suit;
import cribbage.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ShowPairsStrategy implements ShowScoringRulesStrategy {
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();
        Hand allHand = new Hand(deck);
        for (Card c: hand.getCardList()) {
            allHand.insert(c.getCardNumber(), false);
        }
        allHand.insert(starter.getCardNumber(), false);

        HashMap<Integer, Integer> pairs = new HashMap<>();
        for (Card c: allHand.getCardList()) {
            if (pairs.containsKey(Cribbage.cardOrder(c))) {
                pairs.put(Cribbage.cardOrder(c), pairs.get(Cribbage.cardOrder(c)) + 1);
            } else {
                pairs.put(Cribbage.cardOrder(c), 1);
            }
        }

        // get pairs from length2 to length4
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == 2) {
                scores.add(new Score("show", "pair2", factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == 3) {
                scores.add(new Score("show", "pair3", factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == 4) {
                scores.add(new Score("show", "pair4", factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }

        return scores;
    }

    // get the cards for a relative rank
    private ArrayList<Card> getCardsFromRank(Hand allHand, int order, Deck deck) {
        Hand pair = new Hand(deck);
        for (Card c: allHand.getCardList()) {
            if (Cribbage.cardOrder(c) == order) {
                pair.insert(c.getCardNumber(), false);
            }
        }
        pair.sort(Hand.SortType.POINTPRIORITY, false);
        return pair.getCardList();
    }

    private static int factorial(int n){
        if (n == 0)
            return 1;
        else
            return(n * factorial(n-1));
    }

}
