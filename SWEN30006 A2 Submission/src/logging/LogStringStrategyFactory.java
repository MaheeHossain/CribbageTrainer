package logging;

import scoring.*;

public class LogStringStrategyFactory {
    private static LogStringStrategyFactory instance = null;

    private LogStringStrategyFactory() {

    }

    public static LogStringStrategyFactory getInstance() {
        if (instance == null) {
            instance = new LogStringStrategyFactory();
        }
        return instance;
    }

    public LogStringStrategy getLogStringStrategy(String logType) {
        switch (logType) {
            case "seed":
                return new SeedLogString();
            case "playerInitialisation":
                return new PlayerInitialisationLogString();
            case "deal":
                return new DealLogString();
            case "discard":
                return new DiscardlLogString();
            case "starter":
                return new StarterLogString();
            case "play":
                return new PlayLogString();
            case "show":
                return new ShowLogString();
            default:
                return new ScoreLogString();
        }
    }
}
