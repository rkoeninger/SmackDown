package smack.down.dinosaurs;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Laseratops extends Minion {

	public Laseratops() {
		super("Laseratops", Faction.Dinosaurs, 4);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay(base, 2);
		
		if (minion != null)
			minion.destroy();
	}
}
