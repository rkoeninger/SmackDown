package smack.down.zombies;

import smack.down.Action;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.moves.PlayMinionFromDiscard;

public class TheyKeepComing extends Action {
	
	public TheyKeepComing() {
		super("They Keep Coming", Faction.Zombies, Target.General);
	}
	
	public void play(Callback callback) {
		getOwner().addMove(new PlayMinionFromDiscard());
	}
}
