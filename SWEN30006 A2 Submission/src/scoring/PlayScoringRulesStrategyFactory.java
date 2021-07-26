package scoring;

public class PlayScoringRulesStrategyFactory {
    private static PlayScoringRulesStrategyFactory instance = null;

    private PlayScoringRulesStrategyFactory() {

    }

    public static PlayScoringRulesStrategyFactory getInstance() {
        if (instance == null) {
            instance = new PlayScoringRulesStrategyFactory();
        }
        return instance;
    }

    public PlayScoringRulesStrategy getPlayScoringRulesStrategy(String scoringType) {
        switch (scoringType) {
            case "fifteen":
                return new PlayAddToFifteenStrategy();
            case "thirtyone":
                return new AddToThirtyOneStrategy();
            case "runs":
                return new PlayRunsStrategy();
            case "pairs":
                return new PlayPairsStrategy();
            default:
                return new ConcretePlayRulesStrategyComposite();
        }
    }

}
