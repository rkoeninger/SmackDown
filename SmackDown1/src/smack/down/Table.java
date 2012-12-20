package smack.down;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private List<Player> players;
	private List<Base> bases;
	private List<Base> baseDeck;
	private int currentPlayer = 0;
	
	public Table(List<Player> players, List<Base> baseDeck) {
		this.players = new ArrayList<Player>(players);
		this.baseDeck = new ArrayList<Base>(baseDeck);
		this.bases = new ArrayList<Base>(players.size() - 1);
		
		for (int i = 0; i < players.size(); ++i)
			this.bases.add(baseDeck.remove(baseDeck.size() - 1));
	}
	
	public Player nextPlayer() {
		currentPlayer++;
		currentPlayer = currentPlayer % players.size();
		return getCurrentPlayer();
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
	
	public List<Base> getBaseDeck() {
		return new ArrayList<Base>(baseDeck);
	}
}
