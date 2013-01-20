package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import smack.down.*;
import smack.down.robots.*;

public class RobotsTest {

	@Test
	public void testTechCenter() {
		Player player1 = new Player("TestPlayer1", new CardList(
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1),
			new Minion("DeckMinion1", Faction.Aliens, 1)
		), new CardSet(), new CardSet());
		for (DeckCard card : player1.getDrawPile())
			card.setOwner(player1);
		Player player2 = new Player("TestPlayer2", new CardList(), new CardSet(), new CardSet());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		
		Minion m1 = new Minion("TestMinion1", Faction.Dinosaurs, 1).setOwner(player1);
		Minion m2 = new Minion("TestMinion2", Faction.Dinosaurs, 1).setOwner(player1);
		Minion m3 = new Minion("TestMinion3", Faction.Ninjas, 1).setOwner(player1);
		Minion m4 = new Minion("TestMinion4", Faction.Pirates, 1).setOwner(player1);
		Minion m5 = new Minion("TestMinion5", Faction.Tricksters, 1).setOwner(player1);
		Minion m6 = new Minion("TestMinion6", Faction.Tricksters, 1).setOwner(player1);
		Minion m7 = new Minion("TestMinion7", Faction.Wizards, 1).setOwner(player2);
		Minion m8 = new Minion("TestMinion8", Faction.Wizards, 1).setOwner(player2);
		Minion m9 = new Minion("TestMinion9", Faction.Zombies, 1).setOwner(player2);
		base1.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9);
		m1.setBase(base1);
		m2.setBase(base1);
		m3.setBase(base1);
		m4.setBase(base1);
		m5.setBase(base1);
		m6.setBase(base1);
		m7.setBase(base1);
		m8.setBase(base1);
		m9.setBase(base1);
		
		assertEquals(6, base1.getTotalStrength(player1));
		assertEquals(3, base1.getTotalStrength(player2));
		assertEquals(9, base1.getMinions().size());
		assertEquals(0, player1.getHand().size());
		assertEquals(8, player1.getDrawPile().size());
		
		Action action = (Action) new TechCenter().setOwner(player1);
		action.play(base1, new Callback(){});
		
		assertEquals(6, player1.getHand().size());
		assertEquals(2, player1.getDrawPile().size());
	}

}
