package smackdown

object Pirates extends Faction("Pirates") {
  override def bases(table: Table) = List(new Tortuga(table), new GreyOpal(table))
  override def cards(owner: Player) = List(
    new FirstMate(owner), new FirstMate(owner), new FirstMate(owner), new FirstMate(owner),
    new SaucyWench(owner), new SaucyWench(owner), new SaucyWench(owner),
    new Buccaneer(owner), new Buccaneer(owner),
    new PirateKing(owner),
    new Broadside(owner), new Broadside(owner),
    new Dingy(owner), new Dingy(owner),
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
    for (p <- score.filter(x => x._2._2 == 2 && minions.exists(_.owner == x._1)).map(_._1)) {
      p.callback.selectMinion(m => m.owner == p && m.base.isDefined && m.base.get == this).foreach(_.moveToBase(newBase))
    }
  }
}

class GreyOpal(table: Table) extends Base("The Grey Opal", Pirates, 17, (3, 1, 1), table) {
  // Everyone on this base other than the winner may move a minion to another base instead of to the discard pile.
  override def onScore() {
    for (p <- score.filter(_._2._2 != 1).map(_._1)) {
      val m = p.callback.selectMinion(m => m.base == this && m.owner == p)
      if (m.isDefined) {
        val b = p.callback.selectBase(_ != this)
        if (b.isDefined) {
          m.get.moveToBase(b.get)
        }
      }
    }
  }
}

class FirstMate(owner: Player) extends Minion("First Mate", Pirates, 2, owner) {
  // Special: After this base is scored, you may move this minion to another base instead of to the discard pile.
  override def afterScore(base: Base, newBase: Base) {
    if (isOnTable && base == this.base.get) {
      if (owner.callback.selectBoolean) {
        moveToBase(newBase)
      }
    }
  }
}

class SaucyWench(owner: Player) extends Minion("Saucy Wench", Pirates, 3, owner) {
  // You may destory a minion power 2 or less on this base.
  override def play(base: Base) {
    owner.callback.selectMinion(m => m.destructable && m.strength <= 2 && m.base == Some(base)).foreach(_.destroy(owner))
  }
}

class Buccaneer(owner: Player) extends Minion("Buccaneer", Pirates, 4, owner) {
  // Special: If this minion would be destroyed, move it to another base instead.
  override def destroy(destroyer: Player) {
    owner.callback.selectBase(Some(_) != base).foreach(moveToBase(_))
  }
}

class PirateKing(owner: Player) extends Minion("Pirate King", Pirates, 5, owner) {
  // Special: Before a base scores, you may move this minion there.
  override def beforeScore(base: Base) {
    if (isOnTable && Some(base) != this.base) {
      if (owner.callback.selectBoolean) {
        moveToBase(base)
      }
    }
  }
}

class Broadside(owner: Player) extends Action("Broadside", Pirates, owner) {
  // Destroy all of one player's minions power 2 or less on a base where you have a minion.
  override def play(user: Player) {
    val b = user.callback.selectBase(_.minions.exists(_.owner == user))
    if (b.isEmpty) return
    val p = user.callback.selectPlayer(p => true)
    if (p.isEmpty) return
    val ms = b.get.minions.filter(m => m.destructable && m.owner == p && m.strength <= 2)
    ms.foreach(_.destroy(user))
  }
}

class Cannon(owner: Player) extends Action("Cannon", Pirates, owner) {
  // Destroy up to two minions of power 2 or less.
  override def play(user: Player) {
    var minionsDestroyed = 0
    while (minionsDestroyed < 2) {
      val m = user.callback.selectMinion(_.owner == user)
      if (m.isEmpty) return
      m.filter(_.destructable).foreach(_.destroy(user))
      minionsDestroyed += 1
    }
  }
}

class Dingy(owner: Player) extends Action("Dingy", Pirates, owner) {
  // Move up to two of your minions to other bases.
  override def play(user: Player) {
    var minionsMoved = 0
    while (minionsMoved < 2) {
      val m = user.callback.selectMinion(_.owner == user)
      if (m.isEmpty) return
      val b = user.callback.selectBase(_ != m.get.base)
      if (b.isDefined) {
        m.get.moveToBase(b.get)
        minionsMoved += 1
      }
    }
  }
}

class FullSail(owner: Player) extends Action("Full Sail", Pirates, owner) {
  // Move any number of your minions to other bases. Special: Before a base scores, you may play this card.
  override def play(user: Player) {
    while (true) {
      val m = user.callback.selectMinion(_.owner == user)
      if (m.isEmpty) return
      val b = user.callback.selectBase(_ != m.get.base)
      if (b.isDefined)
        m.get.moveToBase(b.get)
    }
  }
  override def beforeScore(base: Base) {
    if (isInHand && owner.callback.selectBoolean) play(owner)
  }
}

class Powderkeg(owner: Player) extends Action("Powderkeg", Pirates, owner) {
  // Destroy one of your minions, also destroy all minions on that base with power less than/equal to your minion.
  override def play(user: Player) {
    val m = user.callback.selectMinion(_.owner == user)
    if (m.isEmpty) return
    val b = m.get.base
    val s = m.get.strength
    m.get.destroy(user)
    b.get.minions.filter(m => m.destructable && m.strength <= s).foreach(_.destroy(user))
  }
}

class SeaDogs(owner: Player) extends Action("Sea Dogs", Pirates, owner) {
  // Name a faction. Move all minions of that faction to another base.
  override def play(user: Player) {
    val b0 = user.callback.selectBase(x => true)
    if (b0.isEmpty) return
    val b1 = user.callback.selectBase(_ != b0)
    if (b1.isEmpty) return
    val f = user.callback.selectFaction()
    if (f.isEmpty) return
    b0.get.minions.filter(_.faction == f).foreach(_.moveToBase(b1.get))
  }
}

class Shanghai(owner: Player) extends Action("Shanghai", Pirates, owner) {
  // Move an opponent's minion to another base.
  override def play(user: Player) {
    val m = user.callback.selectMinion(m => m.base.isDefined && m.owner != user)
    if (m.isEmpty) return
    val b = user.callback.selectBase(_ != m.get.base)
    if (b.isEmpty) return
    m.get.moveToBase(b.get)
  }
}

class Swashbuckling(owner: Player) extends Action("Swashbuckling", Pirates, owner) {
  // Each of your minions gains +1 until end of turn.
  override def play(user: Player) {
    Bonus.untilTurnEnd(user, 1)
  }
}