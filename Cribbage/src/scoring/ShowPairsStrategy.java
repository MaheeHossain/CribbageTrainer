package scoring;
import ch.aplu.jcardgame.*;

import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowPairsStrategy implements ShowScoringRulesStrategy {
    private final static String phase = "show";
    private final static String scoreEvent = "pair";
    private final int length2 = 2, length3 = 3, length4 = 4;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();

        /* Make a new hand with the starter included */
        Hand allHand = new Hand(deck);
        for (Card c: hand.getCardList()) {
            allHand.insert(c.getCardNumber(), false);
        }
        allHand.insert(starter.getCardNumber(), false);

        /* Make a hashmap of all orders in the hand with how often they appear */
        HashMap<Integer, Integer> pairs = new HashMap<>();
        for (Card c: allHand.getCardList()) {
            if (pairs.containsKey(Cribbage.cardOrder(c))) {
                pairs.put(Cribbage.cardOrder(c), pairs.get(Cribbage.cardOrder(c)) + 1);
            } else {
                pairs.put(Cribbage.cardOrder(c), 1);
            }
        }

        /* If any of the ranks have multiple cards in it (2/3/4),
         * its is a pair of that length                           */
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == length2) {
                scores.add(new Score(phase, scoreEvent+length2, factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == length3) {
                scores.add(new Score(phase, scoreEvent+length3, factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }
        for (Integer rank: pairs.keySet()) {
            if (pairs.get(rank) == length4) {
                scores.add(new Score(phase, scoreEvent+length4, factorial(pairs.get(rank)), getCardsFromRank(allHand, rank, deck)));
            }
        }

        return scores;
    }

    /* Get the cards for a relative rank in a sorted ArrayList */
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

    /* Returns the factorial of a number (used for scoring) */
    private static int factorial(int n){
        if (n == 0)
            return 1;
        else
            return(n * factorial(n-1));
    }

}
