package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.effects.StrengthEffect;

public class Upgrade extends Action {
	
	public Upgrade() {
		super("Upgrade", Faction.Dinosaurs, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		minion.addEffect(new StrengthEffect(minion, 2));
	}
}
