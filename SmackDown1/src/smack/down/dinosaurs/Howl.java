package smack.down.dinosaurs;

import smack.down.Action;
import smack.down.Base;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.effects.StrengthEffect;

public class Howl extends Action {
	
	public Howl() {
		super("Howl", Faction.Dinosaurs, Target.General);
	}
	
	public void play() {
		for (Base base : getOwner().getTable().getBases())
			for (Minion minion : base.getMinions())
				if (minion.getOwner() == getOwner()) {
					StrengthEffect effect = new StrengthEffect(minion, 1);
					minion.addEffect(effect);
					getOwner().expireAtNextTurnEnd(effect);
				}
	}
}
