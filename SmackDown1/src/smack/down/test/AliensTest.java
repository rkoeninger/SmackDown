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
		Player player1 = new Player("TestPlayer1", new CardList(), new CardSet(), new CardSet());
		Player player2 = new Player("TestPlayer2", new CardList(), new CardSet(), new CardSet());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		
		final Minion m1 = new Minion("TestMinion1", Faction.Dinosaurs, 2).setOwner(player1).setBase(base1);
		Minion m2 = new Collector().setOwner(player2);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(0, player1.getHand().size());
		
		m2.play(base1, new Callback() {
			public Minion selectMinionInPlay(Base base, int strengthLimit) {
				return m1;
			}
		});
		m2.setBase(base1);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(1, player1.getHand().size());
	}
	
	@Test
	public void testInvader() {
		Player player1 = new Player("TestPlayer1", new CardList(), new CardSet(), new CardSet());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		new Table(Arrays.asList(player1), new ArrayList<Base>(), Arrays.asList(base1));
		
		Minion m1 = new Invader().setOwner(player1);
		
		assertEquals(0, player1.getPoints());
		
		m1.play(base1, new Callback(){});
		
		assertEquals(1, player1.getPoints());
	}
	
	@Test
	public void testAbduction() {
		Player player1 = new Player("TestPlayer1", new CardList(), new CardSet(), new CardSet());
		Player player2 = new Player("TestPlayer2", new CardList(), new CardSet(), new CardSet());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		new Table(Arrays.asList(player1, player2), new ArrayList<Base>(), Arrays.asList(base1));
		
		final Minion m1 = new Minion("TestMinion1", Faction.Dinosaurs, 2).setOwner(player1).setBase(base1);
		Action action = new Abduction().setOwner(player2);
		
		assertEquals(1, base1.getMinions().size());
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player2.getMoves().size());
		
		action.play(m1, new Callback(){});
		
		assertEquals(0, base1.getMinions().size());
		assertEquals(1, player1.getHand().size());
		assertEquals(1, player2.getMoves().size());
	}
	
	@Test
	public void testCropCircles() {
		Player player1 = new Player("TestPlayer1", new CardList(), new CardSet(), new CardSet());
		Player player2 = new Player("TestPlayer2", new CardList(), new CardSet(), new CardSet());
		Player player3 = new Player("TestPlayer3", new CardList(), new CardSet(), new CardSet());
		Player player4 = new Player("TestPlayer4", new CardList(), new CardSet(), new CardSet());
		Base base1 = new Base("TestBase", Faction.Aliens, 100, 3, 2, 1);
		new Table(Arrays.asList(player1, player2, player3, player4), new ArrayList<Base>(), Arrays.asList(base1));
		
		new Minion("TestMinion1", Faction.Dinosaurs, 4).setOwner(player1).setBase(base1);
		new Minion("TestMinion2", Faction.Dinosaurs, 3).setOwner(player1).setBase(base1);
		new Minion("TestMinion3", Faction.Ninjas, 2).setOwner(player2).setBase(base1);
		new Minion("TestMinion4", Faction.Pirates, 2).setOwner(player3).setBase(base1);
		new Minion("TestMinion5", Faction.Tricksters, 4).setOwner(player3).setBase(base1);
		new Minion("TestMinion6", Faction.Tricksters, 5).setOwner(player3).setBase(base1);
		new Minion("TestMinion7", Faction.Wizards, 4).setOwner(player4).setBase(base1);
		new Minion("TestMinion8", Faction.Wizards, 7).setOwner(player4).setBase(base1);
		new Minion("TestMinion9", Faction.Zombies, 3).setOwner(player4).setBase(base1);
		
		assertEquals(0, player1.getHand().size());
		assertEquals(0, player2.getHand().size());
		assertEquals(0, player3.getHand().size());
		assertEquals(0, player4.getHand().size());
		assertEquals(9, base1.getMinions().size());
		
		Action action = new CropCircles().setOwner(player1);
		action.play(base1, new Callback(){});
		
		assertEquals(2, player1.getHand().size());
		assertEquals(1, player2.getHand().size());
		assertEquals(3, player3.getHand().size());
		assertEquals(3, player4.getHand().size());
		assertEquals(0, base1.getMinions().size());
	}
}
