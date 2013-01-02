package smack.down.robots;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class TechCenter extends Action {

	public TechCenter() {
		super("Tech Center", Faction.Wizards, Target.Base);
	}
	
	public void play(Base base, Callback callback) {
		int minionCount = 0;
		
		for (Minion minion : base.getMinions())
			if (getOwner() == minion.getOwner())
				minionCount++;
		
		getOwner().draw(minionCount);
	}
}
