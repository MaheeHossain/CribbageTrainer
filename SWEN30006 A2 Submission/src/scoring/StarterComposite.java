package scoring;

import ch.aplu.jcardgame.Card;
import cribbage.Score;

import java.util.ArrayList;

public abstract class StarterComposite implements StarterStrategy {
    private ArrayList<StarterStrategy> starterStrategies = new ArrayList<>();
    ArrayList<Score> scores = new ArrayList<>();

    @Override
    public abstract ArrayList<Score> score(Card starter);

    public abstract void addStarterScoringRulesStrategies(StarterStrategy starterStrategy);
}
