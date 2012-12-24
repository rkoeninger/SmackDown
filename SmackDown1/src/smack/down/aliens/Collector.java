package smack.down.aliens;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class Collector extends Minion {

	public Collector() {
		super("Collector", Faction.Aliens, 2);
	}
	
	public void play(final Base base, Callback callback) {
		Minion minion = callback.selectMinionInPlay("Select minion to bounce", true, new Callback.Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return (base == minion.getBase()) && (minion.getStrength() <= 3);
			}
		});
		
		if (minion != null)
			minion.returnToHand();
	}
}
