package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import smack.down.*;
import smack.down.tricksters.TakeTheShinies;

public class TrickstersTest {
	
	@Test
	public void testTakeTheShinies() {
		DeckCard[] cards = TestCard.getTestCards(17);
		Player player1 = new Player("1",
			new CardList(cards[0], cards[1]),
			new CardSet(cards[6]),
			new CardSet(cards[2], cards[3], cards[5]));
		Player player2 = new Player("2",
			new CardList(),
			new CardSet(),
			new CardSet());
		Player player3 = new Player("3",
			new CardList(cards[7], cards[8], cards[9]),
			new CardSet(cards[10], cards[11]),
			new CardSet(cards[12], cards[13], cards[14], cards[15], cards[16]));
		new Table(Arrays.asList(player1, player2, player3), new ArrayList<Base>(), new ArrayList<Base>());
		Action action = new TakeTheShinies();
		action.setOwner(player2);
		
		assertEquals(3, player1.getHand().size());
		assertEquals(1, player1.getDiscardPile().size());
		assertEquals(5, player3.getHand().size());
		assertEquals(2, player3.getDiscardPile().size());
		
		action.play(new Callback(){});

		assertEquals(1, player1.getHand().size());
		assertEquals(3, player1.getDiscardPile().size());
		assertEquals(3, player3.getHand().size());
		assertEquals(4, player3.getDiscardPile().size());
	}
	
}
