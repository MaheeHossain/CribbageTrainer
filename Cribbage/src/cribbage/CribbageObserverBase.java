package cribbage;
import ch.aplu.jcardgame.Card;
import java.util.*;

public interface CribbageObserverBase {
    void update(String event, String player, String cardList, String scoreEvent, String card, String playerClass,
                String seed, String segmentValue, String totalScore, String score);
}
