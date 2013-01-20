package smack.down;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import smack.down.moves.PlayAction;
import smack.down.moves.PlayMinion;

public class Player {
	private String name;
	private int points;
	private CardList drawPile;
	private CardSet discardPile;
	private CardSet hand;
	private Table table;
	private List<Move> moves;
	private List<Effect> effectsBeginTurnExpire;
	private List<Effect> effectsEndTurnExpire;
	
	public Player(String name, CardList drawPile, CardSet discardPile, CardSet hand) {
		this.name = name;
		this.points = 0;
		this.drawPile = drawPile;
		this.discardPile = discardPile;
		this.hand = hand;
		this.moves = new ArrayList<Move>();
		this.effectsBeginTurnExpire = new ArrayList<Effect>();
		this.effectsEndTurnExpire = new ArrayList<Effect>();
		
		for (DeckCard card : drawPile)
			card.setOwner(this);
		
		for (DeckCard card : discardPile)
			card.setOwner(this);
		
		for (DeckCard card : hand)
			card.setOwner(this);
	}
	
	public String getName() {
		return name;
	}
	
	public Player setTable(Table table) {
		this.table = table;
		return this;
	}
	
	public Table getTable() {
		return table;
	}
	
	public int getTurnOrder() {
		List<Player> allPlayers = table.getPlayers();
		
		for (int i = 0; i < allPlayers.size(); ++i)
			if (allPlayers.get(i) == this)
				return i;
		
		throw new RuntimeException("Player not at table");
	}
	
	public List<Player> getOtherPlayers() {
		List<Player> allPlayers = table.getPlayers();
		List<Player> otherPlayers = new ArrayList<Player>();
		int myTurnOrder = getTurnOrder();
		
		for (int i = myTurnOrder + 1; i < allPlayers.size(); ++i)
			otherPlayers.add(allPlayers.get(i));
		
		for (int i = 0; i < myTurnOrder; ++i)
			otherPlayers.add(allPlayers.get(i));
		
		return otherPlayers;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int addPoints(int amount) {
		points += amount;
		return points;
	}
	
	public Player clearMoves() {
		moves.clear();
		return this;
	}
	
	public Player addMove(Move move) {
		moves.add(move);
		return this;
	}
	
	public List<Move> getMoves() {
		return new ArrayList<Move>(moves);
	}
	
	public DeckCard draw() {
		if (drawPile.size() == 0) {
			if (discardPile.size() == 0)
				return null;
			drawPile.takeAndShuffle(discardPile);
		}
		return drawPile.peek().putInHand();
	}
	
	public List<DeckCard> draw(int amount) {
		List<DeckCard> cards = new ArrayList<DeckCard>(amount);
		
		while (amount > 0) {
			DeckCard drawnCard = draw();
			
			if (drawnCard == null)
				break;
			
			cards.add(drawnCard);
			amount--;
		}
		
		return cards;
	}
	
	public DeckCard peek() {
		if (drawPile.size() == 0) {
			if (discardPile.size() == 0)
				return null;
			drawPile.takeAndShuffle(discardPile);
		}
		return drawPile.peek();
	}
	
	public CardList getDrawPile() {
		return drawPile;
	}
	
	public CardSet getDiscardPile() {
		return discardPile;
	}
	
	public CardSet getHand() {
		return hand;
	}
	
	public void addToHand(DeckCard... cards) {
		for (DeckCard card : cards)
			hand.add(card);
	}
	
	public void addToDiscard(DeckCard... cards) {
		for (DeckCard card : cards)
			discardPile.add(card);
	}
	
	public void removeFromDiscard(DeckCard... cards) {
		for (DeckCard card : cards)
			discardPile.remove(card);
	}
	
	public void discardRandomCard() {
		if (hand.size() > 0) {
			Random rand = new Random();
			int cardIndex = rand.nextInt(hand.size());
			Iterator<DeckCard> itr = hand.iterator();
			DeckCard card = itr.next();
			
			for (int i = 0; (i < cardIndex && itr.hasNext()); ++i)
				card = itr.next();
			
			card.discard();
		}
	}
	
	public void expireAtNextTurnBegin(Effect effect) {
		effectsBeginTurnExpire.add(effect);
	}
	
	public void expireAtNextTurnEnd(Effect effect) {
		effectsEndTurnExpire.add(effect);
	}
	
	public void playMove(Move move, Callback callback) {
		move.play(this, callback);
	}
	
	public void beginTurn() {
		moves.add(new PlayMinion());
		moves.add(new PlayAction());
		
		for (Effect effect : effectsBeginTurnExpire)
			effect.expire();
		
		effectsBeginTurnExpire.clear();
	}
	
	public void endTurn(Callback callback) {
		moves.clear();
		draw(2);
		
		while (hand.size() > 10) {
			DeckCard card = callback.selectCardFromHand("Discard down to 10", false, Callback.truePredicate(new DeckCard[0]));
			hand.remove(card);
			addToDiscard(card);
		}
		
		for (Effect effect : effectsEndTurnExpire)
			effect.expire();
		
		effectsEndTurnExpire.clear();
	}
}
