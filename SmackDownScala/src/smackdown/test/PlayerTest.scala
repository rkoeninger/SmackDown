package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Table}
import smackdown.Utils._
import org.scalatest.FunSpec

class PlayerTest extends FunSpec {
  
  describe("A Player's draw pile") {
    it ("should replenish itself with the randomized discard pile") {
      val table = new Table()
      val player = new Player("Bob", List(TestFaction), table, new TestCallback())
      val card1 = new TestMinion(player, 1)
      val card2 = new TestMinion(player, 2)
      val card3 = new TestMinion(player, 3)
      val card4 = new TestMinion(player, 4)
      val card5 = new TestMinion(player, 5)
      val card6 = new TestMinion(player, 6)
      
      player.hand = Set()
      player.drawPile = List(card1, card2)
      player.discardPile = Set(card3, card4, card5, card6)
      
      player.draw
      assert(player.hand == Set(card1))
      assert(player.drawPile == List(card2))
      assert(player.discardPile == Set(card3, card4, card5, card6))
      
      player.draw
      assert(player.hand == Set(card1, card2))
      assert(player.drawPile == List())
      assert(player.discardPile == Set(card3, card4, card5, card6))
      
      player.draw
      assert(player.hand.size == 3)
      assert(player.drawPile.size == 3)
      assert(player.discardPile == Set())
      
      player.draw
      assert(player.hand.size == 4)
      assert(player.drawPile.size == 2)
      assert(player.discardPile == Set())
    }
  }
  
  describe("A Player's end of turn phase") {
    it ("should perform all onTurnEnd events") {
      val table = new Table()
      val player = new Player("Bob", List(TestFaction), table, new TestCallback())
      var i = 0
      
      player.onTurnEnd { i = 1 }
      assert(i == 0)
      assert(player.onTurnEndSet.size == 1)
      
      player.onTurnEnd { i = 2 }
      assert(i == 0)
      assert(player.onTurnEndSet.size == 2)
      
      player.endTurn
      assert(i == 2)
      assert(player.onTurnEndSet.isEmpty)
    }
    it ("should draw 2 cards and then discard down to 10") {
      val table = new Table()
      val callback = new Callback() {
        // Always discard weakest card first
        override def choose[T](cards: Set[T]): Option[T] =
          Some(cards.minBy(_.as[Minion].strength))
      }
      val player = new Player("Bob", List(TestFaction), table, callback)
      val card1 = new TestMinion(player, 1)
      val card2 = new TestMinion(player, 2)
      val card3 = new TestMinion(player, 3)
      val card4 = new TestMinion(player, 4)
      val card5 = new TestMinion(player, 5)
      val card6 = new TestMinion(player, 6)
      val card7 = new TestMinion(player, 7)
      val card8 = new TestMinion(player, 8)
      val card9 = new TestMinion(player, 9)
      val card10 = new TestMinion(player, 10)
      val card11 = new TestMinion(player, 11)
      val card12 = new TestMinion(player, 12)
      val card13 = new TestMinion(player, 13)
      val card14 = new TestMinion(player, 14)
      player.drawPile = List(card7, card11)
      player.hand = Set(card3, card4, card12, card6, card1, card8, card9, card10, card2, card5, card13, card14)
      
      player.endTurn
      assert(player.drawPile.isEmpty)
      assert(player.hand.size == 10)
      assert(player.discardPile == Set(card1, card2, card3, card4))
    }
  }
}