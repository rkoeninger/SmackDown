package smack.down;

public abstract class Callback {
	public DeckCard selectFromHand(String message, boolean optional, Predicate<DeckCard> pred) {
		throw new UnsupportedOperationException("selectFromHand");
	}
	
	public DeckCard selectFromDiscard(String message, boolean optional, Predicate<DeckCard> pred) {
		throw new UnsupportedOperationException("selectFromDiscard");
	}
	
	public Base selectBaseInPlay(String message, boolean optional, Predicate<Base> pred) {
		throw new UnsupportedOperationException("selectBaseInPlay");
	}
	
	public Minion selectMinionInPlay(String message, boolean optional, Predicate<Minion> pred) {
		throw new UnsupportedOperationException("selectMinionInPlay");
	}
	
	public boolean confirm(String message) {
		throw new UnsupportedOperationException("confirm");
	}
	
	public interface Predicate<T> {
		public boolean eval(T arg);
	}
	
	public static <T> Predicate<T> truePredicate(T[] type) {
		return new Predicate<T>() {
			public boolean eval(T arg) {
				return true;
			}
		};
	}
	
	public static Predicate<DeckCard> minionPredicate() {
		return new Predicate<DeckCard>() {
			public boolean eval(DeckCard card) {
				return card instanceof Minion;
			}
		};
	}

	public static Predicate<DeckCard> minionPredicate(final int strengthLimit) {
		return new Predicate<DeckCard>() {
			public boolean eval(DeckCard card) {
				return (card instanceof Minion) && (((Minion) card).getStrength() <= strengthLimit);
			}
		};
	}
	
	public static Predicate<DeckCard> actionPredicate() {
		return new Predicate<DeckCard>() {
			public boolean eval(DeckCard card) {
				return card instanceof Action;
			}
		};
	}
}
