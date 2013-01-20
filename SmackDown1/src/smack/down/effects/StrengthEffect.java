package smack.down.effects;

import smack.down.Effect;
import smack.down.Minion;

public class StrengthEffect implements Effect {
	private Minion minion;
	private Minion.StrengthBonus bonus;
	
	public StrengthEffect(Minion minion, int amount) {
		this.minion = minion;
		this.bonus = new Minion.StrengthBonus(amount);
	}
	
	public Minion getMinion() {
		return minion;
	}
	
	public Minion.StrengthBonus getBonus() {
		return bonus;
	}
	
	public void expire() {
		minion.removeStrengthBonus(bonus);
		minion.removeEffect(this);
	}
}
