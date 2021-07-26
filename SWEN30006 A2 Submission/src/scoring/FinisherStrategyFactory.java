package scoring;

public class FinisherStrategyFactory {
    private static FinisherStrategyFactory instance = null;
    public static FinisherStrategy finisherStrategy;

    private FinisherStrategyFactory() {

    }

    public static FinisherStrategyFactory getInstance() {
        if (instance == null) {
            instance = new FinisherStrategyFactory();
        }
        return instance;
    }

    public FinisherStrategy getFinisherStrategy(String scoringType) {
        switch (scoringType) {
            case "go":
                return new GoFinisherStrategy();
            default:
                return new ConcreteFinisherStrategyComposite();
        }
    }

}