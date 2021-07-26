package scoring;

import ch.aplu.jcardgame.*;
import cribbage.Score;

import java.util.ArrayList;

public interface ShowScoringRulesStrategy {
    ArrayList<Score> score(Hand hand, Card starter, Deck deck);
}
