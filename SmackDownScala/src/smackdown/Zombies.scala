package smackdown

import Utils._

object Zombies extends Faction("Zombies") {
  override def bases(table: Table) = List[Base](new RhodesPlazaMall(table), new EvansCityCemetery(table))
  override def cards(owner: Player) = List[DeckCard](
    new Walker(owner), new Walker(owner), new Walker(owner), new Walker(owner),
    new TenaciousZ(owner), new TenaciousZ(owner), new TenaciousZ(owner),
    new GraveDigger(owner), new GraveDigger(owner),
    new ZombieLord(owner),
    new LendAHand(owner),
    new TheyreComingToGetYou(owner),
    new MallCrawl(owner),
    new GraveRobbing(owner), new GraveRobbing(owner),
    new TheyKeepComing(owner), new TheyKeepComing(owner),
    new NotEnoughBullets(owner),
    new Overrun(owner),
    new Outbreak(owner))
}

class RhodesPlazaMall(table: Table) extends Base("Rhodes Plaza Mall", Zombies, 24, (0, 0, 0), table) {
  // When this base scores, each player gets +1 point for each minion they have here
  override def afterScore(newBase: Base) {
    minions.groupBy(_.owner).map(x => x._1.points += x._2.size)
  }
}

class EvansCityCemetery(table: Table) extends Base("Evans City Cemetery", Zombies, 20, (5, 3, 2), table) {
  // The winner discards their hand and draws 5 cards
  override def afterScore(newBase: Base) {
    score.filter(_._2._2 == 1).map(_._1).foreach(winner => {
      winner.hand.foreach(_.moveToDiscard)
      winner.draw(5)
    })
  }
}

class Walker(owner: Player) extends Minion("Walker", Zombies, 2, owner) {
  // Look at the top card of your deck, you may place it in your discard pile
  // TODO: should reveal to all players or only to owner?
  override def play(base: Base) {
    owner.reveal.foreach(card => if (owner.callback.selectBoolean) card.moveToDiscard)
  }
}

class TenaciousZ(owner: Player) extends Minion("Tenacious Z", Zombies, 2, owner)
// May be played as an extra minion from the discard. Only one Ten-Z may be played this way per turn
// TODO: need onTurnBegin event so Ten-Z can add Play Ten-Z to player's move list

class GraveDigger(owner: Player) extends Minion("Grave Digger", Zombies, 4, owner) {
  // You may place a minion from your discard into your hand
  override def play(base: Base) {
    owner.callback.select(owner.discardPile.filterType[Minion]).foreach(_.moveToHand)
  }
}

class ZombieLord(owner: Player) extends Minion("Zombie Lord", Zombies, 5, owner)
// You may play an extra minion from your discard on each base where you have no minion

// TODO: is it "Lend a Hand" or "Fresh Bodies"?
class LendAHand(owner: Player) extends Action("Lend a Hand", Zombies, owner) {
  // Suffle any number of cards from your discard into your deck
}

class TheyreComingToGetYou(owner: Player) extends Action("They're Coming to Get You", Zombies, owner) {
  // Play on a base. Ongoing: you may play minions on this base from
  // your discard (this doesn't give you an extra minion)
}

class MallCrawl(owner: Player) extends Action("Mall Crawl", Zombies, owner) {
  // Select a minion from your deck, take all copies of that minion
  // and put them in your discard
  // shuffle your deck
  override def play(user: Player) {
    user.callback.select(user.drawPile.filterType[Minion]).foreach(m => {
      user.drawPile.filter(_.getClass == m.getClass).foreach(_.moveToHand)
    })
  }
}

class GraveRobbing(owner: Player) extends Action("Grave Robbing", Zombies, owner) {
  // Place a card from your discard into your hand
  override def play(user: Player) {
    user.callback.select(user.discardPile).foreach(_.moveToHand)
  }
}

class TheyKeepComing(owner: Player) extends Action("They Keep Coming", Zombies, owner) {
  // you may play a minion from your discard as an extra minion 
}

class NotEnoughBullets(owner: Player) extends Action("Not Enough Bullets", Zombies, owner) {
  // select a minion from your discard, take all copies of that minion and put them into your hand
  override def play(user: Player) {
    user.callback.select(owner.discardPile.filterType[Minion]).foreach(m => {
      user.discardPile.filter(_.getClass == m.getClass).foreach(_.moveToHand)
    })
  }
}

class Overrun(owner: Player) extends Action("Overrun", Zombies, owner) {
  // Play on a base. Ongoing: Other players cannot play minions on this base
  // destroy this card at the beginning of your next turn
}

class Outbreak(owner: Player) extends Action("Outbreak", Zombies, owner) {
  // play an extra minion on a base where you have no minions
}