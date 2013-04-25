package smackdown

import Utils._

object Zombies extends Faction("Zombies") {
  override def bases(table: Table) = Set[Base](new RhodesPlazaMall(table), new EvansCityCemetery(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Walker],
    3.of[TenaciousZ],
    2.of[GraveDigger],
    1.of[ZombieLord],
    2.of[GraveRobbing],
    2.of[TheyKeepComing],
    1.of[LendAHand],
    1.of[TheyreComingToGetYou],
    1.of[MallCrawl],
    1.of[NotEnoughBullets],
    1.of[Overrun],
    1.of[Outbreak])
}

class RhodesPlazaMall(table: Table) extends Base("Rhodes Plaza Mall", Zombies, 24, (0, 0, 0), table) {
  // When this base scores, each player gets +1 point for each minion they have here.
  override def afterScore(newBase: Base) {
    for (p <- minions.map(_.owner))
      p.points += minions.count(_.owner == p)
  }
}

class EvansCityCemetery(table: Table) extends Base("Evans City Cemetery", Zombies, 20, (5, 3, 2), table) {
  // The winner discards their hand and draws 5 cards.
  override def afterScore(newBase: Base) {
    for (p <- score.filter(_.winner).map(_.player)) {
      for (c <- p.hand) c --> Discard
      p.draw(5)
    }
  }
}

class Walker(owner: Player) extends Minion("Walker", Zombies, 2, owner) {
  // Look at the top card of your deck, you may place it in your discard pile
  override def play(base: Base) {
    for (c <- owner.peek)
      if (owner.chooseYesNo)
        c --> Discard
  }
}

class TenaciousZ(owner: Player) extends Minion("Tenacious Z", Zombies, 2, owner)
// May be played as an extra minion from the discard. Only one Ten-Z may be played this way per turn
// TODO: need onTurnBegin event so Ten-Z can add Play Ten-Z to player's move list

class GraveDigger(owner: Player) extends Minion("Grave Digger", Zombies, 4, owner) {
  // You may place a minion from your discard into your hand.
  override def play(base: Base) {
    for (m <- owner.callback.choose(owner.discardPile.minions))
      m --> Hand
  }
}

class ZombieLord(owner: Player) extends Minion("Zombie Lord", Zombies, 5, owner)
// You may play an extra minion from your discard on each base where you have no minion

class LendAHand(owner: Player) extends Action("Lend a Hand", Zombies, owner) {
  // Suffle any number of cards from your discard into your deck
}

class TheyreComingToGetYou(owner: Player) extends Action("They're Coming to Get You", Zombies, owner) {
  // Play on a base. Ongoing: you may play minions on this base from
  // your discard (this doesn't give you an extra minion)
}

class MallCrawl(owner: Player) extends Action("Mall Crawl", Zombies, owner) {
  // Select a minion from your deck, take all copies of that minion and put them in your discard.
  // Shuffle your deck.
  override def play(user: Player) {
    for (m0 <- user.callback.choose(user.drawPile.ofType[Minion].toSet);
         m <- user.drawPile.filter(_.getClass == m0.getClass)) {
      m --> Discard
      user.shuffle
    }
  }
}

class GraveRobbing(owner: Player) extends Action("Grave Robbing", Zombies, owner) {
  // Place a card from your discard into your hand.
  override def play(user: Player) {
    for (c <- user.callback.choose(user.discardPile))
      c --> Hand
  }
}

class TheyKeepComing(owner: Player) extends Action("They Keep Coming", Zombies, owner) {
  // You may play a minion from your discard as an extra minion.
  override def play(user: Player) {
    for (m <- user.callback.choose(user.discardPile.minions))
      user.playMinion(m)
  }
}

class NotEnoughBullets(owner: Player) extends Action("Not Enough Bullets", Zombies, owner) {
  // Select a minion from your discard, take all copies of that minion and put them into your hand.
  override def play(user: Player) {
    for (m0 <- user.callback.choose(user.discardPile.ofType[Minion].toSet);
         m <- user.discardPile.filter(_.getClass == m0.getClass))
      m --> Hand
  }
}

class Overrun(owner: Player) extends Action("Overrun", Zombies, owner) {
  // Play on a base. Ongoing: Other players cannot play minions on this base
  // destroy this card at the beginning of your next turn
}

class Outbreak(owner: Player) extends Action("Outbreak", Zombies, owner) {
  // play an extra minion on a base where you have no minions
}