package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;

import static cribbage.Cribbage.cardValue;

public class PlayAddToFifteenStrategy implements PlayScoringRulesStrategy {
    private final static String phase = "play";
    private final static String scoreEvent = "fifteen";
    private final int points = 2;
    private final static int target = 15;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Segment segment, Deck deck) {
        scores.clear();
        if (total(segment) == target) {
            Score score = new Score(phase, scoreEvent, points, segment.segment.getCardList());
            scores.add(score);
        }
        return scores;
    }

    private int total(Segment segment) {
        int total=0;
        for (Card c: segment.segment.getCardList()) {
            total += cardValue(c);
        }
        return total;
    }
}
