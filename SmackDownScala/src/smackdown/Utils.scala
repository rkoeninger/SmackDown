package smackdown

import scala.reflect.Manifest

object Deck {
  def apply(cs: (Int, () => DeckCard)*) = cs.flatMap(x => Utils.int2Awesome(x._1).times(x._2())).toSet
  def apply(owner: Player, cs: (Player => Set[DeckCard])*) = cs.flatMap(x => x(owner)).toSet
}

object Utils {
  implicit def byNameToNoArg[A](a: => A) = () => a
  implicit def anyAsOption(whatever: Any) = new {
    def optionCast[U](implicit m: Manifest[U]): Option[U] = if (simpleIsAs(whatever).is[U]) Some(whatever.as[U]) else None
  }
  implicit def simpleIsAs(any: Any) = new {
    /** Equivalent to Any.isInstanceOf[T] */
    def is[T](implicit m: Manifest[T]) = m.erasure.isInstance(any)
    /** Equivalent to Any.asInstanceOf[T] */
    def as[T] = any.asInstanceOf[T]
  }
  implicit def list2EnhancedList[T](list: List[T]) = new {
    /** Filters and casts elements of list by the given element sub-type */
    def ofType[U](implicit m: Manifest[U]) = list.filter(_.is[U]).map(_.as[U])
    /** Filters elements of list by the given element sub-type */
    def filterType[U](implicit m: Manifest[U]) = list.filter(_.is[U])
    /** Casts elements of list by the given element sub-type */
    def cast[U](implicit m: Manifest[U]) = list.map(_.as[U])
    /** Returns new list missing the element at the given index */
    def dropIndex[T](n: Int) = 
      if (n < 0 || n >= list.length) list
      else {
        val (a, b) = list splitAt n
        a ::: (b drop 1)
      }
  }
  implicit def set2EnhancedSet[T](set: Set[T]) = new {
    /** Filters and casts elements of set by the given element sub-type */
    def ofType[U](implicit m: Manifest[U]) = set.filter(_.is[U]).map(_.as[U])
    /** Filters elements of set by the given element sub-type */
    def filterType[U](implicit m: Manifest[U]) = set.filter(_.is[U])
    /** Casts elements of list by the given element sub-type */
    def cast[U](implicit m: Manifest[U]) = set.map(_.as[U])
    def any = set.size > 0
  }
  implicit def int2Awesome(i: Int) = new {
    /** Ruby-style n.times {} method */
    def times[T](todo: => T): List[T] =
      if (i <= 0) List[T]()
      else (1 to i map(_ => todo)).toList
    
    def of[A <: DeckCard](implicit m: Manifest[A]): Player => Set[DeckCard] =
      (owner: Player) => {
        if (i <= 0)
          Set[DeckCard]()
        else
          (1 to i).map(_ =>
            m.erasure.getConstructor(classOf[Player]).newInstance(owner).asInstanceOf[DeckCard]
          ).toSet
      }
  }
  implicit def bool2Cool(b: Boolean) = new {
    def not() = ! b
  }
  implicit def minionSetEnhance(ms: Set[Minion]) = new {
    def destructable() = ms.filter(_.destructable)
    def ownedBy(p: Player) = ms.filter(_.owner == p)
    def notOwnedBy(p: Player) = ms.filter(_.owner != p)
    def maxStrength(max: Int) = ms.filter(_.strength <= max)
    def ofFaction(f: Faction) = ms.filter(_.faction == f)
  }
  implicit def deckCardSetEnhance(dcs: Set[DeckCard]) = new {
    def minions() = dcs.ofType[Minion]
    def actions() = dcs.ofType[Action]
  }
}