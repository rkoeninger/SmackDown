package smack.down.tricksters;

import smack.down.Faction;
import smack.down.Minion;
import smack.down.Player;

public class Gremlin extends Minion {
	
	public Gremlin() {
		super("Gremlin", Faction.Tricksters, 2);
	}
	
	public void destroyed() {
		getOwner().draw();
		
		for (Player player : getOwner().getOtherPlayers())
			player.discardRandomCard();
	}
}
