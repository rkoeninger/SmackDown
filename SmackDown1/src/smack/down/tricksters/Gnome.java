package smack.down.tricksters;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Gnome extends Minion {
	
	public Gnome() {
		super("Gnome", Faction.Tricksters, 3);
	}
	
	public void play(final Base base, Callback callback) {
		int myMinionCount = 1; // 1 for the Gnome itself
		
		for (Minion minion : base.getMinions())
			if (minion.getOwner() == getOwner())
				myMinionCount++;
		
		final int finalMinionCount = myMinionCount;
		
		Minion minion = callback.selectMinionInPlay("Select a minion to destroy", true, new Callback.Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return (minion.getBase() == base) && (minion.getStrength() < finalMinionCount);
			}
		});
		
		if (minion != null)
			minion.destroy();
	}
}
