package scoring;

import ch.aplu.jcardgame.*;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public class JackStarterStrategy implements StarterStrategy {
    private final static String phase = "starter";
    private final static String scoreEvent = "starter";
    private final static int JACKVALUE = 11;
    private final int points = 2;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Card card) {
        scores.clear();
        /* Check if the starter card is a Jack */
        if (Cribbage.cardOrder(card) == JACKVALUE) {
            scores.add(new Score(phase, scoreEvent, points, listify(card)));
            return scores;
        }
        return scores;
    }

    /* Returns a list with one card to make it consistent with the scoring call */
    private ArrayList<Card> listify(Card card) {
        ArrayList<Card> list = new ArrayList<>();
        list.add(card);
        return list;
    }
}
