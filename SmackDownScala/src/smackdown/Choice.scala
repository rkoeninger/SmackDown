package smackdown

class Choice[T](me: Player, var options: Set[T]) {
  def decide = me.callback.choose(options)
  
  def foreach(f: T => Unit) { decide foreach f }
  def map[U](f: T => U) { decide map f }
  
  def filter(f: T => Boolean): this.type = {
    options = options filter f
    this
  }
  
  def otherThan(option: T) = filter(_ != option)
}

class PlayerChoice(me: Player, options: Set[Player]) extends Choice[Player](me, options) {
  def otherThanMe = otherThan(me)
}

class BaseChoice(me: Player, options: Set[Base]) extends Choice[Base](me, options) {
  def withMinion(f: Minion => Boolean) = filter(_.minions.exists(f))
  def withAction(f: Action => Boolean) = filter(_.actions.exists(f))
}

class ActionChoice(me: Player, options: Set[Action]) extends Choice[Action](me, options) {
  def onBase(base: Base) = filter(_.base == Some(base))
  def notOnBase(base: Base) = filter(_.base != Some(base))
  def onMinion(minion: Minion) = filter(_.minion == Some(minion))
  def notOnMinion(minion: Minion) = filter(_.minion != Some(minion))
  def ownedBy(owner: Player) = filter(_.owner == owner)
  def notOwnedBy(owner: Player) = filter(_.owner != owner)
  def mine = ownedBy(me)
  def notMine = notOwnedBy(me)
}

class MinionChoice(me: Player, options: Set[Minion]) extends Choice[Minion](me, options) {
  def strength(strength: Int) = filter(_.strength == strength)
  def strengthAtMost(strength: Int) = filter(_.strength <= strength)
  def strengthAtLeast(strength: Int) = filter(_.strength >= strength)
  def withAction(f: Action => Boolean) = filter(_.actions.exists(f))
  def onBase(base: Base) = filter(_.base == Some(base))
  def notOnBase(base: Base) = filter(_.base != Some(base))
  def ownedBy(owner: Player) = filter(_.owner == owner)
  def notOwnedBy(owner: Player) = filter(_.owner != owner)
  def mine = ownedBy(me)
  def notMine = notOwnedBy(me)
}