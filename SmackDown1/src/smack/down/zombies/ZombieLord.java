package smack.down.zombies;

import smack.down.Base;
import smack.down.Callback;
import smack.down.Faction;
import smack.down.Minion;
import smack.down.moves.PlayMinionOnBase;

public class ZombieLord extends Minion {
	
	public ZombieLord() {
		super("Zombie Lord", Faction.Zombies, 5);
	}
	
	public void play(Base base, Callback callback) {
		for (Base eachBase : getOwner().getTable().getBases()) {
			if (eachBase == base)
				continue;
			
			boolean anyMinions = false;
			
			for (Minion eachMinion : eachBase.getMinions())
				if (eachMinion.getOwner() == getOwner()) {
					anyMinions = true;
					break;
				}
			
			if (anyMinions)
				getOwner().addMove(new PlayMinionOnBase(eachBase, 2));
		}
	}
}
