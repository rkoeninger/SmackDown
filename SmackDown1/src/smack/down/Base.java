package smack.down;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Base extends Card {
	private int cap;
	private List<DeckCard> cards;
	private int score1, score2, score3;
	
	public Base(String name, Faction faction, int cap, int score1, int score2, int score3) {
		super(name, faction);
		this.cards = new ArrayList<DeckCard>();
		this.cap = cap;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
	}
	
	public List<Minion> getMinions() {
		List<Minion> minions = new ArrayList<Minion>();
		
		for (DeckCard card : cards)
			if (card instanceof Minion)
				minions.add((Minion) card);
		
		return minions;
	}
	
	public List<Minion> getMinions(Player player) {
		List<Minion> playersMinions = new ArrayList<Minion>();
		
		for (Minion minion : getMinions())
			if (minion.getOwner() == player)
				playersMinions.add(minion);
		
		return playersMinions;
	}
	
	public Base addMinion(Minion minion) {
		cards.add(minion);
		
		if (minion.getBase() != this)
			minion.setBase(this);
		
		return this;
	}
	
	public Base removeMinion(Minion minion) {
		cards.remove(minion);
		
		if (minion.getBase() == this)
			minion.setBase(null);
		
		return this;
	}
	
	public boolean hasMinion(Minion minion) {
		return cards.contains(minion);
	}
	
	public int getCap() {
		return cap;
	}
	
	public int getFirstScore() {
		return score1;
	}
	
	public int getSecondScore() {
		return score2;
	}
	
	public int getThirdScore() { 
		return score3;
	}
	
	public int getTotalStrength() {
		int strength = 0;
		
		for (Minion minion : getMinions())
			strength += minion.getStrength();
		
		return strength;
	}
	
	public int getTotalStrength(Player player) {
		int strength = 0;
		
		for (Minion minion : getMinions())
			if (player.equals(minion.getOwner()))
				strength += minion.getStrength();
		
		return strength;
	}
	
	public boolean isCapped() {
		return getTotalStrength() >= cap;
	}
	
	public Map<Player, Integer> getScores() {
		Map<Player, Integer> strengths = new HashMap<Player, Integer>();
		
		for (Minion m : getMinions()) {
			if (m.getOwner() != null) {
				if (strengths.containsKey(m.getOwner())) {
					strengths.put(m.getOwner(), strengths.get(m.getOwner()) + m.getStrength());
				}
				else {
					strengths.put(m.getOwner(), m.getStrength());
				}
			}
		}
		
		List<Integer> rankedStrengths = new ArrayList<Integer>();
		
		for (Integer strength : strengths.values())
			if (! rankedStrengths.contains(strength))
				rankedStrengths.add(strength);
		
		Collections.sort(rankedStrengths);
		Collections.reverse(rankedStrengths);
		
		Map<Player, Integer> results = new HashMap<Player, Integer>();
		
		int scoreAssignments = 0;
		
		for (Integer strength : rankedStrengths) {
			if (scoreAssignments >= 3)
				break;
			
			int value =
				scoreAssignments == 0 ? getFirstScore() :
				scoreAssignments == 1 ? getSecondScore() :
				scoreAssignments == 2 ? getThirdScore() :
				0;
				
			for (Map.Entry<Player, Integer> playerStrength : strengths.entrySet()) {
				if (playerStrength.getValue() == strength) {
					results.put(playerStrength.getKey(), value);
					scoreAssignments++;
				}
			}
		}
		
		return results;
	}
	
	public Base clear() {
		for (Minion m : getMinions()) {
			m.setBase(null);
			m.discard();
		}
		
		cards.clear();
		return this;
	}
}
