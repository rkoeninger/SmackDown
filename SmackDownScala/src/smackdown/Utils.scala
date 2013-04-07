package smackdown

import scala.reflect.Manifest

object Utils {
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
  implicit def int2Loop(i: Int) = new {
    /** Ruby-style n.times {} method */
    def times[T](todo: => T) =
      if (i <= 0) List[T]()
      else 1 to i map(_ => todo)
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