package smackdown

import Utils._

object Aliens extends Faction("Aliens") {
  override def bases(table: Table) = Set(new Homeworld(table), new Mothership(table))
  override def cards(owner: Player) = Set(
    new Collector(owner), new Collector(owner), new Collector(owner), new Collector(owner),
    new Scout(owner), new Scout(owner), new Scout(owner),
    new Invader(owner), new Invader(owner),
    new SupremeOverlord(owner)
  )
}

class Homeworld(table: Table) extends Base("The Homeworld", Aliens, 23, (4, 2, 1), table) {
  // after each time a minion is played here,
  // its owner my play an extra minion of power 2 or less
}

class Mothership(table: Table) extends Base("The Mothership", Aliens, 20, (4, 2, 1), table) {
  // After this base scores, the winner may return one of their minions
  // power 3 or less from here to their hand.
  override def afterScore(newBase: Base) {
    for (p <- score.filter(_.winner).map(_.player);
         m <- p.chooseMinionOnBase(this, 3))
      m.moveToHand
  }
}

class Collector(owner: Player) extends Minion("Collector", Aliens, 2, owner) {
  // You may return a minion power 3 or less on this base to its owner's hand.
  override def play(base: Base) {
    for (m <- owner.chooseMinionOnBase(base, 3))
      m.moveToHand
  }
}

class Scout(owner: Player) extends Minion("Scout", Aliens, 3, owner) {
  // Special: After this base is scored,
  // you may place this minion in your hand instead of the discard pile.
  override def afterScore(base: Base, newBase: Base) {
    if (owner.chooseYesNo)
      moveToHand
  }
}

class Invader(owner: Player) extends Minion("Invader", Aliens, 3, owner) {
  // You gain +1 VP.
  override def play(base: Base) { owner.points += 1 }
}

class SupremeOverlord(owner: Player) extends Minion("Supreme Overlord", Aliens, 5, owner) {
  // You may return a minion to its owner's hand.
  override def play(base: Base) {
    for (m <- owner.chooseMinionInPlay)
      m.moveToHand
  }
}

class BeamUp(owner: Player) extends Action("Beam Up", Aliens, owner) {
  // Return a minion to its owner's hand.
  override def play(user: Player) {
    for (m <- user.chooseMinionInPlay)
      m.moveToHand
  }
}

class Invasion(owner: Player) extends Action("Invasion", Aliens, owner) {
  // Move a minion to another base.
  override def play(user: Player) {
    for (m <- user.chooseMinionInPlay;
         b0 <- m.base;
         b1 <- user.chooseOtherBaseInPlay(b0))
      m.moveToBase(b1)
  }
}

class Abduction(owner: Player) extends Action("Abduction", Aliens, owner) {
  // Return a minion to its owner's hand.
  // Play an extra minion.
  override def play(user: Player) {
    for (m <- user.chooseMinionInPlay)
      m.moveToHand
    // TODO: Player/Callback method for playing a minion immediately
  }
}

class Disintegrator(owner: Player) extends Action("Disintegrator", Aliens, owner) {
  // Place a minion power 3 or less on the bottom of its owner's draw pile.
  override def play(user: Player) {
    for (m <- user.chooseMinionInPlay(3))
      m.moveToDrawPileBottom
  }
}

class CropCircles(owner: Player) extends Action("Crop Circles", Aliens, owner) {
  // Choose a base. Return each minion on that base to its owner's hand.
  override def play(user: Player) {
    for (b <- user.chooseBaseInPlay;
         m <- b.minions)
      m.moveToHand
  }
}

class Probe(owner: Player) extends Action("Probe", Aliens, owner) {
  // Look at an opponent's hand and choose a minion in it.
  // That player discards that minion.
  override def play(user: Player) {
    for (p <- user.chooseOtherPlayer;
         m <- user.chooseMinionInHand(p))
      m.moveToDiscard
  }
}

class JammedSignal(owner: Player) extends Action("Jammed Signal", Aliens, owner) {
  // play on a base. ongoing: all players ignore this base's ability
}

class Terraforming {
  // Search the base deck for a base.
  // Swap it with a base in play (discard all actions attached to it).
  // Shuffle the base deck.
  // You may play an extra minion on the new base.
}