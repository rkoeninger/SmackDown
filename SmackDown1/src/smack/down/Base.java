package smack.down;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Base extends Card {
	private int cap;
	private List<Minion> minions;
	private int score1, score2, score3;
	
	public Base(String name, Faction faction, int cap, int score1, int score2, int score3) {
		super(name, faction);
		this.minions = new ArrayList<Minion>();
		this.cap = cap;
		this.score1 = score1;
		this.score2 = score2;
		this.score3 = score3;
	}
	
	public List<Minion> getMinions() {
		return new ArrayList<Minion>(minions);
	}
	
	public Base addMinion(Minion minion) {
		minions.add(minion);
		return this;
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
		
		for (Minion minion : minions)
			strength += minion.getStrength();
		
		return strength;
	}
	
	public int getTotalStrength(Player player) {
		int strength = 0;
		
		for (Minion minion : minions)
			if (player.equals(minion.getOwner()))
				strength += minion.getStrength();
		
		return strength;
	}
	
	public boolean isCapped() {
		return getTotalStrength() >= cap;
	}
	
	public Map<Player, Integer> getScores() {
		Map<Player, Integer> strengths = new HashMap<Player, Integer>();
		
		for (Minion m : minions) {
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
		for (Minion m : minions) {
			m.setBase(null);
			m.discard();
		}
		
		minions.clear();
		return this;
	}
}
