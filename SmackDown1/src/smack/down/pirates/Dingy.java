package smack.down.pirates;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;

public class Dingy extends Action {

	public Dingy() {
		super("Dingy", Faction.Pirates, Target.General);
	}
	
	public void play(Callback callback) {
		
	}
}
