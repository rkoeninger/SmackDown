package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

object Wizards extends Faction("Wizards") {
  override def bases(table: Table) = Set(new GreatLibrary(table), new SchoolOfWizardry(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Enchantress],
    3.of[Neophyte],
    2.of[Chronomage],
    1.of[Archmage],
    2.of[Summon],
    2.of[MysticStudies],
    1.of[Scry],
    1.of[MassEnchantment],
    1.of[TimeLoop],
    1.of[Portal],
    1.of[WindsOfChange],
    1.of[Sacrifice])
}

class GreatLibrary(table: Table) extends Base("The Great Library", Wizards, 22, (4, 2, 1), table) {
  // After this base scores, each player with a minion here may draw a card.
  override def afterScore(newBase: Base) {
    for (p <- minions.map(_.owner))
      if (p.chooseYesNo)
        p.draw
  }
}

class SchoolOfWizardry(table: Table) extends Base("School of Wizardry", Wizards, 20, (3, 2, 1), table) {
  // after this base scores, the winner may look at the top 3 cards of the base deck
  // chooses one to replace this base and places the others back on top in any other
  // TODO: what if there's a tie?
}

class Enchantress(owner: Player) extends Minion("Enchantress", Wizards, 2, owner) {
  // Draw a card.
  override def play(base: Base) { owner.draw }
}

class Neophyte(owner: Player) extends Minion("Neophyte", Wizards, 2, owner) {
  // Reveal the top card of your deck.
  // If it is an action,
  //     you may place it in your hand
  //     or play it as an extra action.
  // Otherwise, return it to the top of your draw pile.
  override def play(base: Base) {
    for (c <- owner.reveal;
         a <- c.optionCast[Action])
      if (owner.chooseYesNo)
        owner.playAction(a)
  }
}

class Chronomage(owner: Player) extends Minion("Chronomage", Wizards, 3, owner) {
  // You may play an extra action this turn.
  override def play(base: Base) { owner.moves += new PlayAction() }
}

class Archmage(owner: Player) extends Minion("Archmage", Wizards, 4, owner) {
  // Ongoing: You may play an extra action on each of your turns.
  override def beginTurn() { if (isOnTable) owner.moves += new PlayAction() }
}

class MysticStudies(owner: Player) extends Action("Mystic Studies", Wizards, owner) {
  // Draw 2 cards.
  override def play(user: Player) { user.draw(2) }
}

class TimeLoop(owner: Player) extends Action("Time Loop", Wizards, owner) {
  // Play 2 extra actions.
  override def play(user: Player) { 2 times user.playAction }
}

class Sacrifice(owner: Player) extends Action("Sacrifice", Wizards, owner) {
  // Choose one of your minions. Draw cards equal to its power. Destroy that minion.
  override def play(user: Player) {
    for (m <- user.choose.minion.inPlay.mine) {
      user.draw(m.strength)
      m.destroy(user)
    }
  }
}

class Summon(owner: Player) extends Action("Summon", Wizards, owner) {
  // Play an extra minion.
  override def play(user: Player) { user.playMinion }
}

class WindsOfChange(owner: Player) extends Action("Winds of Change", Wizards, owner) {
  // Shuffle your hand into your deck and draw 5 cards. You may play an extra action.
  override def play(user: Player) {
    user.hand.foreach(_ --> DrawTop)
    user.shuffle
    user.draw(5)
  }
}

class Scry(owner: Player) extends Action("Scry", Wizards, owner) {
  // Search your deck for an action and reveal it to all players.
  // Place it in your hand and shuffle your deck.
  override def play(user: Player) {
    for (a <- user.choose.action.inDrawPile) {
      a --> Hand
      user.drawPile = Random.shuffle(user.drawPile)
    }
  }
}

class Portal(owner: Player) extends Action("Portal", Wizards, owner) {
  // Reveal the top five cards of your deck.
  // Place any number of minions revealed into your hand.
  // Return the other cards to the top of your deck in any order.
}

class MassEnchantment(owner: Player) extends Action("Mass Enchantment", Wizards, owner) {
  // Reveal the top card of each other player's deck.
  // Play one revealed action as an extra action.
  // Return unused to the top of their decks.
  override def play(user: Player) {
    for (a <- user.callback.choose(user.otherPlayers.map(_.reveal).ofType[Action].toSet)) {
      a.play(user)
      // TODO: Move to discard?
    }
  }
}