package smackdown

import Utils._

object Pirates extends Faction("Pirates") {
  override def bases(table: Table) = List(new Tortuga(table), new GreyOpal(table))
  override def cards(owner: Player) = List(
    new FirstMate(owner), new FirstMate(owner), new FirstMate(owner), new FirstMate(owner),
    new SaucyWench(owner), new SaucyWench(owner), new SaucyWench(owner),
    new Buccaneer(owner), new Buccaneer(owner),
    new PirateKing(owner),
    new Broadside(owner), new Broadside(owner),
    new Dinghy(owner), new Dinghy(owner),
    new Shanghai(owner),
    new Cannon(owner),
    new FullSail(owner),
    new Powderkeg(owner),
    new Swashbuckling(owner),
    new SeaDogs(owner)
  )
}

class Tortuga(table: Table) extends Base("Tortuga", Pirates, 21, (4, 3, 2), table) {
  // The runner-up may move one of his or her minions to the base that replaces this base.
  override def afterScore(newBase: Base) {
    for (p <- score.filter(_.runnerUp).map(_.player).filter(p => minions.ownedBy(p).any);
         m <- p.callback.selectMinion(minions.ownedBy(p)))
      m.moveToBase(newBase)
  }
}

class GreyOpal(table: Table) extends Base("The Grey Opal", Pirates, 17, (3, 1, 1), table) {
  // Everyone on this base other than the winner
  // may move a minion to another base instead of to the discard pile.
  override def onScore() {
    for (p <- score.filter(! _.winner).map(_.player);
         m <- p.callback.selectMinion(minions.ownedBy(p));
         b <- p.callback.selectBase(_ != this))
      m.moveToBase(b)
  }
}

class FirstMate(owner: Player) extends Minion("First Mate", Pirates, 2, owner) {
  // Special: After this base is scored,
  // you may move this minion to another base instead of to the discard pile.
  override def afterScore(base: Base, newBase: Base) {
    if (this.base == Some(base))
      if (owner.callback.selectBoolean)
        moveToBase(newBase)
  }
}

class SaucyWench(owner: Player) extends Minion("Saucy Wench", Pirates, 3, owner) {
  // You may destory a minion power 2 or less on this base.
  override def play(base: Base) {
    for (m <- owner.callback.selectMinion(base.minions().destructable.maxStrength(2)))
      m.destroy(owner)
  }
}

class Buccaneer(owner: Player) extends Minion("Buccaneer", Pirates, 4, owner) {
  // Special: If this minion would be destroyed, move it to another base instead.
  override def destroy(destroyer: Player) {
    for (b <- owner.callback.selectBase(Some(_) != base))
      moveToBase(b)
  }
}

class PirateKing(owner: Player) extends Minion("Pirate King", Pirates, 5, owner) {
  // Special: Before a base scores, you may move this minion there.
  override def beforeScore(base: Base) {
    if (isOnTable && Some(base) != this.base)
      if (owner.callback.selectBoolean)
        moveToBase(base)
  }
}

class Broadside(owner: Player) extends Action("Broadside", Pirates, owner) {
  // Destroy all of one player's minions power 2 or less on a base where you have a minion.
  override def play(user: Player) {
    for (b <- user.callback.selectBase(_.minions.exists(_.owner == user));
         p <- user.callback.selectPlayer;
         m <- b.minions.destructable.ownedBy(p).maxStrength(2))
      m.destroy(user)
  }
}

class Cannon(owner: Player) extends Action("Cannon", Pirates, owner) {
  // Destroy up to two minions of power 2 or less.
  override def play(user: Player) {
    2 times {
      for (m <- user.callback.selectMinion(m => m.destructable && m.strength <= 2))
        m.destroy(user)
    }
  }
}

class Dinghy(owner: Player) extends Action("Dinghy", Pirates, owner) {
  // Move up to two of your minions to other bases.
  override def play(user: Player) {
    2 times {
      for (m <- user.callback.selectMinion(_.owner == user);
           b <- user.callback.selectBase(Some(_) != m.base))
        m.moveToBase(b)
    }
  }
}

class FullSail(owner: Player) extends Action("Full Sail", Pirates, owner) {
  // Move any number of your minions to other bases.
  // Special: Before a base scores, you may play this card.
  override def play(user: Player) {
    while (true) {
      // FIXME: Infinite loop - how to propogate Cancel/Done choice?
      for (m <- user.callback.selectMinion(_.owner == user);
           b <- user.callback.selectBase(Some(_) != m.base))
        m.moveToBase(b)
    }
  }
  override def beforeScore(base: Base) {
    if (isInHand && owner.callback.selectBoolean) play(owner)
  }
}

class Powderkeg(owner: Player) extends Action("Powderkeg", Pirates, owner) {
  // Destroy one of your minions,
  // also destroy all minions on that base with power less than/equal to your minion.
  override def play(user: Player) {
    for (m <- user.callback.selectMinion(_.owner == user);
         b <- m.base) {
      val strength = m.strength
      m.destroy(user)
      b.minions.destructable.maxStrength(strength).foreach(_.destroy(user))
    }
  }
}

class SeaDogs(owner: Player) extends Action("Sea Dogs", Pirates, owner) {
  // Name a faction. Move all minions of that faction to another base.
  override def play(user: Player) {
    for (f <- user.callback.selectFaction;
         b <- user.callback.selectBase;
         m <- table.minions.filter(_.faction == f))
      m.moveToBase(b)
  }
}

class Shanghai(owner: Player) extends Action("Shanghai", Pirates, owner) {
  // Move an opponent's minion to another base.
  override def play(user: Player) {
    for (m <- user.callback.selectMinion(m => m.base.isDefined && m.owner != user);
         b <- user.callback.selectBase(Some(_) != m.base))
      m.moveToBase(b)
  }
}

class Swashbuckling(owner: Player) extends Action("Swashbuckling", Pirates, owner) {
  // Each of your minions gains +1 until end of turn.
  override def play(user: Player) {
    Bonus.untilTurnEnd(user, 1)
  }
}