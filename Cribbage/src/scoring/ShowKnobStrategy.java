package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Suit;
import cribbage.Score;

import java.util.ArrayList;

public class ShowKnobStrategy implements ShowScoringRulesStrategy {
    private final static String phase = "show";
    private final static String scoreEvent = "jack";
    private final int points = 1;
    private final static int JACKVALUE = 11;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();

        /* Check if each card is of the same suit as the starter card and is a Jack */
        for (Card c: hand.getCardList()) {
            if (Cribbage.cardOrder(c) == JACKVALUE && ((Suit)starter.getSuit()).equals((Suit)c.getSuit())) {

                scores.add(new Score(phase, scoreEvent, points, listify(c)));
                return scores;
            }
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
