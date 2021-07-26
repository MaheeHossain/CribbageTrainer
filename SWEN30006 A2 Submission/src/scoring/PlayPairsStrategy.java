package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;

public class PlayPairsStrategy implements PlayScoringRulesStrategy {
    private final static String phase = "play";
    private final static String scoreEvent = "pair";
    private final int smallestPair = 2;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Segment segment, Deck deck) {
        scores.clear();
        ArrayList<Card> playedCards = segment.segment.getCardList();
        if (playedCards.size() < smallestPair) { return scores; }
        return recursiveScore(playedCards, smallestPair, null);
    }

    public ArrayList<Score> recursiveScore(ArrayList<Card> playedCards, int pairSize, Score score) {
        // If pairSize has gone over 4, impossible to return
        if (pairSize > 4) {
            scores.add(score);
            return scores;
        }

        // If segment size can fit this run
        if (playedCards.size() >= pairSize) {
            // Make arraylist with last {pairSize} cards played
            ArrayList<Card> pair = new ArrayList<Card>();
            int pairRank = Cribbage.cardOrder(playedCards.get(playedCards.size()-1));
            for (int i=1; i<=pairSize; i++) {
                Card c = playedCards.get(playedCards.size()-i);
                pair.add(c);
                // If any of the cards don't match the pairRank, return scores
                if (pairRank != Cribbage.cardOrder(c)) {
                    if (score != null) {
                        scores.add(score);
                    }
                    return scores;
                }
            }
            // If all the cards match pairRank, make score and call recursive
            score = new Score(phase, scoreEvent+pairSize, factorial(pairSize), pair);
            recursiveScore(playedCards, pairSize+1, score);

        }

        else {
            if (score != null) {
                scores.add(score);
            }
        }
        return scores;
    }

    private static int factorial(int n){
        if (n == 0)
            return 1;
        else
            return(n * factorial(n-1));
    }
}