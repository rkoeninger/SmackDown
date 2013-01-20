package smack.down.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;
import smack.down.*;

public class CardSetTest {

	@Test
	public void testCardSetAndCardList() {
		DeckCard[] cards = TestCard.getTestCards(8);
		
		for (DeckCard card : cards)
			assertEquals(null, card.getContainer());
		
		CardSet set1 = new CardSet(cards);
		assertEquals(8, set1.size());
		
		for (DeckCard card : cards) {
			assertEquals(set1, card.getContainer());
			assertTrue(set1.contains(card));
		}
		
		CardSet set2 = new CardSet();
		
		set2.add(cards[0]);
		assertEquals(set2, cards[0].getContainer());
		assertTrue(set2.contains(cards[0]));
		assertFalse(set1.contains(cards[0]));
		assertEquals(7, set1.size());
		assertEquals(1, set2.size());
		
		cards[5].setContainer(set2);
		assertEquals(set2, cards[5].getContainer());
		assertTrue(set2.contains(cards[5]));
		assertFalse(set1.contains(cards[5]));
		assertEquals(6, set1.size());
		assertEquals(2, set2.size());
		
		set1.remove(cards[3]);
		assertEquals(null, cards[3].getContainer());
		assertFalse(set1.contains(cards[3]));
		assertEquals(5, set1.size());
		
		cards[0].setContainer(null);
		assertEquals(null, cards[0].getContainer());
		assertFalse(set2.contains(cards[0]));
		assertEquals(1, set2.size());
		
		CardSet set3 = new CardSet(cards[0], cards[5], cards[6], cards[7]);
		assertEquals(set3, cards[0].getContainer());
		assertEquals(set3, cards[5].getContainer());
		assertEquals(set3, cards[6].getContainer());
		assertEquals(set3, cards[7].getContainer());
		assertEquals(4, set3.size());
		assertEquals(0, set2.size());
		assertEquals(set1, cards[1].getContainer());
		assertEquals(set1, cards[2].getContainer());
		assertEquals(set1, cards[4].getContainer());
		assertEquals(3, set1.size());
		assertEquals(null, cards[3].getContainer());
		
		CardList list = new CardList(cards[1], cards[2], cards[3]);
		Iterator<DeckCard> cardItr = list.iterator();
		assertEquals(3, list.size());
		assertEquals(cards[1], cardItr.next());
		assertEquals(cards[2], cardItr.next());
		assertEquals(cards[3], cardItr.next());
		assertFalse(cardItr.hasNext());
		
		list.add(cards[1]);
		cardItr = list.iterator();
		assertEquals(3, list.size());
		assertEquals(cards[1], cardItr.next());
		assertEquals(cards[2], cardItr.next());
		assertEquals(cards[3], cardItr.next());
		assertFalse(cardItr.hasNext());
		
		list.add(cards[4]);
		cardItr = list.iterator();
		assertEquals(4, list.size());
		assertEquals(cards[1], cardItr.next());
		assertEquals(cards[2], cardItr.next());
		assertEquals(cards[3], cardItr.next());
		assertEquals(cards[4], cardItr.next());
		assertFalse(cardItr.hasNext());

		assertEquals(list.pop(), cards[1]);
		assertEquals(3, list.size());
		cardItr = list.iterator();
		assertEquals(cards[2], cardItr.next());
		assertEquals(cards[3], cardItr.next());
		assertEquals(cards[4], cardItr.next());
		assertFalse(cardItr.hasNext());
		
		CardList list2 = new CardList();
		list2.takeAndShuffle(set3);
		assertEquals(0, set3.size());
		assertEquals(4, list2.size());
		
		cardItr = list2.iterator();
		DeckCard[] orderedCards = new DeckCard[4];
		orderedCards[0] = cardItr.next();
		orderedCards[1] = cardItr.next();
		orderedCards[2] = cardItr.next();
		orderedCards[3] = cardItr.next();
		assertFalse(cardItr.hasNext());
		assertEquals(list2.peek(), orderedCards[0]);
		
		DeckCard[] peek2 = list2.peek(2);
		assertEquals(2, peek2.length);
		assertEquals(orderedCards[0], peek2[0]);
		assertEquals(orderedCards[1], peek2[1]);
		
		DeckCard[] peek4 = list2.peek(4);
		assertEquals(4, peek4.length);
		assertEquals(orderedCards[0], peek4[0]);
		assertEquals(orderedCards[1], peek4[1]);
		assertEquals(orderedCards[2], peek4[2]);
		assertEquals(orderedCards[3], peek4[3]);
	}
}
