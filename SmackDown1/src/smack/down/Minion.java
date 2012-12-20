package smack.down;

public class Minion extends DeckCard {
	private int strength;
	private Base base = null;
	
	public Minion(String name, Faction faction, int strength) {
		super(name, faction);
		this.strength = strength;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public Minion setBase(Base base) {
		this.base = base;
		return this;
	}
	
	public Base getBase() {
		return base;
	}
	
	public boolean isOnBase() {
		return base != null;
	}
}
