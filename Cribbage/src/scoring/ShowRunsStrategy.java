package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public class ShowRunsStrategy implements ShowScoringRulesStrategy {
    private final static String phase = "show";
    private final static String scoreEvent = "run";
    private final int minRun = 3, maxRun=5;
    private final static int ACEVALUE = 1;
    private final static int KINGVALUE = 13;
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
        allHand.sort(Hand.SortType.POINTPRIORITY, false);

        /* iteratively checking the existence of run3, run4 and run5 */
        for (int i=minRun; i<=maxRun; i++) {
            addRuns(allHand, i, deck);
        }
        return scores;
    }

    private void addRuns(Hand allHand, int length, Deck deck) {

        /* extract all the possible runs from the combination of starter and the hand */
        Hand[] runs = allHand.extractSequences(length);
        for (Hand h: runs) {

            /* check whether it is a valid run and sort it */
            if (!checkRun(h)) {
                h.sort(Hand.SortType.POINTPRIORITY, false);
                scores.add(new Score(phase, scoreEvent+length, length, h.getCardList()));
            } else {

                /* if it is a run of containing Ace and King together then remove the Ace to see if any runs
                * can still be extracted from the leftovers */
                Hand newHand = new Hand(deck);
                for (Card c: h.getCardList()) {
                    if (Cribbage.cardOrder(c) != 1) {
                        newHand.insert(c.getCardNumber(), false);
                    }
                }
                if (newHand.extractSequences(minRun).length > 0) {
                    for (Hand h1: newHand.extractSequences(minRun)) {
                        h1.sort(Hand.SortType.POINTPRIORITY, false);
                        scores.add(new Score(phase, scoreEvent+minRun, minRun, h1.getCardList()));
                    }
                }
            }
        }
    }

    /* check the validity of the run */
    private boolean checkRun(Hand run) {
        boolean containAce = false;
        boolean containKing = false;

        /* check if the run contains both the Ace and the King and if so, the run is not a valid run */
        for (Card c: run.getCardList()) {
            if (Cribbage.cardOrder(c) == KINGVALUE) {
                containKing = true;
            } else if (Cribbage.cardOrder(c) == ACEVALUE) {
                containAce = true;
            }
        }
        return containAce && containKing;
    }
}
