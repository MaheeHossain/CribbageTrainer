package scoring;

import ch.aplu.jcardgame.Card;
import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public class ConcreteFinisherStrategyComposite extends FinisherComposite {
    private ArrayList<FinisherStrategy> finisherStrategies = new ArrayList<>();
    ArrayList<Score> scores = new ArrayList<>();

    @Override
    public ArrayList<Score> score(Cribbage.Segment segment) {
        for (FinisherStrategy finisherStrategy: finisherStrategies) {
            scores.addAll(finisherStrategy.score(segment));
        }
        return scores;
    }

    public void addFinisherScoringRulesStrategies(FinisherStrategy finisherStrategy) {
        finisherStrategies.add(finisherStrategy);
    }
}
