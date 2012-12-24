package smack.down.dinosaurs;

import smack.down.Faction;
import smack.down.Minion;

public class WarRaptor extends Minion {

	public WarRaptor() {
		super("War Raptor", Faction.Dinosaurs, 2);
	}
	
	public int getStrength() {
		int bonus = 0;
		
		if ((getOwner() != null) && (getBase() != null))
			for (Minion minion : getBase().getMinions())
				if ((minion.getOwner() == getOwner()) && (minion instanceof WarRaptor))
					bonus++;
		
		return super.getStrength() + bonus;
	}
}
