package scoring;

public class StarterStrategyFactory {
    private static StarterStrategyFactory instance = null;
    public static StarterStrategy starterStrategy;

    private StarterStrategyFactory() {

    }

    public static StarterStrategyFactory getInstance() {
        if (instance == null) {
            instance = new StarterStrategyFactory();
        }
        return instance;
    }

    /* Instantiation for all the starter strategies */
    public StarterStrategy getStarterStrategy(String scoringType) {
        switch (scoringType) {
            case "jack":
                return new JackStarterStrategy();
            default:
                return new ConcreteStarterCompositeStrategy();
        }
    }

}
