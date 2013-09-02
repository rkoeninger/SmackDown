package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import Utils._

object Ninjas extends Faction("Ninjas") {
  override def bases(table: Table) = Set[Base](new TempleOfGoju(table), new NinjaDojo(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[NinjaAcolyte],
    3.of[Shinobi],
    2.of[TigerAssassin],
    1.of[NinjaMaster],
    2.of[SeeingStars],
    2.of[Infiltrate],
    1.of[Poison],
    1.of[Assassination],
    1.of[WayOfDeception],
    1.of[HiddenNinja],
    1.of[Disguise],
    1.of[SmokeBomb])
}

class TempleOfGoju(table: Table) extends Base("Temple of Goju", Ninjas, 18, (2, 3, 2), table) {
  // After this base scores, place each player's highest powered minion here
  // on the bottom of its owner's draw pile.
  override def afterScore(newBase: Base) {
    for (p <- minions.groupBy(_.owner).map(_._1)) {
      val highestPower = minions.ownedBy(p).map(_.power).max
      for (m <- minions.ownedBy(p).filter(_.power == highestPower))
        m moveTo DrawBottom // TODO: What if there's a tie for highest-powered?
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
  override def play(base: Base) = Ability {
    for (m <- owner.choose.minion.onBase(base).powerAtMost(3))
      m.destroyBy(owner)
  }
}

class NinjaMaster(owner: Player) extends Minion("Ninja Master", Ninjas, 5, owner) {
  // You may destory a minion on this base.
  override def play(base: Base) = Ability {
    for (m <- owner.choose.minion.onBase(base))
      m.destroyBy(owner)
  }
}

class SeeingStars(owner: Player) extends Action("Seeing Stars", Ninjas, owner) {
  // Destroy a minion of power 3 or less.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay.powerAtMost(3))
      m.destroyBy(user)
  }
}

class WayOfDeception(owner: Player) extends Action("Way of Deception", Ninjas, owner) {
  // Move one of your minions to a different base.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay.mine;
         b0 <- m.base;
         b1 <- user.choose.base.inPlay.otherThan(b0))
      m moveTo b1
  }
}

class HiddenNinja(owner: Player) extends Action("Hidden Ninja", Ninjas, owner) {
  // Special: Before a base scores, play a minion there.
  override def beforeScore(base: Base) = Ability {
    owner.playMinion(base)
  }
}

class Assassination(owner: Player) extends Action("Assassination", Ninjas, owner) {
  // Play on a minion. Ongoing: Destroy this minion at end of turn.
  override def play(user: Player) = Ability {
    for (m <- user.choose.minion.inPlay)
      table.currentPlayer.onTurnEnd { m.destroyBy(user) }
  }
}

class Disguise(owner: Player) extends Action("Disguise", Ninjas, owner) {
  // choose one or two of your minions on one base
  // play an equal number of extra minions there
  // and return the choosen minions to your hand
}

class SmokeBomb(owner: Player) extends Action("Smoke Bomb", Ninjas, owner) {
  // play on one of your minions. ongoing: this minion is not effected
  // by other player's actions. destroy this card at the start of your turn
}

class Poison(owner: Player) extends Action("Poison", Ninjas, owner) {
  // play on a minion. ongoing: minion has -4 power (but not less than 0)
  // destroy any actions on this minion
}

class Infiltrate(owner: Player) extends Action("Infiltrate", Ninjas, owner) {
  // play on a base. destroy an action that has been played here
  // ongoing: you may ignore this base's ability until the start of your next turn
  // TODO: put this action in discard at start of next turn?
}