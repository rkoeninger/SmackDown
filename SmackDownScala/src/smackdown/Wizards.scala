package smackdown

object Wizards extends Faction("Wizards") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}

class GreatLibrary(table: Table) extends Base("The Great Library", Wizards, 22, (4, 2, 1), table) {
  // after this base scores, each player with a minion here may draw a card
  override def afterScore(newBase: Base) {
    minions.map(_.owner).foreach(player => {
      if (player.callback.selectBoolean) player.draw 
    })
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
  // reveal the top card of your deck.
  // if it is an action,
  //     you may place it in your hand
  //     or play it as an extra action
  // otherwise, return it to the top of your draw pile
}

class Chronomage(owner: Player) extends Minion("Chronomage", Wizards, 3, owner) {
  // You may play an extra action this turn.
  override def play(base: Base) { owner.moves += new PlayAction() }
}

class Archmage(owner: Player) extends Minion("Archmage", Wizards, 4, owner) {
  // Ongoing: You may play an extra action on each of your turns.
  override def beginTurn() {
    if (isOnTable) owner.moves += new PlayAction()
  }
}

class MysticStudies(owner: Player) extends Action("Mystic Studies", Wizards, owner) {
  // Draw 2 cards.
  override def play(user: Player) { user.draw(2) }
}

class TimeLoop(owner: Player) extends Action("Time Loop", Wizards, owner) {
  // play 2 extra actions (immediately, required)
}

class Sacrifice(owner: Player) extends Action("Sacrifice", Wizards, owner) {
  // choose one of your minions. draw cards equal to its power. destroy that minion
  override def play(user: Player) {
    val mo = user.callback.selectMinion(m => m.isOnTable && m.owner == user)
    mo.foreach(m => {
      user.draw(m.strength)
      m.destroy(user)
    })
  }
}

class Summon(owner: Player) extends Action("Summon", Aliens, owner) {
  // play an extra minion
}

class Portal(owner: Player) extends Action("Portal", Aliens, owner) {
  // reveal the top five cards of your deck
  // place any number of minions revealed into your hand
  // return the other cards to the top of your deck in any order
}

class Scry(owner: Player) extends Action("Scry", Aliens, owner) {
  // search your deck for an action and reveal it to all players
  // place it in your hand and shuffle your deck
}

class MassEnchantment // TODO: need card text

class WindsOfChange {
  // shuffle your hand into your deck and draw 5 cards. you may play an extra action
}