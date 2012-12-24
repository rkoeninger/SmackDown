package smack.down.ninjas;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class NinjaMaster extends Minion {

	public NinjaMaster() {
		super("Ninja Master", Faction.Ninjas, 5);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay("Select minion to destroy", true, new Callback.Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return (minion.getBase() == base);
			}
		});
		
		if (minion != null)
			minion.destroy();
	}
}
