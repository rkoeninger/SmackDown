package smack.down.effects;

import smack.down.Effect;
import smack.down.Minion;

public class StrengthEffect implements Effect {
	private Minion minion;
	private int amount;
	
	public StrengthEffect(Minion minion, int amount) {
		this.minion = minion;
		this.amount = amount;
	}
	
	public Minion getMinion() {
		return minion;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void expire() {
		minion.removeEffect(this);
	}
}
