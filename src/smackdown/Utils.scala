package smackdown

import scala.language.higherKinds
import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.reflect.Manifest

object Utils {
  implicit def castWhatever(whatever: Any) = new {
    def is[T](implicit typeT: Manifest[T]) = typeT.runtimeClass.isInstance(whatever)
    def as[T] = whatever.asInstanceOf[T]
    def optionCast[U](implicit typeU: Manifest[U]): Option[U] = if (is[U]) Some(as[U]) else None
  }
  
  implicit def list2EnhancedList[T](list: List[T]) = new {
    def dropIndex[T](n: Int) =
      if (n < 0 || n >= list.length) list
      else {
        val (a, b) = list splitAt n
        a ::: (b drop 1)
      }
  }
  
  implicit def int2Awesome(i: Int) = new {
    def times[T](todo: => T) = if (i <= 0) List[T]() else (1 to i map(_ => todo)).toList
    def of[A <: DeckCard](implicit m: Manifest[A]) = (owner: Player) =>
      if (i <= 0) Set[DeckCard]()
      else {
        val ctor = m.runtimeClass.getConstructor(classOf[Player]);
        (1 to i).map(_ => ctor.newInstance(owner).as[DeckCard]).toSet
      }
  }
  
  implicit def deckCardSetEnhance(cards: Set[DeckCard]) = new {
    def minions = cards.filter(_.is[Minion]).map(_.as[Minion])
    def actions = cards.filter(_.is[Action]).map(_.as[Action])
  }
  
  implicit def deckCardListEnhance(cards: List[DeckCard]) = new {
    def minions = cards.filter(_.is[Minion]).map(_.as[Minion])
    def actions = cards.filter(_.is[Action]).map(_.as[Action])
  }
  
  implicit def monadOverDeckCard[F[_]](m: MapFilterable[DeckCard, F]) = new {
//    def minions: F[Minion] = m.filter(_.is[Minion]).map(_.as[Minion])
  }
  
  type MapFilterable[T, F[_]] = {
    def filter(f: T => Boolean): F[T]
    def map[U](f: T => U): F[U]
  }
  
  type Filterable[T, F[_]] = { def filter(f: T => Boolean): F[T] }
  
  implicit def choiceOverPlayer(c: Choice[Player]) = new {
    def otherThanMe = c.filter(_ != c.me)
  }
  
  implicit def choiceOverMinion(c: Choice[Minion]) = new {
    def mine = c.filter(_.owner == c.me)
    def notMine = c.filter(_.owner != c.me)
  }
  
  implicit def monadOverMinion[F[_]](m: Filterable[Minion, F]) = new {
    def powerAtMost(power: Int) = m.filter(_.power <= power)
    def destructable() = m.filter(_.destructable)
    def ownedBy(owner: Player) = m.filter(_.owner == owner)
    def notOwnedBy(owner: Player) = m.filter(_.owner != owner)
    def ofFaction(faction: Faction) = m.filter(_.faction == faction)
  }
  
  implicit def monadOverBase[F[_]](m: Filterable[Base, F]) = new {
    def otherThan(base: Base) = m.filter(_ != base)
    def withMinion(predicate: Minion => Boolean) = m.filter(_.minions.exists(predicate))
  }
}