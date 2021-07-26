package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import cribbage.Cribbage.Segment;
import cribbage.Score;
import static cribbage.Cribbage.cardValue;

import java.util.ArrayList;


public class AddToThirtyOneStrategy implements PlayScoringRulesStrategy {
    private final static String phase = "play";
    private final static String scoreEvent = "thirtyone";
    private final int points = 2;
    private final static int TARGET = 31;
    ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Segment segment, Deck deck) {
        scores.clear();
        /* If segment totals thirty-one, return the score */
        if (total(segment) == TARGET) {
            Score score = new Score(phase, scoreEvent, points, segment.segment.getCardList());
            scores.add(score);
        }
        return scores;
    }

    /* Returns the total face value for the segment */
    private int total(Segment segment) {
        int total=0;
        for (Card c: segment.segment.getCardList()) {
            total += cardValue(c);
        }
        return total;
    }
}