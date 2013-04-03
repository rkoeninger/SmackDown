package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Table}
import org.scalatest.FunSpec

class TestCallback extends Callback {
  def selectBase(predicate: Base => Boolean): Option[Base] = None
  def selectMinion(predicate: Minion => Boolean): Option[Minion] = None
  def selectAction(predicate: Action => Boolean): Option[Action] = None
  def selectFaction(): Option[Faction] = None
  def selectPlayer(predicate: Player => Boolean): Option[Player] = None
  def selectFromHand(predicate: DeckCard => Boolean): Option[DeckCard] = None
  def selectBoolean(): Boolean = false
}

object TestFaction extends Faction("TestFaction") {
  def bases(table: Table) = List[Base]()
  def cards(owner: Player) = List[DeckCard]()
}

class TestBase(table: Table) extends Base("TestBase", TestFaction, 1, (1, 2, 3), table) // 1 for 1st place points, 2 for 2nd, etc

class TestMinion(owner: Player, strength: Int) extends Minion("TestMinion", TestFaction, strength, owner)