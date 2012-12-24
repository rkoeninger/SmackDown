package smack.down.robots;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.moves.PlayMinion;

public class Zapbot extends Minion {

	public Zapbot() {
		super("Zapbot", Faction.Robots, 2);
	}
	
	public void play(Base base, Callback callback) {
		getOwner().addMove(new PlayMinion(2));
	}
}
