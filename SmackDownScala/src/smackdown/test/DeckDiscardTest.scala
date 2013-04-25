package smackdown.test

import smackdown._
import org.scalatest.FunSpec

class DeckDiscardTest extends FunSpec {
  
  describe("A DeckCard ") {
    it ("should remove itself from owner's draw pile when drawn") {
      player.drawPile = List(card1, card2, card3, card4)
      player.discardPile = Set()
      player.hand = Set()
      
      card1 --> Hand
      assert(player.hand == Set(card1))
      assert(player.drawPile == List(card2, card3, card4))
      
      card3 --> Hand
      assert(player.hand == Set(card1, card3))
      assert(player.drawPile == List(card2, card4))
    }
    it ("should remove itself from owner's hand when discarded") {
      player.drawPile = List()
      player.discardPile = Set(card1)
      player.hand = Set(card2, card3, card4)
      
      card2 --> Discard
      assert(player.hand == Set(card3, card4))
      assert(player.discardPile == Set(card1, card2))
      
      card3 --> Discard
      assert(player.hand == Set(card4))
      assert(player.discardPile == Set(card1, card2, card3))
    }
    it ("should remove itself from a base when returned to owner's hand") {
      player.drawPile = List()
      player.discardPile = Set()
      player.hand = Set()
      base.cards = Set()
      
      card1 --> base
      card2 --> base
      assert(player.hand == Set())
      assert(base.cards == Set(card1, card2))
      
      card1 --> Hand
      assert(player.hand == Set(card1))
      assert(base.cards == Set(card2))
      
      card2 --> Hand
      assert(player.hand == Set(card1, card2))
      assert(base.cards == Set())
    }
    it ("should remove itself from owner's discard when drawn") {
      player.drawPile = List()
      player.discardPile = Set(card1, card2, card3, card4)
      player.hand = Set()
      
      card1 --> Hand
      assert(player.hand == Set(card1))
      assert(player.discardPile == Set(card2, card3, card4))
      
      card3 --> Hand
      assert(player.hand == Set(card1, card3))
      assert(player.discardPile == Set(card2, card4))
    }
    it ("should remove itself from owner's discard when placed on a base") {
      player.drawPile = List()
      player.discardPile = Set(card1, card2, card3)
      player.hand = Set()
      base.cards = Set(card4)
      
      card2 --> base
      assert(player.discardPile == Set(card1, card3))
      assert(base.cards == Set(card4, card2))
      
      card3 --> base
      assert(player.discardPile == Set(card1))
      assert(base.cards == Set(card2, card3, card4))
    }
    it ("should remove itself from a base when placed on the bottom of owner's draw pile") {
      player.drawPile = List(card3, card4)
      player.discardPile = Set()
      player.hand = Set()
      base.cards = Set()
      
      card1 --> base
      card2 --> base
      assert(player.hand == Set())
      assert(base.cards == Set(card1, card2))
      
      card1 --> DrawBottom
      assert(player.drawPile == List(card3, card4, card1))
      assert(base.cards == Set(card2))
      
      card2 --> DrawBottom
      assert(player.drawPile == List(card3, card4, card1, card2))
      assert(base.cards == Set())
    }
  }
  
  val table = new Table()
  
  val base = new TestBase(table)
  
  val player = new Player("Bob", List(TestFaction), table, new TestCallback())
  
  val card1 = new TestMinion(player, 1)
  val card2 = new TestMinion(player, 2)
  val card3 = new TestMinion(player, 3)
  val card4 = new TestMinion(player, 4)
}