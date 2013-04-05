package smackdown

object Tricksters extends Faction("Tricksters") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}

class CaveOfShinies(table: Table) extends Base("Cave of Shinies", Tricksters, 23, (4, 2, 1), table) {
  // when a minion is destroyed here, its owner gains +1 point
}

class MushroomKingdom(table: Table) extends Base("Mushroom Kingdom", Tricksters, 20, (5, 3, 2), table) {
  // at the start of each player's turn, they may move on other player's minion
  // from any base to here
}

class Gremlin(owner: Player) extends Minion("Gremlin", Tricksters, 2, owner) {
  // ongoing: after this minion is destroyed,
  //    draw a card
  //    and each other player discards a random card
}

class Gnome(owner: Player) extends Minion("Gnome", Tricksters, 3, owner) {
  // you may destroy a minion on this base
  // with power less than the number of minions you have here
}

class Brownie(owner: Player) extends Minion("Brownie", Tricksters, 4, owner) {
  // ongoing: after another player plays a card that effects this minion,
  // that player discards two random cards
}

class Leprechaun(owner: Player) extends Minion("Leprechaun", Tricksters, 5, owner) {
  // ongoing: after another player plays a minion here
  // with power less than this minions power
  // destroy it (it's ability is resolved first)
  // TODO: what happens if Leprechaun has Upgrade (so he's a 7) and NinjaMaster is played, which kills the Lep?
}

class TakeTheShinies(owner: Player) extends Action("Take the Shinies", Tricksters, owner) {
  // each other player discards 2 random cards
  override def play(user: Player) { user.otherPlayers.foreach(_.randomDiscard(2)) }
}

class FlameTrap(owner: Player) extends Action("Flame Trap", Tricksters, owner) {
  // play on a base. ongoing: whenever another player plays a minion here,
  // destroy it and this card (it's ability is resolved first)
}

class BlockThePath(owner: Player) extends Action("Block the Path", Tricksters, owner) {
  // play on a base and select a faction
  // ongoing: minions of that faction cannot be played here
}

class PayThePiper(owner: Player) extends Action("Pay the Piper", Tricksters, owner) {
  // play on a base. ongoing: after another player plays a minion here,
  // that player discards a card
}

class MarkOfSleep(owner: Player) extends Action("Mark of Sleep", Tricksters, owner) {
  // choose a player. that player cannot play actions on their next turn
  // (effect expires when targetPlayer.onTurnEnd)
}

class Hideout(owner: Player) extends Action("Hideout", Tricksters, owner) {
  // play on a base. ongoing: if another player's Action would affect your minions here,
  // destroy this card and the Action does not affect your minions
}

class EnshroudingMist(owner: Player) extends Action("Enshrouding Mist", Tricksters, owner) {
  // play on a base. ongoing: on your turn, you may play an extra minion here
}

class Disenchant