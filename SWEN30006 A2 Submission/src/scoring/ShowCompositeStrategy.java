package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Score;

import java.util.ArrayList;

public abstract class ShowCompositeStrategy implements ShowScoringRulesStrategy {
    protected ArrayList<ShowScoringRulesStrategy> showScoringRulesStrategies = new ArrayList<>();
    protected ArrayList<Score> scores = new ArrayList<>();

    @Override
    public abstract ArrayList<Score> score(Hand hand, Card starter, Deck deck);

    public abstract void addShowScoringRulesStrategies(ShowScoringRulesStrategy showScoringRulesStrategy);

}
