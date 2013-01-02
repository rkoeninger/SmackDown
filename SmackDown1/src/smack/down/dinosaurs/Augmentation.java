package smack.down.dinosaurs;

import smack.down.Callback;
import smack.down.Faction;
import smack.down.Action;
import smack.down.Minion;
import smack.down.effects.StrengthEffect;

public class Augmentation extends Action {
	
	public Augmentation() {
		super("Augmentation", Faction.Dinosaurs, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		StrengthEffect effect = new StrengthEffect(minion, 4);
		minion.addEffect(effect);
		minion.getOwner().expireAtNextTurnEnd(effect);
	}
}
