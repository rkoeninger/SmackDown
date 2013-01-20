package smack.down;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A mutually-exclusive set of DeckCards. If a card is added to this set,
 * its container reference will point to this CardSet and it will be
 * removed from its former containing CardSet.
 */
public class CardSet implements Iterable<DeckCard> {
	private Set<DeckCard> cards;
	
	public CardSet() {
		this.cards = new HashSet<DeckCard>();
	}
	
	/**
	 * Initializes with an array of cards.
	 * DeckCards become associated with this CardSet.
	 * @param cards
	 */
	public CardSet(DeckCard... cards) {
		this.cards = new HashSet<DeckCard>();
		
		for (DeckCard card : cards)
			add(card);
	}
	
	public boolean add(DeckCard card) {
		boolean didAdd = cards.add(card);
		
		if (card.getContainer() != this)
			card.setContainer(this);
		
		return didAdd;
	}
	
	public boolean remove(DeckCard card) {
		boolean didRemove = cards.remove(card);
		
		if (card.getContainer() == this)
			card.setContainer(null);
		
		return didRemove;
	}
	
	public boolean contains(DeckCard card) {
		return cards.contains(card);
	}
	
	public int size() {
		return cards.size();
	}
	
	public Iterator<DeckCard> iterator() {
		return cards.iterator();
	}
}
