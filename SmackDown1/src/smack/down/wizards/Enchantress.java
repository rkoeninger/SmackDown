package smack.down.wizards;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Enchantress extends Minion {

	public Enchantress() {
		super("Enchantress", Faction.Wizards, 2);
	}
	
	public void play(Base base, Callback callback) {
		getOwner().draw();
	}
}
