package smack.down.zombies;

import smack.down.Base;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Faction;
import smack.down.Minion;

public class Walker extends Minion {

	public Walker() {
		super("Walker", Faction.Zombies, 2);
	}
	
	public void play(Base base, Callback callback) {
		DeckCard card = getOwner().peek();
		
		if (card == null)
			return;
		
		if (callback.confirm("Put \"" + card.getName() + "\" in discard pile?")) {
			getOwner().draw();
			card.discard();
		}
	}
}
