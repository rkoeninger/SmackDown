package smack.down.wizards;

import smack.down.Action;
import smack.down.Faction;

public class MysticStudies extends Action {
	
	public MysticStudies() {
		super("Mystic Studies", Faction.Wizards, Target.General);
	}
	
	public void play() {
		getOwner().draw(2);
	}
}
