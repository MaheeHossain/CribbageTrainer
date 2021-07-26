package cribbage;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

public class Score {
    public String phase;
    public String scoreEvent;
    public int score;
    public ArrayList<Card> cards;

    public Score(String phase, String scoreEvent, int score, ArrayList<Card> cards) {
        this.phase = phase;
        this.scoreEvent = scoreEvent;
        this.score = score;
        this.cards = cards;
    }

    public String toString() {
        return phase + "," + score + "," + scoreEvent + "," + printCards(cards);
    }

    /* Prints out an array of cards as a string (used for testing purposes) */
    public static String printCards(ArrayList<Card> cards) {
        StringBuilder cardList= new StringBuilder("[");
        for (Card c: cards) {
            if (cardList.toString().equals("[")) {
                cardList.append(Cribbage.canonical((Cribbage.Rank)c.getRank())).
                        append(Cribbage.canonical((Cribbage.Suit)c.getSuit()));
            }
            else {
                cardList.append(",").append(Cribbage.canonical((Cribbage.Rank)c.getRank())).
                        append(Cribbage.canonical((Cribbage.Suit)c.getSuit()));
            }
        }
        return cardList+"]";
    }
}
