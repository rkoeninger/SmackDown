package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

object Aliens extends Faction("Aliens") {
  override def bases(table: Table) = Set(new Homeworld(table), new Mothership(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Collector],
    3.of[Scout],
    2.of[Invader],
    1.of[SupremeOverlord],
    2.of[BeamUp],
    2.of[Disintegrator],
    1.of[CropCircles],
    1.of[Abduction],
    1.of[Probe],
    1.of[Terraforming],
    1.of[JammedSignal],
    1.of[Invasion])
}

class Homeworld(table: Table) extends Base("The Homeworld", Aliens, 23, (4, 2, 1), table) {
  // After each time a minion is played here,
  // its owner may play an extra minion of power 2 or less.
  override def minionPlayed(minion: Minion) { minion.owner.playMinion(2) }
}

class Mothership(table: Table) extends Base("The Mothership", Aliens, 20, (4, 2, 1), table) {
  // After this base scores, the winner may return one of their minions
  // power 3 or less from here to their hand.
  override def afterScore(newBase: Base) {
    for (p <- score.filter(_.winner).map(_.player);
         m <- p.choose.minion.onBase(this).mine.powerAtMost(3))
      m moveTo Hand
  }
}

class Collector(owner: Player) extends Minion("Collector", Aliens, 2, owner) {
  // You may return a minion power 3 or less on this base to its owner's hand.
  override def play(base: Base) = Ability {
    for (m <- owner.choose.minion.onBase(base).powerAtMost(3))
      m moveTo Hand
  }
}

class Scout(owner: Player) extends Minion("Scout", Aliens, 3, owner) {
  // Special: After this base is scored,
  // you may place this minion in your hand instead of the discard pile.
  override def afterScore(base: Base, newBase: Base) = Ability {
    if (owner.chooseYesNo)
      this moveTo Hand
  }
}

class Invader(owner: Player) extends Minion("Invader", Aliens, 3, owner) {
  // You gain +1 VP.
  override def play(base: Base) = Ability { owner addPoints 1 }
}

class SupremeOverlord(owner: Player) extends Minion("Supreme Overlord", Aliens, 5, owner) {
  // You may return a minion to its owner's hand.
  override def play(base: Base) = Ability {
    for (m <- owner.choose.minion.inPlay)
      m moveTo Hand
  }
}

class BeamUp(owner: Player) extends Action("Beam Up", Aliens, owner) {
  // Return a minion to its owner's hand.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay)
      m moveTo Hand
  }
}

class Invasion(owner: Player) extends Action("Invasion", Aliens, owner) {
  // Move a minion to another base.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay;
         b0 <- m.base;
         b1 <- user.choose.base.inPlay.otherThan(b0))
      m moveTo b1
  }
}

class Abduction(owner: Player) extends Action("Abduction", Aliens, owner) {
  // Return a minion to its owner's hand.
  // Play an extra minion.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay)
      m moveTo Hand
    user.playMinion
  }
}

class Disintegrator(owner: Player) extends Action("Disintegrator", Aliens, owner) {
  // Place a minion power 3 or less on the bottom of its owner's draw pile.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay.powerAtMost(3))
      m moveTo DrawBottom
  }
}

class CropCircles(owner: Player) extends Action("Crop Circles", Aliens, owner) {
  // Choose a base. Return each minion on that base to its owner's hand.
  override def play(user: Player) = Ability {
    for (b <- user.choose.base.inPlay;
         m <- b.minions)
      m moveTo Hand
  }
}

class Probe(owner: Player) extends Action("Probe", Aliens, owner) {
  // Look at an opponent's hand and choose a minion in it.
  // That player discards that minion.
  override def play(user: Player) = Ability {
    for (p <- user.choose.player.otherThanMe;
         m <- user.choose.minion.inHand(p))
      m moveTo Discard
  }
}

class Terraforming(owner: Player) extends Action("Terraforming", Aliens, owner){
  // Search the base deck for a base.
  // Swap it with a base in play (discard all actions attached to it).
  // Shuffle the base deck.
  // You may play an extra minion on the new base.
  override def play(user: Player) = Ability {
    for (b0 <- user.choose.base.inDeck;
         b1 <- user.choose.base.inPlay) {
      table.baseDrawPile = table.baseDrawPile.filterNot(_ == b0)
      table.basesInPlay -= b1
      table.baseDrawPile = b1 :: table.baseDrawPile
      table.basesInPlay += b0
      b0.cards = b1.cards
      b1.cards = Set()
      table.baseDrawPile = Random.shuffle(table.baseDrawPile)
      owner.playMinion(b0)
    }
  }
}

class JammedSignal(owner: Player) extends Action("Jammed Signal", Aliens, owner) {
  // play on a base. ongoing: all players ignore this base's ability
}