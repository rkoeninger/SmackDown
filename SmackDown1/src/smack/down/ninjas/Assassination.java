package smack.down.ninjas;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Effect;
import smack.down.Faction;
import smack.down.Minion;

public class Assassination extends Action {
	
	public Assassination() {
		super("Assassination", Faction.Ninjas, Target.Minion);
	}
	
	public void play(final Minion minion, Callback callback) {
		Effect killAtEndOfTurn = new Effect() {
			public void expire() {
				minion.destroy();
			}
		};
		
		getOwner().expireAtNextTurnEnd(killAtEndOfTurn);
	}
}
