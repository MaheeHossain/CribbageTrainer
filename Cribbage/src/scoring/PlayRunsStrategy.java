package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;

public class PlayRunsStrategy implements PlayScoringRulesStrategy {
    private final static String phase = "play";
    private final static String scoreEvent = "run";
    private final int smallestRun = 3;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Segment segment, Deck deck) {
        scores.clear();
        ArrayList<Card> playedCards = segment.segment.getCardList();
        return recursiveScore(playedCards, smallestRun, null, deck);
    }

    private ArrayList<Score> recursiveScore(ArrayList<Card> playedCards, int runSize, Score score, Deck deck) {
        /* If runSize has gone over 7, can't go higher, so return previous score */
        if (runSize > 7) {
            System.out.println(score);
            scores.add(score);
            return scores;
        }
        /* If segment size can fit a run of runSize */
        if (playedCards.size() >= runSize) {

            /* Make arraylist with the last {runSize} cards played */
            ArrayList<Card> run = new ArrayList<Card>();
            for (int i=1; i<=runSize; i++) {
                run.add(playedCards.get(playedCards.size()-i));
            }

            /* If it is consecutive, set up score for this run, call again with next runSize */
            if (consecutive(run, runSize, deck)) {
                score = new Score(phase, scoreEvent+runSize, runSize, run);
                recursiveScore(playedCards, runSize+1, score, deck);
            }

            /* If not consecutive, return previous score */
            else {
                scores.add(score);
                return scores;
            }
        }
        /* If segment size can't fit a run of runSize, return previous score */
        else {
            scores.add(score);
        }
        return scores;
    }

    /* Checks to see if a run is consecutive or not */
    private boolean consecutive(ArrayList<Card> cards, int runSize, Deck deck) {

        /* Makes a new hand with the run and sorts it */
        Hand cardList = new Hand(deck);
        for (Card c: cards) {
            cardList.insert(c.getCardNumber(), false);
        }
        cardList.sort(Hand.SortType.POINTPRIORITY, false);

        /* If any card is not the same as the previous card's order minus one,
        *  it is not consecutive, so return false. This also removes the Q,K,A
        *  run as A has the order of 1 so A is not counted as consecutive to K  */
        for (int i=0; i<runSize-1; i++) {
            if (Cribbage.cardOrder(cardList.getCardList().get(i)) != Cribbage.cardOrder(cardList.getCardList().get(i+1))-1) {
                return false;
            }
        }
        return true;
    }
}
