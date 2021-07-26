package scoring;

import ch.aplu.jcardgame.Deck;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public class ConcretePlayRulesStrategyComposite extends PlayCompositeStrategy {
    private ArrayList<PlayScoringRulesStrategy> playScoringRulesStrategies = new ArrayList<>();
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Cribbage.Segment segment, Deck deck) {
        scores.clear();
        /* Get all the play scores for this segment, and remove the nulls */
        for (PlayScoringRulesStrategy playScoringRulesStrategy: playScoringRulesStrategies) {
            scores.addAll(playScoringRulesStrategy.score(segment, deck));
        }
        scores.remove(null);
        return scores;
    }

    /* Add all the play strategies we want to use */
    public void addPlayScoringRulesStrategies(PlayScoringRulesStrategy playScoringRulesStrategy) {
        playScoringRulesStrategies.add(playScoringRulesStrategy);
    }
}
