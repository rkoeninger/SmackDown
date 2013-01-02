package smack.down.ninjas;

import smack.down.Action;
import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;

public class WayOfDeception extends Action {
	
	public WayOfDeception() {
		super("Way of Deception", Faction.Ninjas, Target.Minion);
	}
	
	public void play(final Minion minion, Callback callback) {
		Base base = callback.selectBaseInPlay("Select destination", true, new Callback.Predicate<Base>() {
			public boolean eval(Base arg) {
				return arg != minion.getBase();
			}
		});
		
		minion.getBase().removeMinion(minion);
		minion.setBase(base);
		base.addMinion(minion);
	}
}
