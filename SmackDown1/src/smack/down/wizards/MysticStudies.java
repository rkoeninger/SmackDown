package smack.down.wizards;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;

public class MysticStudies extends Action {
	
	public MysticStudies() {
		super("Mystic Studies", Faction.Wizards, Target.General);
	}
	
	public void play(Callback callback) {
		getOwner().draw(2);
	}
}
