package smack.down;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {
	private List<Player> players;
	private List<Base> bases;
	private List<Base> baseDeck;
	private int currentPlayer = 0;
	
	public Table(List<Player> players, List<Base> baseDeck, List<Base> bases) {
		this.players = new ArrayList<Player>(players);
		this.baseDeck = new ArrayList<Base>(baseDeck);
		this.bases = new ArrayList<Base>(bases);
	}
	
	public Player nextPlayer() {
		currentPlayer++;
		currentPlayer = currentPlayer % players.size();
		return getCurrentPlayer();
	}
	
	public boolean hasWinner() {
		int maxScore = 0;
		boolean tie = false;
		
		for (Player player : players)
			if (player.getPoints() > maxScore) {
				maxScore = player.getPoints();
				tie = false;
			} else if (player.getPoints() == maxScore) {
				tie = true;
			}
		
		return (maxScore >= 15) && !tie;
	}
	
	public Player getWinner() {
		Player winner = null;
		int maxScore = 0;
		boolean tie = false;
		
		for (Player player : players)
			if (player.getPoints() > maxScore) {
				maxScore = player.getPoints();
				winner = player;
				tie = false;
			} else if (player.getPoints() == maxScore) {
				tie = true;
				winner = null;
			}
		
		return ((maxScore >= 15) && !tie) ? winner : null;
	}
	
	public void scoreBases() {
		Base base = null;
		
		while ((base = getFirstCappedBase()) != null) {
			Map<Player, Integer> scores = base.getScores();
			
			for (Map.Entry<Player, Integer> score : scores.entrySet())
				score.getKey().addPoints(score.getValue());
			
			base.clear();
		}
	}
	
	public boolean anyBasesCapped() {
		for (Base base : bases)
			if (base.isCapped())
				return true;
		
		return false;
	}
	
	public Base getFirstCappedBase() {
		for (Base base : bases)
			if (base.isCapped())
				return base;
		
		return null;
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}
	
	public List<Base> getBases() {
		return new ArrayList<Base>(bases);
	}
	
	public List<Minion> getMinions() {
		List<Minion> minions = new ArrayList<Minion>();
		
		for (Base base : bases)
			minions.addAll(base.getMinions());
		
		return minions;
	}
	
	public List<Minion> getMinions(Player player) {
		List<Minion> minions = new ArrayList<Minion>();
		
		for (Base base : bases)
			for (Minion minion : base.getMinions())
				if (minion.getOwner() == player)
					minions.add(minion);
		
		return minions;
	}
	
	public List<Base> getBaseDeck() {
		return new ArrayList<Base>(baseDeck);
	}
}
