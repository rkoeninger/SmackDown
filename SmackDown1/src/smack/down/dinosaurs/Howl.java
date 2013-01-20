package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.EffectFactory;
import smack.down.Faction;
import smack.down.Minion;

public class Howl extends Action {
	
	public Howl() {
		super("Howl", Faction.Dinosaurs, Target.General);
	}
	
	public void play(Callback callback) {
		for (Base base : getOwner().getTable().getBases())
			for (Minion minion : base.getMinions())
				if (minion.getOwner() == getOwner())
					getOwner().expireAtNextTurnEnd(EffectFactory.applyStrengthBonus(minion, 1));
	}
}
