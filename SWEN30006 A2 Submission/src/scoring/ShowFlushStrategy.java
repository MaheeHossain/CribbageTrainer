package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Suit;
import cribbage.Score;

import java.util.*;

public class ShowFlushStrategy implements ShowScoringRulesStrategy {
    //Arraylist of scores
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();
        Hand allHand = new Hand(deck);
        for (Card c: hand.getCardList()) {
            allHand.insert(c.getCardNumber(), false);
        }
        allHand.insert(starter.getCardNumber(), false);
        allHand.sort(Hand.SortType.POINTPRIORITY, false);
        HashSet<Suit> suits = new HashSet<>();
        for (Card c: hand.getCardList()) {
            suits.add((Suit)c.getSuit());
        }
        // If only one suit in hand, then flush 4
        if (suits.size() == 1) {
            // If starter has same suit as hand, then flush 5
            if (suits.contains((Suit)starter.getSuit())) {
                //Score flushFour
                scores.add(new Score("show", "Flush 5", 5, allHand.getCardList()));
                return scores;
            }
            scores.add(new Score("show", "Flush 4", 4, hand.getCardList()));
            return scores;
        }
        return scores;//empty
    }

}
