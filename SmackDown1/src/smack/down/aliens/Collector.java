package smack.down.aliens;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Collector extends Minion {

	public Collector() {
		super("Collector", Faction.Aliens, 2);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay(base, 3);
		
		if (minion != null)
			minion.returnToHand();
	}
}
