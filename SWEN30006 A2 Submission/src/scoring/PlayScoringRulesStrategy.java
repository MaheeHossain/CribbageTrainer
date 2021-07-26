package scoring;

import ch.aplu.jcardgame.Deck;
import cribbage.Cribbage.Segment;
import cribbage.Score;

import java.util.ArrayList;

public interface PlayScoringRulesStrategy {
    ArrayList<Score> score(Segment segment, Deck deck);
}
