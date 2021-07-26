package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Score;

import java.util.ArrayList;

public class ConcreteShowRulesStrategyComposite extends ShowCompositeStrategy {

    @Override
    public ArrayList<Score> score(Hand hand, Card starter, Deck deck) {
        scores.clear();
        for (ShowScoringRulesStrategy showScoringRulesStrategy: showScoringRulesStrategies) {
            scores.addAll(showScoringRulesStrategy.score(hand, starter, deck));
        }
        return scores;
    }

    public void addShowScoringRulesStrategies(ShowScoringRulesStrategy showScoringRulesStrategy) {
        showScoringRulesStrategies.add(showScoringRulesStrategy);
    }


}
