package smack.down.test;

import smack.down.DeckCard;
import smack.down.Faction;

public class TestCard extends DeckCard {
	public static DeckCard[] getTestCards(int count) {
		DeckCard[] cards = new DeckCard[count];
		for (int i = 0; i < count; ++i)
			cards[i] = new TestCard(i);
		return cards;
	}
	public TestCard(int num) {
		super("Card" + num, Faction.Aliens);
	}
	public String toString() {
		return getName();
	}
}
