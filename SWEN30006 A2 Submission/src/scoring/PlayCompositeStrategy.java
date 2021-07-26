package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;

public abstract class PlayCompositeStrategy implements PlayScoringRulesStrategy {
    private ArrayList<PlayScoringRulesStrategy> playScoringRulesStrategies = new ArrayList<>();
    private ArrayList<Score> scores = new ArrayList<>();

    @Override
    public abstract ArrayList<Score> score(Segment segment, Deck deck);

    public abstract void addPlayScoringRulesStrategies(PlayScoringRulesStrategy playScoringRulesStrategy);
}
