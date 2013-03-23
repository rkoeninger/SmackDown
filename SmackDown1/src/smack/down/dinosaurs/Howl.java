package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Callback;
import smack.down.EffectFactory;
import smack.down.Faction;
import smack.down.Minion;

public class Howl extends Action {
	
	public Howl() {
		super("Howl", Faction.Dinosaurs, Target.General);
	}
	
	public void play(Callback callback) {
		for (Minion minion : getOwner().getTable().getMinions(getOwner()))
			getOwner().expireAtNextTurnEnd(EffectFactory.applyStrengthBonus(minion, 1));
	}
}
