package smackdown

import Utils._

object Ninjas extends Faction("Ninjas") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}

class TempleOfGoju(table: Table) extends Base("Temple of Goju", Ninjas, 18, (2, 3, 2), table) {
  // After this base scores, place each player's highest powered minion here
  // on the bottom of its owner's draw pile.
  override def afterScore(newBase: Base) {
    for (p <- minions.groupBy(_.owner).map(_._1)) {
      val highestStrength = minions.ownedBy(p).map(_.strength).max
      for (m <- minions.ownedBy(p).filter(_.strength == highestStrength))
        m.moveToDrawPileBottom // TODO: What if there's a tie for highest-powered?
    }
  }
}

class NinjaDojo(table: Table) extends Base("Ninja Dojo", Ninjas, 18, (2, 3, 2), table) {
  // after this base scores, the winner may destroy any 1 minion
  // TODO: including minions still on the base?
}

class NinjaAcolyte(owner: Player) extends Minion("Ninja Acolyte", Ninjas, 2, owner) {
  // special: on your turn, if you have not yet played a minion,
  // you may return this minion to your hand and play an extra minion on this base
}

class Shinobi(owner: Player) extends Minion("Shinobi", Ninjas, 3, owner) {
  // special: before a base scores, you may play this minion there.
  // you may only use a Shinobi's ability once per turn
}

class TigerAssassin(owner: Player) extends Minion("Tiger Assassin", Ninjas, 4, owner) {
  // You may destroy a minion power 3 or less on this base.
  override def play(base: Base) {
    for (m <- owner.callback.selectMinion(base.minions.destructable.maxStrength(3)))
      m.destroy(owner)
  }
}

class NinjaMaster(owner: Player) extends Minion("Ninja Master", Ninjas, 5, owner) {
  // You may destory a minion on this base.
  override def play(base: Base) {
    for (m <- owner.callback.selectMinion(base.minions.destructable))
      m.destroy(owner)
  }
}

class SeeingStars(owner: Player) extends Action("Seeing Stars", Ninjas, owner) {
  // Destroy a minion of power 3 or less.
  override def play(user: Player) {
    for (m <- user.callback.selectMinion(table.minions.destructable.maxStrength(3)))
      m.destroy(user)
  }
}

class WayOfDeception(owner: Player) extends Action("Way of Deception", Ninjas, owner) {
  // Move one of your minions to a different base.
  override def play(user: Player) {
    for (m <- user.callback.selectMinion(table.minions.ownedBy(user));
         b <- user.callback.selectBase(Some(_) != m.base))
      m.moveToBase(b)
  }
}

class Disguise(owner: Player) extends Action("Disguise", Ninjas, owner) {
  // choose one or two of your minions on one base
  // play an equal number of extra minions there
  // and return the choosen minions to your hand
}

class HiddenNinja(owner: Player) extends Action("Hidden Ninja", Ninjas, owner) {
  // special: before a base scores, play a minion there
}

class SmokeBomb(owner: Player) extends Action("Smoke Bomb", Ninjas, owner) {
  // play on one of your minions. ongoing: this minion is not effected
  // by other player's actions. destroy this card at the start of your turn
}

class Poison(owner: Player) extends Action("Poison", Ninjas, owner) {
  // play on a minion. ongoing: minion has -4 power (but not less than 0)
  // destroy any actions on this minion
}

class Assassination(owner: Player) extends Action("Assassination", Ninjas, owner) {
  // play on a minion. ongoing: destroy this minion at end of turn
}

class Infiltrate(owner: Player) extends Action("Infiltrate", Ninjas, owner) {
  // play on a base. destroy an action that has been played here
  // ongoing: you may ignore this base's ability until the start of your next turn
  // TODO: put this action in discard at start of next turn?
}