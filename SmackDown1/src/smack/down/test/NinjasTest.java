package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import smack.down.*;
import smack.down.ninjas.*;

public class NinjasTest {

	@Test
	public void testNinjaMaster() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		
		final Minion m1 = (Minion) new Minion("TestMinion1", Faction.Dinosaurs, 50).setOwner(player1);
		base1.addMinion(m1);
		m1.setBase(base1);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(0, player1.getDiscardPile().size());
		
		Minion m2 = (Minion) new NinjaMaster().setOwner(player2);
		m2.play(base1, new Callback() {
			public Minion selectMinionInPlay(String message, boolean optional, Predicate<Minion> pred) {
				return m1;
			}
		});
		
		assertEquals(0, base1.getMinions().size());
		assertEquals(1, player1.getDiscardPile().size());
		
	}
}
