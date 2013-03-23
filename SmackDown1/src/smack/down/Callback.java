package smack.down;

public abstract class Callback {
	public DeckCard selectCardFromHand(String message, boolean optional, Predicate<DeckCard> pred) {
		throw new UnsupportedOperationException("selectFromHand");
	}
	
	public DeckCard selectFromDiscard(String message, boolean optional, Predicate<DeckCard> pred) {
		throw new UnsupportedOperationException("selectFromDiscard");
	}
	
	public Base selectBaseInPlay(String message, boolean optional, Predicate<Base> pred) {
		throw new UnsupportedOperationException("selectBaseInPlay");
	}
	
	public Minion selectMinionInPlay() {
		throw new UnsupportedOperationException("selectMinionInPlay()");
	}
	
	public Minion selectMinionInPlay(int strengthLimit) {
		throw new UnsupportedOperationException("selectMinionInPlay(int)");
	}
	
	public Minion selectMinionInPlay(Base base) {
		throw new UnsupportedOperationException("selectMinionInPlay(Base)");
	}
	
	public Minion selectMinionInPlay(Base base, int strengthLimit) {
		throw new UnsupportedOperationException("selectMinionInPlay(Base, int)");
	}
	
	public Class<? extends Minion> selectMinionTypeFromDiscard(String message, boolean optional) {
		throw new UnsupportedOperationException("selectMinionTypeFromDiscard");
	}
	
	public Integer selectAmount(String message, boolean optional, int max) {
		throw new UnsupportedOperationException("selectAmount");
	}
	
	public Faction selectFaction() {
		throw new UnsupportedOperationException("selectFaction");
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
	
	public static Predicate<Base> anyOtherBase(final Base base) {
		return new Predicate<Base>() {
			public boolean eval(Base arg) {
				return arg != base;
			}
		};
	}
	
	public static Predicate<Minion> minionStrengthPredicate(final int strengthLimit) {
		return new Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return minion.getStrength() <= strengthLimit;
			}
		};
	}
	
	public static Predicate<Minion> minionStrengthBasePrediate(final int strengthLimit, final Base base) {
		return new Predicate<Minion>() {
			public boolean eval(Minion minion) {
				return (minion.getStrength() <= strengthLimit) && (minion.getBase() == base);
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
