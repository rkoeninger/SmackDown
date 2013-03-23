package smack.down.pirates;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Shanghai extends Action {

	public Shanghai() {
		super("Shanghai", Faction.Pirates, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		
	}
}
