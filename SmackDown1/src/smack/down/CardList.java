package smack.down;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An insertion-order preserving CardSet.
 */
public class CardList extends CardSet {
	private List<DeckCard> cards;
	
	public CardList() {
		this.cards = new ArrayList<DeckCard>();
	}
	
	/**
	 * Initializes with an array of cards.
	 * First card in array is first/top card in list.
	 * DeckCards become associated with this CardList.
	 * @param cards
	 */
	public CardList(DeckCard... cards) {
		this.cards = new ArrayList<DeckCard>();
		
		for (DeckCard card : cards)
			add(card);
	}
	
	/**
	 * Adds card to the end/bottom of the list.
	 */
	@Override
	public boolean add(DeckCard card) {
		if (super.add(card)) {
			cards.add(card);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean remove(DeckCard card) {
		if (super.remove(card)) {
			cards.remove(card);
			return true;
		}
		
		return false;
	}
	
	public DeckCard peek() {
		return cards.size() == 0 ? null : cards.get(0);
	}
	
	public DeckCard[] peek(int count) {
		count = min(count, size());
		DeckCard[] results = new DeckCard[count];
		
		for (int i = 0; i < count; ++i)
			results[i] = cards.get(i);
		
		return results;
	}
	
	public DeckCard pop() {
		if (cards.size() == 0)
			return null;
		
		DeckCard result = cards.remove(0);
		super.remove(result);
		return result;
	}
	
	public void takeAndShuffle(CardSet set) {
		if (size() != 0)
			throw new RuntimeException("CardList must be empty to do takeAndShuffle()");
		
		while (set.size() > 0)
			add(set.iterator().next());
		
		Collections.shuffle(cards);
	}
	
	@Override
	public Iterator<DeckCard> iterator() {
		return cards.iterator();
	}
}
