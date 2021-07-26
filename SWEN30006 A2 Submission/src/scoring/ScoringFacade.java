package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage.Segment;
import cribbage.Score;
import java.util.ArrayList;

public class ScoringFacade {
    Deck deck; // Need this since can't get from hand to deck
    Hand hand;
    Card starter;
    Segment segment;
    public static ScoringFacade instance = null;
    StarterStrategy compositeStarter = StarterStrategyFactory.getInstance().getStarterStrategy("composite");
    PlayScoringRulesStrategy compositePlay = PlayScoringRulesStrategyFactory.getInstance().getPlayScoringRulesStrategy("composite");
    ShowScoringRulesStrategy compositeShow = ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("composite");
    FinisherStrategy compositeFinisher = FinisherStrategyFactory.getInstance().getFinisherStrategy("composite");


    private ScoringFacade(Deck deck, Hand hand, Card starter, Segment segment) {
        this.deck = deck;
        this.hand = hand;
        this.starter = starter;
        this.segment = segment;
    }

    public static ScoringFacade getInstance(Deck deck, Hand hand, Card starter, Segment segment) {
        if (instance == null) {
            instance = new ScoringFacade(deck, hand, starter, segment);
        }
        return instance;
    }

    public void addStarter() {
        ((StarterComposite) compositeStarter).addStarterScoringRulesStrategies(new JackStarterStrategy());
    }

    public ArrayList<Score> scoreStarter() { return compositeStarter.score(starter); }

    public void addPlay() {
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(new PlayAddToFifteenStrategy());
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(new AddToThirtyOneStrategy());
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(new PlayRunsStrategy());
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(new PlayPairsStrategy());
    }

    public ArrayList<Score> scorePlay() {
        return compositePlay.score(segment, deck);
    }

    public void addShow() {
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(new ShowAddToFifteenStrategy());
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(new ShowRunsStrategy());
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(new ShowPairsStrategy());
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(new ShowFlushStrategy());
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(new ShowKnobStrategy());
    }

    public ArrayList<Score> scoreShow() {
        return compositeShow.score(hand, starter, deck);
    }

    public void addFinisher() {
        ((FinisherComposite) compositeFinisher).addFinisherScoringRulesStrategies(new GoFinisherStrategy());
    }

    public ArrayList<Score> scoreFinisher() { return compositeFinisher.score(segment);}

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setStarter(Card starter) {
        this.starter = starter;
    }
}
