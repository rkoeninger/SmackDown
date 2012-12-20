package smack.down;

public abstract class DeckCard extends Card {
	private Player owner;
	
	public DeckCard(String name, Faction faction) {
		super(name, faction);
	}
	
	public DeckCard setOwner(Player owner) {
		this.owner = owner;
		return this;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void discard() {
		if (owner == null)
			throw new RuntimeException("no owner");
		
		owner.addToDiscard(this);
	}
}
