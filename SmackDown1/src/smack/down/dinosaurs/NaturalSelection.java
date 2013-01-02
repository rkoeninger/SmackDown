package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class NaturalSelection extends Action {
	
	public NaturalSelection() {
		super("Natural Selection", Faction.Dinosaurs, Target.Minion);
	}
	
	public void play(final Minion minion, Callback callback) {
		Minion target = callback.selectMinionInPlay(minion.getBase(), minion.getStrength() + 1);
		target.destroy();
	}
}
