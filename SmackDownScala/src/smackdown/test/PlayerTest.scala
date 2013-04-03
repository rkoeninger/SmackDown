package smackdown.test

import smackdown.{Action, Base, Callback, DeckCard, Faction, Minion, Player, Table}
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
}