package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;
import java.util.Collections;

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
        // If runSize has gone over 7, impossible so return
        if (runSize > 7) {
            System.out.println(score);
            scores.add(score);
            return scores;
        }
        // If segment size can fit this run
        if (playedCards.size() >= runSize) {
            // Make arraylist with last {runSize} cards played
            ArrayList<Card> run = new ArrayList<Card>();
            for (int i=1; i<=runSize; i++) {
                run.add(playedCards.get(playedCards.size()-i));
            }
            // If it is consecutive, set up score, call again with next size
            if (consecutive(run, runSize, deck)) {
                // Sort run
                score = new Score(phase, scoreEvent+runSize, runSize, run);
                recursiveScore(playedCards, runSize+1, score, deck);
            }
            // If not consecutive, return previous score
            else {
                scores.add(score);
                return scores;
            }
        }
        // If segment size can't fit this run, return previous score
        else {
            scores.add(score);
        }
        return scores;
    }

    private boolean consecutive(ArrayList<Card> cards, int runSize, Deck deck) {
        Hand cardList = new Hand(deck);
        for (Card c: cards) {
            cardList.insert(c.getCardNumber(), false);
        }
        cardList.sort(Hand.SortType.POINTPRIORITY, false);
        for (int i=0; i<runSize-1; i++) {
            if (Cribbage.cardOrder(cardList.getCardList().get(i)) != Cribbage.cardOrder(cardList.getCardList().get(i+1))-1) {
                return false;
            }
        }
        return true;
    }
}
