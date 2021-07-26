package scoring;

import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public abstract class FinisherComposite implements FinisherStrategy {
    protected ArrayList<ShowScoringRulesStrategy> showScoringRulesStrategies = new ArrayList<>();
    protected ArrayList<Score> scores = new ArrayList<>();
    @Override
    public abstract ArrayList<Score> score(Cribbage.Segment segment);

    public abstract void addFinisherScoringRulesStrategies(FinisherStrategy finisherStrategy);
}
