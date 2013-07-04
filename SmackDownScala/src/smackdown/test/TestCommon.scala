package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Table}
import org.scalatest.FunSpec

class TestCallback extends Callback

object TestFaction extends Faction("TestFaction") {
  def bases(table: Table) = Set[Base]()
  def cards(owner: Player) = Set[DeckCard]()
}

class TestBase(table: Table) extends Base("TestBase", TestFaction, 1, (1, 2, 3), table) // 1 for 1st place points, 2 for 2nd, etc

class TestMinion(owner: Player, power: Int) extends Minion("TestMinion", TestFaction, power, owner)