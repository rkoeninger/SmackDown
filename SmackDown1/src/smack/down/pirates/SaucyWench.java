package smack.down.pirates;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class SaucyWench extends Minion {
	
	public SaucyWench() {
		super("Saucy Wench", Faction.Pirates, 3);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay(base, 2);
		
		if (minion != null)
			minion.destroy();
	}
}
