package smack.down;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<DeckCard> drawPile;
	private List<DeckCard> discardPile;
	
	public Deck(List<DeckCard> drawPile) {
		this.drawPile = new ArrayList<DeckCard>(drawPile);
		this.discardPile = new ArrayList<DeckCard>(0);
	}
	
	public List<DeckCard> getDrawPile() {
		return new ArrayList<DeckCard>(drawPile);
	}
	
	public List<DeckCard> getDiscardPile() {
		return new ArrayList<DeckCard>(discardPile);
	}
	
	public DeckCard draw() {
		if (drawPile.size() == 0) {
			if (discardPile.size() == 0)
				return null;
			Collections.shuffle(discardPile);
			drawPile.addAll(discardPile);
			discardPile.clear();
		}
		return drawPile.remove(drawPile.size() - 1);
	}
	
	public List<DeckCard> draw(int amount) {
		List<DeckCard> cards = new ArrayList<DeckCard>(amount);
		
		while (amount > 0) {
			DeckCard drawnCard = draw();
			
			if (drawnCard == null)
				break;
			
			cards.add(drawnCard);
			amount--;
		}
		
		return cards;
	}
	
	public void addToDiscard(DeckCard card) {
		discardPile.add(card);
	}
}
