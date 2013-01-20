package smack.down;

import smack.down.effects.StrengthEffect;

public class EffectFactory {
	public static Effect applyStrengthBonus(Minion minion, int amount) {
		StrengthEffect effect = new StrengthEffect(minion, amount);
		minion.addEffect(effect);
		minion.addStrengthBonus(effect.getBonus());
		return effect;
	}
}
