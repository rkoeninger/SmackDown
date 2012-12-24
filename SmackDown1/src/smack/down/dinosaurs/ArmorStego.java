package smack.down.dinosaurs;

import smack.down.Faction;
import smack.down.Minion;

public class ArmorStego extends Minion {
	
	public ArmorStego() {
		super("Armor Stego", Faction.Dinosaurs, 3);
	}
	
	public int getStrength() {
		int bonus = 0;
		
		if ((getOwner() != null) && (getOwner().getTable().getCurrentPlayer() != getOwner()))
			bonus = 2;
		
		return super.getStrength() + bonus;
	}
}
