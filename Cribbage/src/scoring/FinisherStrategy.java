package scoring;

import cribbage.Cribbage;
import cribbage.Score;

import java.util.ArrayList;

public interface FinisherStrategy {
    ArrayList<Score> score(Cribbage.Segment segment);
}
