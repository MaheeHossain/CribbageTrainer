package scoring;

import ch.aplu.jcardgame.Card;
import cribbage.Score;

import java.util.ArrayList;

public class ConcreteStarterCompositeStrategy extends StarterComposite {
    private ArrayList<StarterStrategy> starterStrategies = new ArrayList<>();
    ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Card starter) {
        /* Get all the starter scores for this segment */
        for (StarterStrategy starterStrategy: starterStrategies) {
            scores.addAll(starterStrategy.score(starter));
        }
        return scores;
    }

    /* Add all the starter strategies we want to use */
    public void addStarterScoringRulesStrategies(StarterStrategy starterStrategy) {
        starterStrategies.add(starterStrategy);
    }
}
