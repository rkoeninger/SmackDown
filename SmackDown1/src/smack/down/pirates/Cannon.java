package smack.down.pirates;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Cannon extends Action {
	
	public Cannon() {
		super("Cannon", Faction.Pirates, Target.General);
	}
	
	public void play(Callback callback) {
		Minion target = callback.selectMinionInPlay(2);
		
		if (target == null)
			return;
		
		target.destroy();
		
		target = callback.selectMinionInPlay(2);
		
		if (target == null)
			return;
		
		target.destroy();
	}
}
