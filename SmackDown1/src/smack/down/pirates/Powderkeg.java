package smack.down.pirates;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Powderkeg extends Action {

	public Powderkeg() {
		super("Powerkeg", Faction.Pirates, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		
	}
}
