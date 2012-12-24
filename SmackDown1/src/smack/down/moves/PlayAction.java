package smack.down.moves;

import smack.down.Action;
import smack.down.Callback;
import smack.down.DeckCard;
import smack.down.Move;
import smack.down.Player;

public class PlayAction implements Move {
	
	public String getName() {
		return "Play Action";
	}
	
	public String getDescription() {
		return "Play action anywhere from your hand.";
	}
	
	public boolean isPlayable(Player player) {
		for (DeckCard card : player.getHand())
			if (card instanceof Action)
				return true;
		
		return false;
	}
	
	public void play(Player player, Callback callback) {
		
	}
}
