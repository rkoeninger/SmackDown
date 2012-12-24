package smack.down;

import java.util.ArrayList;
import java.util.List;

import smack.down.effects.StrengthEffect;

public class Minion extends DeckCard {
	private int strength;
	private Base base = null;
	private List<StrengthEffect> effects;
	
	public Minion(String name, Faction faction, int strength) {
		super(name, faction);
		this.strength = strength;
		this.effects = new ArrayList<StrengthEffect>();
	}
	
	public int getStrength() {
		int bonus = 0;
		
		for (StrengthEffect effect : effects) {
			bonus += effect.getAmount();
		}
		
		return strength + bonus;
	}
	
	public void addEffect(StrengthEffect effect) {
		effects.add(effect);
	}
	
	public void removeEffect(StrengthEffect effect) {
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
	
	public void returnToHand() {
		base.removeMinion(this);
		setBase(null);
		getOwner().addToHand(this);
	}
	
	public void destroy() {
		getOwner().addToDiscard(this);
		base.removeMinion(this);
		setBase(null);
		destroyed();
	}
	
	public void destroyed() {
		
	}
}
