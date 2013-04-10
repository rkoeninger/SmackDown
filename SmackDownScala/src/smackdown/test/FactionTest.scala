package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Rank, Table}
import smackdown.{Aliens, Dinosaurs, Ninjas, Pirates, Robots, Tricksters, Wizards, Zombies}
import org.scalatest.FunSpec

class FactionTest extends FunSpec {

  for (f <- findFactions.toList.sortBy(_.name)) {
    describe(f.name) {
      it ("should have 2 bases") {
        assert(f.bases(table).size == 2)
      } 
      it ("should have 20 cards in deck") {
        assert(f.cards(player1).size == 20)
      }
    }
  }
  
  def findFactions(): Set[Faction] = Set[Faction](
      Aliens, Dinosaurs, Ninjas, Pirates, Robots, Tricksters, Wizards, Zombies)
  
  val table = new Table()
  val player1 = new Player("Bob", List(TestFaction), table, new Callback() {})
}