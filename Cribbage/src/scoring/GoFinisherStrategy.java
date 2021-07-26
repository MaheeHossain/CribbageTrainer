package scoring;

import ch.aplu.jcardgame.Card;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

import static cribbage.Cribbage.cardValue;

public class GoFinisherStrategy implements FinisherStrategy {
    private final static String phase = "play";
    private final static String scoreEvent = "go";
    private final int points = 1;
    private final static int TARGET = 31;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Cribbage.Segment segment) {
        scores.clear();
        /* If segment doesn't total thirty-one, go applies, so return the score */
        if (total(segment) < TARGET) {
            Score score = new Score(phase, scoreEvent, points, segment.segment.getCardList());
            scores.add(score);
        }
        return scores;
    }

    /* Returns the total face value for the segment */
    private int total(Cribbage.Segment segment) {
        int total = 0;
        for (Card c: segment.segment.getCardList()) {
            total += cardValue(c);
        }
        return total;
    }
}