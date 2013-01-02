package smack.down.moves;

import smack.down.Base;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Minion;
import smack.down.Move;
import smack.down.Player;

public class PlayMinionFromDiscard implements Move {
	private int strengthLimit;
	
	public PlayMinionFromDiscard() {
		this.strengthLimit = Integer.MAX_VALUE;
	}
	
	public PlayMinionFromDiscard(int strengthLimit) {
		this.strengthLimit = strengthLimit;
	}
	
	public String getName() {
		return "Play Minion From Discard";
	}
	
	public String getDescription() {
		return "Play a minion, any power, on any base from your discard pile.";
	}
	
	public boolean isPlayable(Player player) {
		for (DeckCard card : player.getDiscardPile())
			if (card instanceof Minion)
				return true;
		
		return false;
	}
	
	public void play(Player player, Callback callback) {
		Minion minion = (Minion) callback.selectFromDiscard("Select Minion", false, Callback.minionPredicate(strengthLimit));
		Base base = callback.selectBaseInPlay("Select Base", false, Callback.truePredicate(new Base[0]));
		base.addMinion(minion);
	}
}
