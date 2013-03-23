package smack.down.pirates;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class SeaDogs extends Action {

	public SeaDogs() {
		super("Sea Dogs", Faction.Pirates, Target.Base);
	}
	
	public void play(Base base, Callback callback) {
		Faction faction = callback.selectFaction();
		Base destBase = callback.selectBaseInPlay("Select destination Base", false, Callback.anyOtherBase(base));
		
		for (Minion minion : base.getMinions())
			if (minion.getFaction() == faction)
				minion.setBase(destBase);
	}
}
