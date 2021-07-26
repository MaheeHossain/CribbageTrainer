package scoring;

public class ShowScoringRulesStrategyFactory {
    private static ShowScoringRulesStrategyFactory instance = null;
    public static ShowScoringRulesStrategy showScoringRulesStrategy;

    private ShowScoringRulesStrategyFactory() {

    }

    public static ShowScoringRulesStrategyFactory getInstance() {
        if (instance == null) {
            instance = new ShowScoringRulesStrategyFactory();
        }
        return instance;
    }

    /* Instantiation for all the show strategies */
    public ShowScoringRulesStrategy getShowScoringRulesStrategy(String scoringType) {
        switch (scoringType) {
            case "flush":
                return new ShowFlushStrategy();
            case "knob":
                return new ShowKnobStrategy();
            case "fifteen":
                return new ShowAddToFifteenStrategy();
            case "runs":
                return new ShowRunsStrategy();
            case "pairs":
                return new ShowPairsStrategy();
            default:
                return new ConcreteShowRulesStrategyComposite();
        }
    }

}
