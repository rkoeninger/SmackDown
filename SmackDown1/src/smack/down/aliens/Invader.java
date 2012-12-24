package smack.down.aliens;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Invader extends Minion {
	
	public Invader() {
		super("Invader", Faction.Aliens, 3);
	}
	
	public void play(Base base, Callback callback) {
		getOwner().addPoints(1);
	}
}
