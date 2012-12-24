package smack.down;

public class Action extends DeckCard {
	public enum Target {
		Base, General, Minion
	}
	
	private Target target;
	
	public Action(String name, Faction faction, Target target) {
		super(name, faction);
		this.target = target;
	}
	
	public Target getTarget() {
		return target;
	}
	
	public void play(Minion minion) {
		if (target != Target.Minion)
			throw new UnsupportedOperationException("play on a minion");
	}
	
	public void play(Base base) {
		if (target != Target.Base)
			throw new UnsupportedOperationException("play on a base");
	}
	
	public void play() {
		if (target != Target.General)
			throw new UnsupportedOperationException("play on the table");
	}
}
