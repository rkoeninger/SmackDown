package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Rank, Table}
import org.scalatest.FunSpec

class FactionTest extends FunSpec {

  describe("Factions") {
    it ("should have 2 bases each") {
      for (f <- findFactions)
        assert(f.bases(table).size == 2)
    }
    it ("should have 20 cards each") {
      for (f <- findFactions)
        assert(f.cards(player1).size == 20)
    }
  }
  
  def findFactions(): Set[Faction] = Set[Faction]()//Class.forName(arg0)
  // TODO: how to discover factions at run-time?
  
  val table = new Table()
  val player1 = new Player("Bob", List(TestFaction), table, new Callback() {})
}