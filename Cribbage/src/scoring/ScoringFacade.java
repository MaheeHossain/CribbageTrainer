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

    /* Initialise all of the composites */
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

    /* Add all the starter strategies used */
    public void addStarter() {
        ((StarterComposite) compositeStarter).addStarterScoringRulesStrategies(StarterStrategyFactory.getInstance().getStarterStrategy("jack"));
    }

    /* Get the score for all the starter strategies */
    public ArrayList<Score> scoreStarter() { return compositeStarter.score(starter); }

    /* Add all the play strategies used */
    public void addPlay() {
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(PlayScoringRulesStrategyFactory.getInstance().getPlayScoringRulesStrategy("fifteen"));
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(PlayScoringRulesStrategyFactory.getInstance().getPlayScoringRulesStrategy("thirtyone"));
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(PlayScoringRulesStrategyFactory.getInstance().getPlayScoringRulesStrategy("runs"));
        ((PlayCompositeStrategy) compositePlay).addPlayScoringRulesStrategies(PlayScoringRulesStrategyFactory.getInstance().getPlayScoringRulesStrategy("pairs"));
    }

    /* Get the score for all the play strategies */
    public ArrayList<Score> scorePlay() {
        return compositePlay.score(segment, deck);
    }

    /* Add all the show strategies used */
    public void addShow() {
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("fifteen"));
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("runs"));
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("pairs"));
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("flush"));
        ((ShowCompositeStrategy) compositeShow).addShowScoringRulesStrategies(ShowScoringRulesStrategyFactory.getInstance().getShowScoringRulesStrategy("knob"));
    }

    /* Get the score for all the show strategies */
    public ArrayList<Score> scoreShow() {
        return compositeShow.score(hand, starter, deck);
    }

    /* Add all the finisher strategies used */
    public void addFinisher() {
        ((FinisherComposite) compositeFinisher).addFinisherScoringRulesStrategies(FinisherStrategyFactory.getInstance().getFinisherStrategy("go"));
    }

    /* Get the score for all the finisher strategies */
    public ArrayList<Score> scoreFinisher() { return compositeFinisher.score(segment);}

    /* Setters for the facade to be used in Cribbage */
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
