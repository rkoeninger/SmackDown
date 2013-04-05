package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Rank, Table}
import org.scalatest.FunSpec

class BaseScoringTest extends FunSpec {
  
  describe("The Base scoring algorithm") {
    it ("should reward points to no-one if no-one has any minions on it") {
      testBase(List(), Set())
    }
    it ("should reward 1st place points to the only player with minions on it and none to other players") {
      testBase(List((player1, 3)),
          Set((player1, 1, 1)))
    }
    it ("should reward 1st place points to the player with greater total strength when there are only 2 players") {
      testBase(List((player1, 3), (player2, 4)),
          Set((player1, 2, 2), (player2, 1, 1)))
    }
    it ("should reward 1st place points to both players when there is a 2-way tie and no others") {
      testBase(List((player1, 5), (player2, 5)),
          Set((player1, 1, 1), (player2, 1, 1)))
    }
    it ("should reward 1st place points to the strongest player, 2nd to the 2nd strongest and 3rd to the 3rd") {
      testBase(List((player1, 5), (player2, 7), (player3, 2)),
          Set((player1, 2, 2), (player2, 1, 1), (player3, 3, 3)))
    }
    it ("should reward 1st place points to both equally strong players, stronger than a 3rd who gets 3rd place points") {
      testBase(List((player1, 5), (player2, 7), (player3, 7)),
          Set((player1, 3, 2), (player2, 1, 1), (player3, 1, 1)))
    }
    it ("should reward 1st place points to the stronger player and 2nd place points to both weaker, but equally strong players") {
      testBase(List((player1, 5), (player2, 4), (player3, 4)),
          Set((player1, 1, 1), (player2, 2, 2), (player3, 2, 2)))
    }
    it ("should reward 1st place points to all present players in the event of a 3-way tie") {
      testBase(List((player1, 4), (player2, 4), (player3, 4)),
          Set((player1, 1, 1), (player2, 1, 1), (player3, 1, 1)))
    }
    it ("should reward 1st place points to the strongest player, 2nd to the 2nd strongest and 3rd to the 3rd, 4th gets none") {
      testBase(List((player1, 5), (player2, 3), (player3, 4), (player4, 7)),
          Set((player1, 2, 2), (player3, 3, 3), (player4, 1, 1)))
    }
    it ("should reward 1st place points to both the strongest, tied players, 3rd place points to the 2nd strongest and the 3rd strongest gets none") {
      testBase(List((player1, 4), (player2, 5), (player3, 7), (player4, 7)),
          Set((player2, 3, 2), (player3, 1, 1), (player4, 1, 1)))
    }
    it ("should reward 1st place points to the strongest, 2nd place points to both 2nd-strongest, tied players, and the weakest gets none") {
      testBase(List((player1, 5), (player2, 5), (player3, 2), (player4, 7)),
          Set((player1, 2, 2), (player2, 2, 2), (player4, 1, 1)))
    }
    it ("should reward 1st place points to the strongest, 2nd to the 2nd strongest and 3rd to both weakest if they're tied for weakest") {
      testBase(List((player1, 5), (player2, 2), (player3, 2), (player4, 7)),
          Set((player1, 2, 2), (player2, 3, 3), (player3, 3, 3), (player4, 1, 1)))
    }
    it ("should reward 1st place points to the 3 players in a 3-way tie, and they're stronger than the 4th, who gets none") {
      testBase(List((player1, 5), (player2, 7), (player3, 7), (player4, 7)),
          Set((player2, 1, 1), (player3, 1, 1), (player4, 1, 1)))
    }
    it ("should reward 2st place points to the 3 players in a 3-way tie, and they're weaker than the 4th, who gets 1st place points") {
      testBase(List((player1, 4), (player2, 4), (player3, 7), (player4, 4)),
          Set((player1, 2, 2), (player2, 2, 2), (player3, 1, 1), (player4, 2, 2)))
    }
    it ("should reward 1st place points to all present players in the event of a 4-way tie") {
      testBase(List((player1, 4), (player2, 4), (player3, 4), (player4, 4)),
          Set((player1, 1, 1), (player2, 1, 1), (player3, 1, 1), (player4, 1, 1)))
    }
    it ("should reward 1st place points to both tied, stronger players and 3rd place points to both tied, weaker players") {
      testBase(List((player1, 4), (player2, 8), (player3, 8), (player4, 4)),
          Set((player1, 3, 2), (player2, 1, 1), (player3, 1, 1), (player4, 3, 2)))
    }
  }
  
  def testBase(minions: List[(Player, Int)], results: Set[(Player, Int, Int)]) {
    val base = new TestBase(table)
    base.cards = minions.map(x => new TestMinion(x._1, x._2)).toSet
    assert(base.score == results.map(x => new Rank(x._1, x._2, x._3)).toSet)
  }
  
  val table = new Table()
  
  val player1 = new Player("Rob", List(TestFaction), table, new TestCallback())
  val player2 = new Player("Job", List(TestFaction), table, new TestCallback())
  val player3 = new Player("Joe", List(TestFaction), table, new TestCallback())
  val player4 = new Player("Moe", List(TestFaction), table, new TestCallback())
}