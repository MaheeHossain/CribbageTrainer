package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.aplu.jcardgame.Hand;
import logging.CribbageLog;
import scoring.*;

public class Cribbage extends CardGame {
	static Cribbage cribbage;  // Provide access to singleton

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	public enum Rank {
		// Order of cards is tied to card images
		ACE(1,1), KING(13,10), QUEEN(12,10), JACK(11,10), TEN(10,10), NINE(9,9), EIGHT(8,8), SEVEN(7,7), SIX(6,6), FIVE(5,5), FOUR(4,4), THREE(3,3), TWO(2,2);
		public final int order;
		public final int value;
		Rank(int order, int value) {
			this.order = order;
			this.value = value;
		}
	}

	public static int cardValue(Card c) { return ((Cribbage.Rank) c.getRank()).value; }

	public static int cardOrder(Card c) { return ((Cribbage.Rank) c.getRank()).order; }

	/*
	Canonical String representations of Suit, Rank, Card, and Hand
	*/
	// C, D, H, S
	public static String canonical(Suit s) { return s.toString().substring(0, 1); }

	// A, K, Q, J, T, 9. 8, 7, 6, 5, 4, 3, 2, 1
	public static String canonical(Rank r) {
		switch (r) {
			case ACE:case KING:case QUEEN:case JACK:case TEN:
				return r.toString().substring(0, 1);
			default:
				return String.valueOf(r.value);
		}
	}

	// 5C, 3H, TS, JD
    public String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }

    public String canonical(Hand h) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C: h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }

	class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
		public int[] values(Enum suit) {  // Returns the value for each card in the suit
			return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order-1)*(Suit.values().length)+suit.ordinal()).toArray();
		}
	}

	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }

	static boolean ANIMATE;

	public void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}
  }
  
  private void dealingOut(Hand pack, Hand[] hands) {
	  for (int i = 0; i < nStartCards; i++) {
		  for (int j=0; j < nPlayers; j++) {
			  Card dealt = randomCard(pack);
			  dealt.setVerso(false);  // Show the face
			  transfer(dealt, hands[j]);
		  }
	  }
  }

	static int SEED;

	public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }

  private final String version = "0.1";
  static public final int nPlayers = 2;
  public final int nStartCards = 6;
  public final int nDiscards = 2;
  private final int handWidth = 400;
  private final int cribWidth = 150;
  private final int segmentWidth = 180;
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
  private final Location[] handLocations = {
			  new Location(360, 75),
			  new Location(360, 625)
	  };
  private final Location[] scoreLocations = {
			  new Location(590, 25),
			  new Location(590, 675)
	  };
  private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350),
			new Location(400, 350),
			new Location(650, 350)
	};
  private final Location starterLocation = new Location(50, 625);
  private final Location cribLocation = new Location(700, 625);
  private final Location seedLocation = new Location(5, 25);
  // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
  private final Actor[] scoreActors = {null, null}; //, null, null };
  private final Location textLocation = new Location(350, 450);
  private final Hand[] hands = new Hand[nPlayers];
  private Hand starter;
  private Hand crib;

  private ScoringFacade scoringFacade;

  private Hand player0Discard;
  private Hand player1Discard;
  private Hand player0;
  private Hand player1;

  private static CribbageObserverBase cribbageObserverBase = new CribbageLog();

  public static void setStatus(String string) { cribbage.setStatusText(string); }

static private final IPlayer[] players = new IPlayer[nPlayers];
private final int[] scores = new int[nPlayers];

final Font normalFont = new Font("Serif", Font.BOLD, 24);
final Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nPlayers; i++) {
		 scores[i] = 0;
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

private void updateScore(int player) {
	removeActor(scoreActors[player]);
	scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void deal(Hand pack, Hand[] hands) {
	for (int i = 0; i < nPlayers; i++) {
		hands[i] = new Hand(deck);
		// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
		players[i].setId(i);
		cribbageObserverBase.update("playerInitialisation", "P"+i, null,null,null, players[i].getClass().getName(),null, null, null, null);
		players[i].startSegment(deck, hands[i]);
	}
	RowLayout[] layouts = new RowLayout[nPlayers];
	for (int i = 0; i < nPlayers; i++)
	{
		layouts[i] = new RowLayout(handLocations[i], handWidth);
		layouts[i].setRotationAngle(0);
		// layouts[i].setStepDelay(10);
		hands[i].setView(this, layouts[i]);
		hands[i].draw();
	}
	layouts[0].setStepDelay(0);

	dealingOut(pack, hands);
	for (int i = 0; i < nPlayers; i++) {
		hands[i].sort(Hand.SortType.POINTPRIORITY, true);
		/** c) Dealing hands to players */
		// pw.printf("deal,P%d,%s\n", i, canonical(hands[i]));
		cribbageObserverBase.update("deal", "P"+i, canonical(hands[i]), null, null,null,null,null,null,null);
	}
	layouts[0].setStepDelay(0);
}

private void discardToCrib() {
	crib = new Hand(deck);
	RowLayout layout = new RowLayout(cribLocation, cribWidth);
	layout.setRotationAngle(0);
	crib.setView(this, layout);
	// crib.setTargetArea(cribTarget);
	crib.draw();
	Hand currHand = new Hand(deck);
	for (IPlayer player: players) {
		for (int i = 0; i < nDiscards; i++) {
			Card discardedCard = player.discard();
			currHand.insert(discardedCard.getCardNumber(), false);
			transfer(discardedCard, crib);
		}
		/** d) Discarding of cards to crib */
		cribbageObserverBase.update("discard", "P"+player.id, canonical(currHand), null, null, null, null, null, null, null);
		crib.sort(Hand.SortType.POINTPRIORITY, true);
		// System.out.println(canonical(crib));
		currHand = new Hand(deck);
	}
}

private void starter(Hand pack) {
	starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
	RowLayout layout = new RowLayout(starterLocation, 0);
	layout.setRotationAngle(0);
	starter.setView(this, layout);
	starter.draw();
	Card dealt = randomCard(pack);
	scoringFacade.setStarter(dealt);
	/** e) Starter Card selected */
	cribbageObserverBase.update("starter", null, null,null, canonical(dealt), null, null,null, null, null);
	scoringFacade.addStarter();
	ArrayList<Card> starters = new ArrayList<>();
	starters.add(dealt);
	for (Score score: scoringFacade.scoreStarter()) {
		scores[1] += score.score;
		updateScore(1);
		cribbageObserverBase.update("score", "P"+1, Score.printCards(starters), score.scoreEvent,null, null,null,null,Integer.toString(scores[1]), Integer.toString(score.score));
	}
	dealt.setVerso(false);
	transfer(dealt, starter);
}

int total(Hand hand) {
	int total = 0;
	for (Card c: hand.getCardList()) total += cardValue(c);
	return total;
}

public class Segment {
		public Hand segment;
		boolean go;
		int lastPlayer;
		boolean newSegment;

		void reset(final List<Hand> segments) {
			segment = new Hand(deck);
			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
			segment.draw();
			go = false;        // No-one has said "go" yet
			lastPlayer = -1;   // No-one has played a card yet in this segment
			newSegment = false;  // Not ready for new segment yet
		}

		public boolean isGo() {
			return go;
		}
}

private void play() {
	final int thirtyone = 31;
	List<Hand> segments = new ArrayList<>();
	int currentPlayer = 0; // Player 1 is dealer
	Segment s = new Segment();
	s.reset(segments);
//	scoringFacade = ScoringFacade.getInstance(deck, player0, starter.get(0), s);
//	scoringFacade.addPlay();
	while (!(players[0].emptyHand() && players[1].emptyHand())) {
		// System.out.println("segments.size() = " + segments.size());
		// System.out.println(s.go + "254");
		Card nextCard = players[currentPlayer].lay(thirtyone-total(s.segment));
//		System.out.println(canonical(nextCard));
//		System.out.print("line 271: ");
//		scoringFacade.setSegment(s);
//		System.out.println(scoringFacade.scorePlay());
		scoringFacade.setSegment(s);
		if (nextCard == null) {
			if (s.go) {
				// Another "go" after previous one with no intervening cards
				// lastPlayer gets 1 point for a "go"
				for (Score score: scoringFacade.scoreFinisher()) {
					scores[currentPlayer] += score.score;
					updateScore(currentPlayer);
					cribbageObserverBase.update("score", "P"+currentPlayer, null, score.scoreEvent,null, null,null,null,Integer.toString(scores[currentPlayer]),Integer.toString(score.score));
				}
				/** g) Scoring during the play - go 1 point */
				s.newSegment = true;
			} else {
				// currentPlayer says "go"
				s.go = true;
			}
			currentPlayer = (currentPlayer+1) % 2;
		} else {
			/** f) Playing of the cards */
			s.lastPlayer = currentPlayer; // last Player to play a card in this segment
			transfer(nextCard, s.segment);
			// pw.printf("play,P%d,%d,%s\n", currentPlayer, total(s.segment), canonical(nextCard));
			cribbageObserverBase.update("play", "P"+currentPlayer, null, null, canonical(nextCard), null, null, Integer.toString(total(s.segment)), null, null);

			for (Score score: scoringFacade.scorePlay()) {
				scores[currentPlayer] += score.score;
				updateScore(currentPlayer);
				cribbageObserverBase.update("score", "P"+currentPlayer, null, score.scoreEvent,null, null,null,null,Integer.toString(scores[currentPlayer]),Integer.toString(score.score));
			}

//			System.out.println("Printing pair strategy");
//			PlayScoringRulesStrategy pairStrategy = new PlayPairsStrategy();
//			System.out.println(pairStrategy.score(s, deck));
//			System.out.println("Printing run strategy");
// 			PlayScoringRulesStrategy runStrategy = new PlayRunsStrategy();
//			System.out.println(runStrategy.score(s, deck));
			if (total(s.segment) == thirtyone) {
				// lastPlayer gets 2 points for a 31
				/** g) Scoring during the play - thirty-one 2 points */
				s.newSegment = true;
				currentPlayer = (currentPlayer+1) % 2;
//				System.out.println("Printing thirty one strategy");
//				PlayScoringRulesStrategy addToThirtyOneStrategy = new AddToThirtyOneStrategy();
//				System.out.println(addToThirtyOneStrategy.score(s));
			} else {
				/** g) Scoring during the play - 15, run or pairs */
				// if total(segment) == 15, lastPlayer gets 2 points for a 15
				if (!s.go) { // if it is "go" then same player gets another turn
					currentPlayer = (currentPlayer+1) % 2;
				}
				// System.out.println(canonical(s.segment));
			}
		}
		if (s.newSegment) {
			segments.add(s.segment);
			s.reset(segments);
		}
	}
	scoringFacade.addFinisher();
	for (Score score: scoringFacade.scoreFinisher()) {
		scores[(currentPlayer+1) % 2] += score.score;
		updateScore((currentPlayer+1) % 2);
		cribbageObserverBase.update("score", "P"+((currentPlayer+1) % 2), null, score.scoreEvent,null, null,null,null,Integer.toString(scores[((currentPlayer+1) % 2)]),Integer.toString(score.score));
	}
	// Score lastToPlay = new Score("play", "go", 1, null);
//	scores[((currentPlayer+1) % 2)] += lastToPlay.score;
//	updateScore(((currentPlayer+1) % 2));
//	cribbageObserverBase.update("score", "P"+((currentPlayer+1) % 2), null, lastToPlay.scoreEvent,null, null,null,null,Integer.toString(scores[((currentPlayer+1) % 2)]),Integer.toString(lastToPlay.score));
}

void showHandsCrib() {
	// score player 0 (non dealer)
	/** h) Showing of the hands and crib */
	/** i) Scoring during the show */
	cribbageObserverBase.update("show","P"+0, canonical(player0), null, canonical(starter.get(0)), null, null, null, null, null);
	scoringFacade.addShow();
	for (Score score: scoringFacade.scoreShow()) {
		scores[0] += score.score;
		updateScore(0);
		cribbageObserverBase.update("score", "P"+0, Score.printCards(score.cards), score.scoreEvent,null, null,null,null,Integer.toString(scores[0]),Integer.toString(score.score));
	}
	// score player 1 (dealer)
	/** h) Showing of the hands and crib */
	/** i) Scoring during the show */
	scoringFacade.setHand(player1);
	cribbageObserverBase.update("show","P"+1, canonical(player1), null, canonical(starter.get(0)), null, null, null, null, null);
	for (Score score: scoringFacade.scoreShow()) {
		scores[1] += score.score;
		updateScore(1);
		cribbageObserverBase.update("score", "P"+1, Score.printCards(score.cards), score.scoreEvent,null, null,null,null,Integer.toString(scores[1]),Integer.toString(score.score));
	}
	// score crib (for dealer)
	scoringFacade.setHand(crib);
	cribbageObserverBase.update("show","P"+1, canonical(crib), null, canonical(starter.get(0)), null, null, null, null, null);
	for (Score score: scoringFacade.scoreShow()) {
		scores[1] += score.score;
		updateScore(1);
		cribbageObserverBase.update("score", "P"+1, Score.printCards(score.cards), score.scoreEvent,null, null,null,null,Integer.toString(scores[1]),Integer.toString(score.score));
	}
	/** h) Showing of the hands and crib */
	/** i) Scoring during the show */
}

  public Cribbage() {
    super(850, 700, 30);
    cribbage = this;
    setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initScore();

	  Hand pack = deck.toHand(false);
	  RowLayout layout = new RowLayout(starterLocation, 0);
	  layout.setRotationAngle(0);
	  pack.setView(this, layout);
	  pack.setVerso(true);
	  pack.draw();
	  addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

	  /* Play the round */
	  deal(pack, hands);
	  discardToCrib();
	  player0 = new Hand(deck);
	  player1 = new Hand(deck);
	  for (Card c: players[0].hand.getCardList()) {
	  	player0.insert(c.getCardNumber(), false);
	  }
	  for (Card c: players[1].hand.getCardList()) {
	  	player1.insert(c.getCardNumber(), false);
	  }

	  scoringFacade = ScoringFacade.getInstance(deck, player0, null, null);
	  scoringFacade.addPlay();

	  // System.out.println(canonical(players[0].hand));
	  starter(pack);
	  play();
	  showHandsCrib();

    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over.");
    refresh();
  }

  public static void main(String[] args)
		  throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
		  	InstantiationException, IllegalAccessException {
	  /* Handle Properties */
	  // System.out.println("Working Directory = " + System.getProperty("user.dir"));
	  Properties cribbageProperties = new Properties();
	  // Default properties
	  cribbageProperties.setProperty("Animate", "true");
	  cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
	  cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

	  // Read properties
	  try (FileReader inStream = new FileReader("cribbage.properties")) {
		  cribbageProperties.load(inStream);
	  }
      // add
	  // Control Graphics
	  ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

	  // Control Randomisation
	  /* Read the first argument and save it as a seed if it exists */
	  if (args.length > 0 ) { // Use arg seed - overrides property
		  SEED = Integer.parseInt(args[0]);
		  /** a) Initialisation of seed */
	  } else { // No arg
		  String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
		  if (seedProp != null) { // Use property seed
			  SEED = Integer.parseInt(seedProp);
			  /** a) Initialisation of seed */
		  } else { // and no property
			  SEED = new Random().nextInt(); // so randomise
			  /** a) Initialisation of seed */
		  }
	  }
	  random = new Random(SEED);

	  cribbageObserverBase.update("seed", null, null,null, null, null, Integer.toString(SEED),null, null, null);

	  // Control Player Types
	  Class<?> clazz;
	  clazz = Class.forName(cribbageProperties.getProperty("Player0"));
	  players[0] = (IPlayer) clazz.getConstructor().newInstance();
	  /** b) Initialisation of players 0 */
	  clazz = Class.forName(cribbageProperties.getProperty("Player1"));
	  players[1] = (IPlayer) clazz.getConstructor().newInstance();
	  /** b) Initialisation of players 1 */
	  // End properties

	  new Cribbage();

  }

}
