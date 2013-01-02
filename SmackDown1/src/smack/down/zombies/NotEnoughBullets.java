package smack.down.zombies;

import java.util.ArrayList;
import java.util.List;

import smack.down.Action;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Faction;
import smack.down.Minion;

public class NotEnoughBullets extends Action {
	
	public NotEnoughBullets() {
		super("Not Enough Bullets", Faction.Zombies, Target.General);
	}
	
	public void play(Callback callback) {
		Class<? extends Minion> minionType = callback.selectMinionTypeFromDiscard("Select minion type", true);
		
		if (minionType == null)
			return;
		
		List<Minion> minions = new ArrayList<Minion>();
		
		for (DeckCard minion : getOwner().getDiscardPile())
			if (minionType.isInstance(minion))
				minions.add((Minion) minion);
		
		Integer amount = callback.selectAmount("Select minion count", true, minions.size());
		
		if ((amount == null) || (amount == 0))
			return;
		
		while (amount > 0) {
			getOwner().removeFromDiscard(minions.get(amount - 1));
			getOwner().addToHand(minions.get(amount - 1));
			amount--;
		}
	}
}
