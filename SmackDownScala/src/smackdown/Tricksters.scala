package smackdown

import Utils._

object Tricksters extends Faction("Tricksters") {
  override def bases(table: Table) = Set[Base](new CaveOfShinies(table), new MushroomKingdom(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Gremlin],
    3.of[Gnome],
    2.of[Brownie],
    1.of[Leprechaun])
}

class CaveOfShinies(table: Table) extends Base("Cave of Shinies", Tricksters, 23, (4, 2, 1), table) {
  // When a minion is destroyed here, its owner gains +1 point.
  override def minionDestroyed(minion: Minion, base: Base) {
    if (base == this)
      minion.owner.points += 1
  }
}

class MushroomKingdom(table: Table) extends Base("Mushroom Kingdom", Tricksters, 20, (5, 3, 2), table) {
  // At the start of each player's turn, they may move one other player's minion
  // from any base to here.
  override def onTurnBegin(player: Player) {
    for (m <- player.callback.choose(table.minions.filter(m => m.base != Some(this) && m.owner != player)))
      m --> this
  }
}

class Gremlin(owner: Player) extends Minion("Gremlin", Tricksters, 2, owner) {
  // Ongoing: After this minion is destroyed, draw a card
  // and each other player discards a random card.
  override def destroy(destroyer: Player) {
    super.destroy(destroyer)
    owner.draw
    for (p <- owner.otherPlayers) p.randomDiscard
  }
}

class Gnome(owner: Player) extends Minion("Gnome", Tricksters, 3, owner) {
  // You may destroy a minion on this base
  // with power less than the number of minions you have here.
  override def play(base: Base) {
    for (m <- owner.chooseMinionOnBase(base, base.minions.ownedBy(owner).size - 1))
      m.destroy(owner)
  }
}

class Brownie(owner: Player) extends Minion("Brownie", Tricksters, 4, owner) {
  // ongoing: after another player plays a card that effects this minion,
  // that player discards two random cards
}

class Leprechaun(owner: Player) extends Minion("Leprechaun", Tricksters, 5, owner) {
  // Ongoing: After another player plays a minion here
  // with power less than this minion's power,
  // destroy it (its ability is resolved first).
  override def minionPlayed(minion: Minion) {
    if (isOnTable && minion.base == this.base && minion.strength < this.strength)
      minion.destroy(owner)
  }
  // TODO: what happens if Leprechaun has Upgrade (so he's a 7) and NinjaMaster is played, which kills the Lep?
}

class TakeTheShinies(owner: Player) extends Action("Take the Shinies", Tricksters, owner) {
  // Each other player discards 2 random cards.
  override def play(user: Player) { for (p <- user.otherPlayers) p.randomDiscard(2) }
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

class Disenchant(owner: Player) extends Action("Disenchant", Tricksters, owner) {
  // Destroy an action attached to a minion or a base.
  override def play(user: Player) {
    for (a <- user.chooseActionInPlay)
      a.destroy(this)
  }
}