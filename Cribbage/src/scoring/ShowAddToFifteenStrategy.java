package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.*;

public class ShowAddToFifteenStrategy implements ShowScoringRulesStrategy {
    private final static String phase = "show";
    private final static String scoreEvent = "fifteen";
    private final int points = 2;
    private ArrayList<Score> scores = new ArrayList<>();
    private static final int TARGET = 15;

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();

        /* Make a new hand with the starter included */
        Hand allHand = new Hand(deck);
        for (Card c: hand.getCardList()) {
            allHand.insert(c.getCardNumber(), false);
        }
        allHand.insert(starter.getCardNumber(), false);

        findSubsets(allHand, deck);
        return scores;
    }


    /** Acknowledgement: This function is referred from https://www.geeksforgeeks.org/perfect-sum-problem/
     * with modifications made to fit our goal
     */
    /* Function to score the subsets whose
    *  sum is equal to the given target fifteen */
    public void sumSubsets(Hand allHand, int n, int target, Deck deck) {
        /* Create the new array with size
        *  equal to array set[] to create
        *  binary array as per n(decimal number) */
        int handSize = allHand.getCardList().size();

        int[] x = new int[handSize];
        int j = handSize - 1;

        /* Convert the array into binary array */
        while (n > 0) {
            x[j] = n % 2;
            n = n / 2;
            j--;
        }

        int sum = 0;

        /* Calculate the sum of this subset */
        for (int i = 0; i < handSize; i++) {
            if (x[i] == 1) {
                sum += Cribbage.cardValue(allHand.getCardList().get(i));
            }
        }

        /* Check whether sum is equal to the target
        *  if it is equal, then score the subset    */
        if (sum == target) {
            Hand fifteens = new Hand(deck);
            for (int i = 0; i < handSize; i++) {
                if (x[i] == 1) {
                    fifteens.insert(allHand.getCardList().get(i), false);
                }
            }
            fifteens.sort(Hand.SortType.POINTPRIORITY, false);
            scores.add(new Score(phase, scoreEvent, points, fifteens.getCardList()));
        }
    }

    /* Function to find the subsets with the sum of the target fifteen */
    public void findSubsets(Hand allHand, Deck deck) {

        /* Calculate the total no. of subsets */
        int x = (int)Math.pow(2, allHand.getCardList().size());

        /* Run loop till total no. of subsets
        *  and call the function for each subset */
        for (int i = 1; i < x; i++) {
            sumSubsets(allHand, i, TARGET, deck);
        }
    }

}
