package smack.down;

import java.util.ArrayList;
import java.util.List;

import smack.down.effects.StrengthEffect;

public class Minion extends DeckCard {
	private int strength;
	private Base base = null;
	private List<Effect> effects;
	
	public Minion(String name, Faction faction, int strength) {
		super(name, faction);
		this.strength = strength;
		this.effects = new ArrayList<Effect>();
	}
	
	public int getStrength() {
		int bonus = 0;
		
		for (Effect effect : effects)
			if (effect instanceof StrengthEffect)
				bonus += ((StrengthEffect) effect).getAmount();
		
		return strength + bonus;
	}
	
	public void addEffect(Effect effect) {
		effects.add(effect);
	}
	
	public void removeEffect(Effect effect) {
		effects.remove(effect);
	}
	
	public void clear() {
		effects.clear();
	}
	
	public Minion setBase(Base base) {
		this.base = base;
		return this;
	}
	
	public Base getBase() {
		return base;
	}
	
	public boolean isOnBase() {
		return base != null;
	}
	
	public void play(Base base, Callback callback) {
		
	}
	
	public void expireEffects() {
		for (Effect effect : effects)
			effect.expire();
	}
	
	public void returnToHand() {
		base.removeMinion(this);
		setBase(null);
		expireEffects();
		getOwner().addToHand(this);
	}
	
	public void destroy() {
		getOwner().addToDiscard(this);
		base.removeMinion(this);
		setBase(null);
		expireEffects();
		destroyed();
	}
	
	public void destroyed() {
		
	}
	
	public void scoring() {
		
	}
	
	public void anyScoring(Base base) {
		
	}
	
	public void scored() {
		
	}
}
