package smack.down.dinosaurs;

import smack.down.Callback;
import smack.down.EffectFactory;
import smack.down.Faction;
import smack.down.Action;
import smack.down.Minion;

public class Augmentation extends Action {
	
	public Augmentation() {
		super("Augmentation", Faction.Dinosaurs, Target.Minion);
	}
	
	public void play(Minion minion, Callback callback) {
		minion.getOwner().expireAtNextTurnEnd(EffectFactory.applyStrengthBonus(minion, 4));
	}
}
