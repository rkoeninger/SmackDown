package smack.down.wizards;

import smack.down.Faction;
import smack.down.Action;
import smack.down.moves.PlayAction;

public class TimeLoop extends Action {

	public TimeLoop() {
		super("Time Loop", Faction.Wizards, Target.General);
	}
	
	public void play() {
		getOwner().addMove(new PlayAction());
		getOwner().addMove(new PlayAction());
	}
}
