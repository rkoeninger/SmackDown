package smack.down;

public abstract class Card {
	private String name;
	private Faction faction;
	
	public Card(String name, Faction faction) {
		this.name = name;
		this.faction = faction;
	}
	
	public String getName() {
		return name;
	}
	
	public Faction getFaction() {
		return faction;
	}
}
