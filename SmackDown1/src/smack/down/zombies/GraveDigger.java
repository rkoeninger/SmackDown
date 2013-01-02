package smack.down.zombies;

import smack.down.Base;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Faction;
import smack.down.Minion;

public class GraveDigger extends Minion {
	
	public GraveDigger() {
		super("Grave Digger", Faction.Zombies, 4);
	}
	
	public void play(Base base, Callback callback) {
		DeckCard card = callback.selectFromDiscard("Select card and put into your hand.", true, Callback.minionPredicate());
		
		if (card != null) {
			getOwner().getDiscardPile().remove(card);
			getOwner().getHand().add(card);
		}
	}
}
