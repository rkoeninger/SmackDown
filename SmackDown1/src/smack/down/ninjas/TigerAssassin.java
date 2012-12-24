package smack.down.ninjas;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class TigerAssassin extends Minion {

	public TigerAssassin() {
		super("Tiger Assassin", Faction.Ninjas, 4);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay("Select minion to destroy", true, new Callback.Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return (minion.getBase() == base) && minion.getStrength() <= 3;
			}
		});
		
		if (minion != null)
			minion.destroy();
	}
}
