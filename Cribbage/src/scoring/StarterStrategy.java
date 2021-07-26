package scoring;

import ch.aplu.jcardgame.*;
import cribbage.Score;

import java.util.ArrayList;

public interface StarterStrategy {
    ArrayList<Score> score(Card starter);
}
