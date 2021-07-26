package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Cribbage.Suit;
import cribbage.Score;

import java.util.*;

public class ShowFlushStrategy implements ShowScoringRulesStrategy {
    private final static String phase = "show";
    private final static String scoreEvent5 = "flush5", scoreEvent4 = "flush4";
    private final int points5 = 5, points4 = 4;
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();

        /* Make a new sorted hand with the starter included (to print for flush5) */
        Hand allHand = new Hand(deck);
        for (Card c: hand.getCardList()) {
            allHand.insert(c.getCardNumber(), false);
        }
        allHand.insert(starter.getCardNumber(), false);
        allHand.sort(Hand.SortType.POINTPRIORITY, false);

        /* Make a hashset with all the suits inside the hand */
        HashSet<Suit> suits = new HashSet<>();
        for (Card c: hand.getCardList()) {
            suits.add((Suit)c.getSuit());
        }

        /* If only one suit in hand, then it is a flush 4 */
        if (suits.size() == 1) {

            /* However, if starter has same the suit as the hand, then flush 5 */
            if (suits.contains((Suit)starter.getSuit())) {
                scores.add(new Score(phase, scoreEvent5, points5, allHand.getCardList()));
                return scores;
            }

            scores.add(new Score(phase, scoreEvent4, points4, hand.getCardList()));
            return scores;
        }
        return scores;
    }
}
