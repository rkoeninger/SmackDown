package smack.down.pirates;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;

public class FullSail extends Action {

	public FullSail() {
		super("Full Sail", Faction.Pirates, Target.General);
	}
	
	public void play(Callback callback) {
		
	}
}
