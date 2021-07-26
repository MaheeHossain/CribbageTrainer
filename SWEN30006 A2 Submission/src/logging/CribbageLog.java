package logging;

import cribbage.CribbageObserverBase;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.ArrayList;

public class CribbageLog implements CribbageObserverBase {
    LogStringStrategy logStringStrategy;
    PrintWriter pw;

    public CribbageLog() {
        try {
            this.pw = new PrintWriter(new FileWriter("cribbage.log"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String event, String player, String cardList, String scoreEvent, String card,
                       String playerClass, String seed, String segmentValue, String totalScore, String score) {
        ArrayList<String> info = new ArrayList<String>();
        LogStringStrategyFactory factory = LogStringStrategyFactory.getInstance();
        logStringStrategy = factory.getLogStringStrategy(event);
        switch (event) {
            case "seed":
                info.add(event);
                info.add(seed);
                break;
            case "playerInitialisation":
                info.add(playerClass);
                info.add(player);
                break;
            case "deal":
                info.add(event);
                info.add(player);
                info.add(cardList);
                break;
            case "discard":
                info.add(event);
                info.add(player);
                info.add(cardList);
                break;
            case "starter":
                info.add(event);
                info.add(card);
                break;
            case "play":
                info.add(event);
                info.add(player);
                info.add(segmentValue);
                info.add(card);
                break;
            case "show":
                info.add(event);
                info.add(player);
                info.add(card);
                info.add(cardList);
                break;
            case "score":
                info.add(event);
                info.add(player);
                info.add(totalScore);
                info.add(score);
                info.add(scoreEvent);
                if (cardList != null) {
                    info.add(cardList);
                }
                break;
        }
        String logString = logStringStrategy.formLogString(info);
        printToLog(logString);
    }

    public void printToLog(String logString) {
        pw.println(logString);
        pw.flush();
    }
}
