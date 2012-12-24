package smack.down.aliens;

import smack.down.Action;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.moves.PlayMinion;

public class Abduction extends Action {
	
	public Abduction() {
		super("Abduction", Faction.Aliens, Target.Minion);
	}
	
	public void play(Minion minion) {
		minion.returnToHand();
		getOwner().addMove(new PlayMinion());
	}
}
