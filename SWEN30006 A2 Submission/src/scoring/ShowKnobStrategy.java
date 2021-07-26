package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Suit;
import cribbage.Score;

import java.util.ArrayList;

public class ShowKnobStrategy implements ShowScoringRulesStrategy {
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();
        for (Card c: hand.getCardList()) {
            // check if the card is of the same suit as the starter card and is a Jack
            if (Cribbage.cardOrder(c) == 11 && ((Suit)starter.getSuit()).equals((Suit)c.getSuit())) {
                ArrayList<Card> knob = new ArrayList<>();
                knob.add(starter);
                scores.add(new Score("show", "jack",1, knob));
                return scores;
            }
        }
        return scores;
    }
}
