package smack.down.wizards;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.moves.PlayMinion;

public class Summon extends Action {

	public Summon() {
		super("Summon", Faction.Wizards, Target.General);
	}
	
	public void play(Callback callback) {
		getOwner().addMove(new PlayMinion());
	}
}
