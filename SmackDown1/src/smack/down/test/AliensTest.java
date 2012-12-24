package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import smack.down.*;
import smack.down.aliens.*;

public class AliensTest {
	
	@Test
	public void testCollector() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		
		final Minion m1 = (Minion) new Minion("TestMinion1", Faction.Dinosaurs, 2).setOwner(player1);
		base1.addMinion(m1);
		m1.setBase(base1);
		
		Minion m2 = (Minion) new Collector().setOwner(player2);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(0, player1.getHand().size());
		
		m2.play(base1, new Callback() {
			public Minion selectMinionInPlay(String message, boolean optional, Predicate<Minion> pred) {
				return m1;
			}
		});
		base1.addMinion(m1);
		m1.setBase(base1);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(1, player1.getHand().size());
	}
	
	@Test
	public void testInvader() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		
		Minion m1 = (Minion) new Invader().setOwner(player1);
		
		assertEquals(0, player1.getPoints());
		
		m1.play(base1, new Callback(){});
		
		assertEquals(1, player1.getPoints());
	}
	
	@Test
	public void testAbduction() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		
		final Minion m1 = (Minion) new Minion("TestMinion1", Faction.Dinosaurs, 2).setOwner(player1);
		base1.addMinion(m1);
		m1.setBase(base1);
		
		Action action = new Abduction();
		action.setOwner(player2);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player2.getMoves().size());
		
		action.play(m1);
		
		assertEquals(0, base1.getMinions().size());
		assertEquals(1, player1.getHand().size());
		assertEquals(1, player2.getMoves().size());
	}
	
	@Test
	public void testCropCircles() {
		Player player1 = new Player("TestPlayer1", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("TestPlayer2", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("TestPlayer3", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("TestPlayer4", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		Table table = new Table(Arrays.asList(player1, player2, player3, player4), new ArrayList<Base>(), Arrays.asList(base1));
		player1.setTable(table);
		player2.setTable(table);
		player3.setTable(table);
		player4.setTable(table);
		
		Minion m1 = (Minion) new Minion("TestMinion1", Faction.Dinosaurs, 4).setOwner(player1);
		Minion m2 = (Minion) new Minion("TestMinion2", Faction.Dinosaurs, 3).setOwner(player1);
		Minion m3 = (Minion) new Minion("TestMinion3", Faction.Ninjas, 2).setOwner(player2);
		Minion m4 = (Minion) new Minion("TestMinion4", Faction.Pirates, 2).setOwner(player3);
		Minion m5 = (Minion) new Minion("TestMinion5", Faction.Tricksters, 4).setOwner(player3);
		Minion m6 = (Minion) new Minion("TestMinion6", Faction.Tricksters, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("TestMinion7", Faction.Wizards, 4).setOwner(player4);
		Minion m8 = (Minion) new Minion("TestMinion8", Faction.Wizards, 7).setOwner(player4);
		Minion m9 = (Minion) new Minion("TestMinion9", Faction.Zombies, 3).setOwner(player4);
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
		
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player2.getHand().size());
		assertEquals(0, player3.getHand().size());
		assertEquals(0, player4.getHand().size());
		assertEquals(9, base1.getMinions().size());
		
		Action action = (Action) new CropCircles().setOwner(player1);
		action.play(base1);
		
		assertEquals(2, player1.getHand().size());
		assertEquals(1, player2.getHand().size());
		assertEquals(3, player3.getHand().size());
		assertEquals(3, player4.getHand().size());
		assertEquals(0, base1.getMinions().size());
	}
}
