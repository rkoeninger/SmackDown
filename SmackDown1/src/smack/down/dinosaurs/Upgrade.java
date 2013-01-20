package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Callback;
import smack.down.EffectFactory;
import smack.down.Faction;
import smack.down.Minion;

public class Upgrade extends Action {
	
	public Upgrade() {
		super("Upgrade", Faction.Dinosaurs, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		EffectFactory.applyStrengthBonus(minion, 2);
	}
}
