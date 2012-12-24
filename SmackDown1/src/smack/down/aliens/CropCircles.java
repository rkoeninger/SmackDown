package smack.down.aliens;

import smack.down.Base;
import smack.down.Faction;
import smack.down.Action;
import smack.down.Minion;

public class CropCircles extends Action {

	public CropCircles() {
		super("Crop Circles", Faction.Aliens, Target.Base);
	}
	
	public void play(Base base) {
		for (Minion minion : base.getMinions())
			minion.returnToHand();
	}
}
