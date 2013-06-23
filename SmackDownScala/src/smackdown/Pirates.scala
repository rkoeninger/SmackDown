package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import Utils._

object Pirates extends Faction("Pirates") {
  override def bases(table: Table) = Set(new Tortuga(table), new GreyOpal(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[FirstMate],
    3.of[SaucyWench],
    2.of[Buccaneer],
    1.of[PirateKing],
    2.of[Broadside],
    2.of[Dinghy],
    1.of[Shanghai],
    1.of[Cannon],
    1.of[FullSail],
    1.of[Powderkeg],
    1.of[Swashbuckling],
    1.of[SeaDogs])
}

class Tortuga(table: Table) extends Base("Tortuga", Pirates, 21, (4, 3, 2), table) {
  // The runner-up may move one of his or her minions to the base that replaces this base.
  override def afterScore(newBase: Base) {
    for (p <- score.filter(_.runnerUp).map(_.player).filter(p => minions.ownedBy(p).any);
         m <- p.chooseMyMinionOnBase(this))
      m --> newBase
  }
}

class GreyOpal(table: Table) extends Base("The Grey Opal", Pirates, 17, (3, 1, 1), table) {
  // Everyone on this base other than the winner
  // may move a minion to another base instead of to the discard pile.
  override def onScore() {
    for (p <- score.filter(! _.winner).map(_.player);
         m <- p.chooseMyMinionOnBase(this);
         b <- p.chooseOtherBaseInPlay(this))
      m --> b
  }
}

class FirstMate(owner: Player) extends Minion("First Mate", Pirates, 2, owner) {
  // Special: After this base is scored,
  // you may move this minion to another base instead of to the discard pile.
  override def afterScore(base: Base, newBase: Base) {
    if (this.base == Some(base))
      for (b <- owner.chooseOtherBaseInPlay(base))
        this --> b
  }
}

class SaucyWench(owner: Player) extends Minion("Saucy Wench", Pirates, 3, owner) {
  // You may destory a minion power 2 or less on this base.
  override def play(base: Base) {
    for (m <- owner.chooseMinionOnBase(base, 2))
      m.destroy(owner)
  }
}

class Buccaneer(owner: Player) extends Minion("Buccaneer", Pirates, 4, owner) {
  // Special: If this minion would be destroyed, move it to another base instead.
  override def destroy(destroyer: Player) {
    for (b0 <- base;
         b1 <- owner.chooseOtherBaseInPlay(b0))
      this --> b1
  }
}

class PirateKing(owner: Player) extends Minion("Pirate King", Pirates, 5, owner) {
  // Special: Before a base scores, you may move this minion there.
  override def beforeScore(base: Base) {
    if (isOnTable && Some(base) != this.base)
      if (owner.chooseYesNo)
        this --> base
  }
}

class Broadside(owner: Player) extends Action("Broadside", Pirates, owner) {
  // Destroy all of one player's minions power 2 or less on a base where you have a minion.
  override def play(user: Player) {
    for (b <- user.callback.choose(table.basesInPlay.filter(_.minions.exists(_.owner == user)));
         p <- user.choosePlayer;
         m <- b.minions.ownedBy(p).maxStrength(2))
      m.destroy(user)
  }
}

class Cannon(owner: Player) extends Action("Cannon", Pirates, owner) {
  // Destroy up to two minions of power 2 or less.
  override def play(user: Player) {
    2 times {
      for (m <- user.chooseMinionInPlay(2))
        m.destroy(user)
    }
  }
}

class Dinghy(owner: Player) extends Action("Dinghy", Pirates, owner) {
  // Move up to two of your minions to other bases.
  override def play(user: Player) {
    2 times {
      for (m <- user.chooseMyMinionInPlay;
           b0 <- m.base;
           b1 <- user.chooseOtherBaseInPlay(b0))
        m --> b1
    }
  }
}

class FullSail(owner: Player) extends Action("Full Sail", Pirates, owner) {
  // Move any number of your minions to other bases.
  // Special: Before a base scores, you may play this card.
  override def play(user: Player) {
      for (m <- user.chooseMyMinionInPlay;
           b0 <- m.base;
           b1 <- user.chooseOtherBaseInPlay(b0)) {
        m --> b1
        play(user)
      }
  }
  override def beforeScore(base: Base) {
    if (isInHand && owner.chooseYesNo) play(owner)
  }
}

class Powderkeg(owner: Player) extends Action("Powderkeg", Pirates, owner) {
  // Destroy one of your minions,
  // also destroy all minions on that base with power less than/equal to your minion.
  override def play(user: Player) {
    for (m <- user.chooseMyMinionInPlay;
         b <- m.base) {
      val strength = m.strength
      m.destroy(user)
      b.minions.destructable.maxStrength(strength).foreach(_.destroy(user))
    }
  }
}

class SeaDogs(owner: Player) extends Action("Sea Dogs", Pirates, owner) {
  // Name a faction. Move all minions of that faction from one base to another.
  override def play(user: Player) {
    for (f <- user.chooseFaction;
         b0 <- user.chooseBaseInPlay;
         b1 <- user.chooseOtherBaseInPlay(b0);
         m <- b0.minions.ofFaction(f))
      m --> b1
  }
}

class Shanghai(owner: Player) extends Action("Shanghai", Pirates, owner) {
  // Move an opponent's minion to another base.
  override def play(user: Player) {
    for (m <- user.chooseTheirMinionInPlay;
         b0 <- m.base;
         b1 <- user.chooseOtherBaseInPlay(b0))
      m --> b1
  }
}

class Swashbuckling(owner: Player) extends Action("Swashbuckling", Pirates, owner) {
  // Each of your minions gains +1 until end of turn.
  override def play(user: Player) {
    Bonus.untilTurnEnd(user, 1)
  }
}