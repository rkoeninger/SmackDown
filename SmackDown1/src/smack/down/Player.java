package smack.down;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private int points;
	private Deck deck;
	private List<DeckCard> hand;
	
	public Player(String name, Deck deck, List<DeckCard> hand) {
		this.name = name;
		this.points = 0;
		this.deck = deck;
		this.hand = new ArrayList<DeckCard>(hand);
	}
	
	public String getName() {
		return name;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int addPoints(int amount) {
		points += amount;
		return points;
	}
	
	public DeckCard draw() {
		DeckCard card = deck.draw();
		hand.add(card);
		return card;
	}
	
	public List<DeckCard> draw(int amount) {
		List<DeckCard> cards = deck.draw(amount);
		hand.addAll(cards);
		return cards;
	}
	
	public List<DeckCard> getDrawPile() {
		return deck.getDrawPile();
	}
	
	public List<DeckCard> getDiscardPile() {
		return deck.getDiscardPile();
	}
	
	public List<DeckCard> getHand() {
		return new ArrayList<DeckCard>(hand);
	}
	
	public void addToDiscard(DeckCard... cards) {
		for (DeckCard card : cards)
			deck.addToDiscard(card);
	}
}
