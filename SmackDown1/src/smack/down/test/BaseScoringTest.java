package smack.down.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import smack.down.*;

public class BaseScoringTest {
	
	@Test
	public void onePlayer() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(4, base.getMinions().size());
		assertEquals(14, base.getTotalStrength());
		assertEquals(14, base.getTotalStrength(player1));
		
		assertEquals(1, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
	}
	
	@Test
	public void twoPlayersUnevenScore() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 4).setOwner(player2);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(5, base.getMinions().size());
		assertEquals(20, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(11, base.getTotalStrength(player2));
		
		assertEquals(2, scores.keySet().size());
		assertEquals(new Integer(2), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
	}
	
	@Test
	public void twoPlayersEvenScore() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		base.addMinion(m2).addMinion(m3).addMinion(m4);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(3, base.getMinions().size());
		assertEquals(14, base.getTotalStrength());
		
		assertEquals(2, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
	}
	
	@Test
	public void threePlayersUnevenScore() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 4).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(7, base.getMinions().size());
		assertEquals(27, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(11, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(2), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
	}
	
	@Test
	public void threePlayers2WayTieForFirst() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m8 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m6).addMinion(m7).addMinion(m8);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(7, base.getMinions().size());
		assertEquals(25, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(9, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
	}
	
	@Test
	public void threePlayers2WayTieForSecond() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m6).addMinion(m7);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(6, base.getMinions().size());
		assertEquals(23, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(7, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(2), scores.get(player2));
		assertEquals(new Integer(2), scores.get(player3));
	}
	
	@Test
	public void threePlayers3WayTie() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		base.addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m6).addMinion(m7);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(5, base.getMinions().size());
		assertEquals(21, base.getTotalStrength());
		assertEquals(7, base.getTotalStrength(player1));
		assertEquals(7, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(3), scores.get(player3));
	}
	
	@Test
	public void fourPlayersUnevenScore() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 4).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(9, base.getMinions().size());
		assertEquals(32, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(11, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		assertEquals(5, base.getTotalStrength(player4));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(2), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
		assertEquals(null, scores.get(player4));
	}
	
	@Test
	public void fourPlayers2WayTieForFirst() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(9, base.getMinions().size());
		assertEquals(30, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(9, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		assertEquals(5, base.getTotalStrength(player4));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
		assertEquals(null, scores.get(player4));
	}
	
	@Test
	public void fourPlayers2WayTieForSecond() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(8, base.getMinions().size());
		assertEquals(28, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(7, base.getTotalStrength(player2));
		assertEquals(7, base.getTotalStrength(player3));
		assertEquals(5, base.getTotalStrength(player4));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(2), scores.get(player2));
		assertEquals(new Integer(2), scores.get(player3));
		assertEquals(null, scores.get(player4));
	}
	
	@Test
	public void fourPlayers2WayTieForThird() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m6).addMinion(m8).addMinion(m9);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(7, base.getMinions().size());
		assertEquals(26, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(7, base.getTotalStrength(player2));
		assertEquals(5, base.getTotalStrength(player3));
		assertEquals(5, base.getTotalStrength(player4));
		
		assertEquals(4, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(2), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
		assertEquals(new Integer(1), scores.get(player4));
	}
	
	@Test
	public void fourPlayers3WayTieForFirst() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion ma = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9).addMinion(ma);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(10, base.getMinions().size());
		assertEquals(32, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(9, base.getTotalStrength(player2));
		assertEquals(9, base.getTotalStrength(player3));
		assertEquals(5, base.getTotalStrength(player4));
		
		assertEquals(3, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(3), scores.get(player3));
		assertEquals(null, scores.get(player4));
	}
	
	@Test
	public void fourPlayers3WayTieForSecond() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion mb = (Minion) new Minion("", Faction.Aliens, 3).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion ma = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		Minion mc = (Minion) new Minion("", Faction.Pirates, 4).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9).addMinion(ma).addMinion(mb).addMinion(mc);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(12, base.getMinions().size());
		assertEquals(39, base.getTotalStrength());
		assertEquals(12, base.getTotalStrength(player1));
		assertEquals(9, base.getTotalStrength(player2));
		assertEquals(9, base.getTotalStrength(player3));
		assertEquals(9, base.getTotalStrength(player4));
		
		assertEquals(4, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(2), scores.get(player2));
		assertEquals(new Integer(2), scores.get(player3));
		assertEquals(new Integer(2), scores.get(player4));
	}
	
	@Test
	public void fourPlayers3WayTieForFirstAndSecond() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion mb = (Minion) new Minion("", Faction.Aliens, 3).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion md = (Minion) new Minion("", Faction.Dinosaurs, 3).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion ma = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		Minion mc = (Minion) new Minion("", Faction.Pirates, 4).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9).addMinion(ma).addMinion(mb).addMinion(mc).addMinion(md);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(13, base.getMinions().size());
		assertEquals(42, base.getTotalStrength());
		assertEquals(12, base.getTotalStrength(player1));
		assertEquals(12, base.getTotalStrength(player2));
		assertEquals(9, base.getTotalStrength(player3));
		assertEquals(9, base.getTotalStrength(player4));
		
		assertEquals(4, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(1), scores.get(player3));
		assertEquals(new Integer(1), scores.get(player4));
	}
	
	@Test
	public void fourPlayers4WayTie() {
		Player player1 = new Player("Rob", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player2 = new Player("Joe", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player3 = new Player("Don", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Player player4 = new Player("Zac", new Deck(new ArrayList<DeckCard>()), new ArrayList<DeckCard>());
		Base base = new Base("TestBase", Faction.Aliens, 12, 3, 2, 1);
		Minion m1 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m2 = (Minion) new Minion("", Faction.Aliens, 2).setOwner(player1);
		Minion m3 = (Minion) new Minion("", Faction.Aliens, 5).setOwner(player1);
		Minion m4 = (Minion) new Minion("", Faction.Dinosaurs, 7).setOwner(player2);
		Minion m5 = (Minion) new Minion("", Faction.Dinosaurs, 2).setOwner(player2);
		Minion m6 = (Minion) new Minion("", Faction.Ninjas, 5).setOwner(player3);
		Minion m7 = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion ma = (Minion) new Minion("", Faction.Ninjas, 2).setOwner(player3);
		Minion m8 = (Minion) new Minion("", Faction.Pirates, 3).setOwner(player4);
		Minion m9 = (Minion) new Minion("", Faction.Pirates, 2).setOwner(player4);
		Minion mc = (Minion) new Minion("", Faction.Pirates, 4).setOwner(player4);
		base.addMinion(m1).addMinion(m2).addMinion(m3).addMinion(m4).addMinion(m5).addMinion(m6).addMinion(m7).addMinion(m8).addMinion(m9).addMinion(ma).addMinion(mc);
		Map<Player, Integer> scores = base.getScores();
		
		assertEquals(11, base.getMinions().size());
		assertEquals(36, base.getTotalStrength());
		assertEquals(9, base.getTotalStrength(player1));
		assertEquals(9, base.getTotalStrength(player2));
		assertEquals(9, base.getTotalStrength(player3));
		assertEquals(9, base.getTotalStrength(player4));
		
		assertEquals(4, scores.keySet().size());
		assertEquals(new Integer(3), scores.get(player1));
		assertEquals(new Integer(3), scores.get(player2));
		assertEquals(new Integer(3), scores.get(player3));
		assertEquals(new Integer(3), scores.get(player4));
	}
}
