package smack.down;

import java.util.ArrayList;
import java.util.List;

public class Minion extends DeckCard {
	public static class StrengthBonus {
		private int amount;
		
		public StrengthBonus(int amount) {
			this.amount = amount;
		}
		
		public int getAmount() {
			return amount;
		}
	}
	
	private int strength;
	private Base base = null;
	private List<StrengthBonus> bonuses;
	private List<Effect> effects;
	
	public Minion(String name, Faction faction, int strength) {
		super(name, faction);
		this.strength = strength;
		this.bonuses = new ArrayList<StrengthBonus>();
		this.effects = new ArrayList<Effect>();
	}
	
	public int getStrength() {
		int totalBonus = 0;
		
		for (StrengthBonus bonus : bonuses)
			totalBonus += bonus.getAmount();
		
		return strength + totalBonus;
	}
	
	public Minion setOwner(Player owner) {
		return (Minion) super.setOwner(owner);
	}
	
	public void addEffect(Effect effect) {
		effects.add(effect);
	}
	
	public void removeEffect(Effect effect) {
		effects.remove(effect);
	}
	
	public void clear() {
		effects.clear();
		bonuses.clear();
	}
	
	public void addStrengthBonus(StrengthBonus bonus) {
		bonuses.add(bonus);
	}
	
	public void removeStrengthBonus(StrengthBonus bonus) {
		bonuses.remove(bonus);
	}
	
	public Minion setBase(Base base) {
		Base oldBase = this.base;
		this.base = base;
		
		if ((oldBase != null) && (oldBase.hasMinion(this)))
			oldBase.removeMinion(this);
		
		if ((base != null) && (! base.hasMinion(this)))
			base.addMinion(this);
		
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
