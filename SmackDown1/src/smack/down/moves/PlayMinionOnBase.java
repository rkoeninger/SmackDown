package smack.down.moves;

import smack.down.Base;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Minion;
import smack.down.Move;
import smack.down.Player;

public class PlayMinionOnBase implements Move {
	private Base base;
	private int strengthLimit;
	
	public PlayMinionOnBase(Base base) {
		this.base = base;
		this.strengthLimit = Integer.MAX_VALUE;
	}
	
	public PlayMinionOnBase(Base base, int strengthLimit) {
		this.base = base;
		this.strengthLimit = strengthLimit;
	}
	
	public String getName() {
		return "Play Minion";
	}
	
	public String getDescription() {
		return "Play a minion, any power, on any base from your hand.";
	}
	
	public boolean isPlayable(Player player) {
		for (DeckCard card : player.getHand())
			if ((card instanceof Minion) && (((Minion) card).getStrength() <= strengthLimit))
				return true;
		
		return false;
	}
	
	public void play(Player player, Callback callback) {
		Minion minion = (Minion) callback.selectCardFromHand("Select Minion", false, Callback.minionPredicate(strengthLimit));
		base.addMinion(minion);
	}
}
