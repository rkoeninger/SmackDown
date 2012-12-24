package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.Deck;
import smack.down.DeckCard;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.Player;
import smack.down.Table;
import smack.down.dinosaurs.*;

public class DinosuarsTest {

	@Test
	public void testAugmentation() {
		Player player1 = new Player("TestPlayer", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Minion m1 = new Minion("TestMinion", Faction.Aliens, 2);
		m1.setOwner(player1);
		Action action = new Augmentation();
		action.setOwner(player1);
		
		assertEquals(2, m1.getStrength());
		
		action.play(m1);
		
		assertEquals(6, m1.getStrength());
		
		player1.endTurn(new Callback(){});
		
		assertEquals(2, m1.getStrength());
	}
	
	@Test
	public void testHowl() {
		Player player1 = new Player("TestPlayer", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Minion m1 = new Minion("TestMinion1", Faction.Aliens, 2);
		m1.setOwner(player1);
		Minion m2 = new Minion("TestMinion2", Faction.Aliens, 5);
		m2.setOwner(player1);
		Minion m3 = new Minion("TestMinion3", Faction.Aliens, 7);
		m3.setOwner(player1);
		Minion m4 = new Minion("TestMinion4", Faction.Aliens, 3);
		m4.setOwner(player1);
		Minion m5 = new Minion("TestMinion5", Faction.Aliens, 6);
		m5.setOwner(player1);
		Base base1 = new Base("TestBase", Faction.Aliens, 50, 3, 2, 1);
		Base base2 = new Base("TestBase", Faction.Aliens, 40, 3, 2, 1);
		base1.addMinion(m1);
		base1.addMinion(m2);
		base1.addMinion(m5);
		base2.addMinion(m3);
		base2.addMinion(m4);
		Table table = new Table(Arrays.asList(player1), new ArrayList<Base>(), Arrays.asList(base1, base2));
		player1.setTable(table);
		Action action = new Howl();
		action.setOwner(player1);
		
		assertEquals(2, m1.getStrength());
		assertEquals(5, m2.getStrength());
		assertEquals(7, m3.getStrength());
		assertEquals(3, m4.getStrength());
		assertEquals(6, m5.getStrength());
		
		action.play();
		
		assertEquals(3, m1.getStrength());
		assertEquals(6, m2.getStrength());
		assertEquals(8, m3.getStrength());
		assertEquals(4, m4.getStrength());
		assertEquals(7, m5.getStrength());
		
		player1.endTurn(new Callback(){});
		
		assertEquals(2, m1.getStrength());
		assertEquals(5, m2.getStrength());
		assertEquals(7, m3.getStrength());
		assertEquals(3, m4.getStrength());
		assertEquals(6, m5.getStrength());
	}
	
	@Test
	public void testWarRaptor() {
		Player player1 = new Player("TestPlayer", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 50, 3, 2, 1);
		Minion m1 = (Minion) new WarRaptor().setOwner(player1);
		Minion m2 = (Minion) new WarRaptor().setOwner(player1);
		Minion m3 = (Minion) new WarRaptor().setOwner(player1);
		Minion m4 = (Minion) new WarRaptor().setOwner(player1);
		
		assertEquals(2, m1.getStrength());
		assertEquals(2, m2.getStrength());
		assertEquals(2, m3.getStrength());
		assertEquals(2, m4.getStrength());
		assertEquals(0, base1.getTotalStrength(player1));
		
		base1.addMinion(m1);
		m1.setBase(base1);
		
		assertEquals(3, m1.getStrength());
		assertEquals(2, m2.getStrength());
		assertEquals(2, m3.getStrength());
		assertEquals(2, m4.getStrength());
		assertEquals(3, base1.getTotalStrength(player1));
		
		base1.addMinion(m2);
		m2.setBase(base1);
		
		assertEquals(4, m1.getStrength());
		assertEquals(4, m2.getStrength());
		assertEquals(2, m3.getStrength());
		assertEquals(2, m4.getStrength());
		assertEquals(8, base1.getTotalStrength(player1));
		
		base1.addMinion(m3);
		m3.setBase(base1);
		
		assertEquals(5, m1.getStrength());
		assertEquals(5, m2.getStrength());
		assertEquals(5, m3.getStrength());
		assertEquals(2, m4.getStrength());
		assertEquals(15, base1.getTotalStrength(player1));
		
		base1.addMinion(m4);
		m4.setBase(base1);
		
		assertEquals(6, m1.getStrength());
		assertEquals(6, m2.getStrength());
		assertEquals(6, m3.getStrength());
		assertEquals(6, m4.getStrength());
		assertEquals(24, base1.getTotalStrength(player1));
		
		base1.removeMinion(m1);
		m1.setBase(null);
		
		assertEquals(2, m1.getStrength());
		assertEquals(5, m2.getStrength());
		assertEquals(5, m3.getStrength());
		assertEquals(5, m4.getStrength());
		assertEquals(15, base1.getTotalStrength(player1));
	}
	
	@Test
	public void testArmorStego() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 50, 3, 2, 1);
		Minion m1 = (Minion) new ArmorStego().setOwner(player1);
		
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		
		assertEquals(3, m1.getStrength());
		
		base1.addMinion(m1);
		m1.setBase(base1);
		
		assertEquals(3, m1.getStrength());
		
		table.nextPlayer();
		
		assertEquals(5, m1.getStrength());
		
		table.nextPlayer();
		
		assertEquals(3, m1.getStrength());
	}
	
	@Test
	public void testLaseratops() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 50, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		Minion m1 = (Minion) new Laseratops().setOwner(player1);
		final Minion m2 = (Minion) new Minion("TestMinion1", Faction.Aliens, 2).setOwner(player2);
		base1.addMinion(m2);
		m2.setBase(base1);
		
		assertEquals(1, base1.getMinions().size());
		assertTrue(base1.getMinions().contains(m2));
		assertFalse(player2.getDiscardPile().contains(m2));
		
		m1.play(base1, new Callback() {
			public Minion selectMinionInPlay(String message, boolean optional, Callback.Predicate<Minion> pred) {
				return m2;
			}
		});
		
		assertEquals(0, base1.getMinions().size());
		assertTrue(player2.getDiscardPile().contains(m2));
	}
}
