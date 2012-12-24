package smack.down.tricksters;

import smack.down.Faction;
import smack.down.Action;
import smack.down.Player;

public class TakeTheShinies extends Action {
	
	public TakeTheShinies() {
		super("Take the Shinies", Faction.Tricksters, Target.General);
	}
	
	public void play() {
		for (Player player : getOwner().getOtherPlayers()) {
			player.discardRandomCard();
			player.discardRandomCard();
		}
	}
}
