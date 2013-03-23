package smack.down.pirates;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;

public class Broadside extends Action {

	public Broadside() {
		super("Broadside", Faction.Pirates, Target.Base);
	}
	
	public void play(Base base, Callback callback) {
		
	}
}
