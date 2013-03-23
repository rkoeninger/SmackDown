package smack.down;

public abstract class DeckCard extends Card {
	private Player owner;
	private CardSet container;
	
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
	
	public DeckCard setContainer(CardSet container) {
		CardSet oldContainer = this.container;
		this.container = container;
		
		if ((oldContainer != null) && (oldContainer.contains(this)))
			oldContainer.remove(this);
		
		if ((container != null) && (! container.contains(this)))
			container.add(this);
		
		return this;
	}
	
	public CardSet getContainer() {
		return container;
	}
	
	public void discard() {
		if (owner == null)
			throw new RuntimeException("no owner");
		
		owner.getDiscardPile().add(this);
	}
	
	public DeckCard putInHand() {
		if (owner == null)
			throw new RuntimeException("no owner");
		
		owner.getHand().add(this);
		return this;
	}
}
