package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowRunsStrategy implements ShowScoringRulesStrategy {
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
        for (int i=3; i<6; i++) {
            addRuns(allHand, i, deck);
        }
        return scores;
    }

    private void addRuns(Hand allHand, int length, Deck deck) {
        // System.out.println(allHand.getCardList());
        Hand[] runs = allHand.extractSequences(length);
        for (Hand h: runs) {
            if (!checkRun(h)) {
                h.sort(Hand.SortType.POINTPRIORITY, false);
                scores.add(new Score("show", "run" + length, length, h.getCardList()));
            } else {
                Hand newHand = new Hand(deck);
                for (Card c: h.getCardList()) {
                    if (Cribbage.cardOrder(c) != 1) {
                        newHand.insert(c.getCardNumber(), false);
                    }
                }
                if (newHand.extractSequences(3).length > 0) {
                    for (Hand h1: newHand.extractSequences(3)) {
                        h1.sort(Hand.SortType.POINTPRIORITY, false);
                        scores.add(new Score("show", "run3", 3, h1.getCardList()));
                    }
                }
            }
        }
    }

    private boolean checkRun(Hand run) {
        boolean containAce = false;
        boolean containKing = false;
        for (Card c: run.getCardList()) {
            if (Cribbage.cardOrder(c) == 13) {
                containKing = true;
            } else if (Cribbage.cardOrder(c) == 1) {
                containAce = true;
            }
        }
        return containAce && containKing;
    }
}
