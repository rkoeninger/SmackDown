package smackdown

import Utils._

object Dinosaurs extends Faction("Dinosaurs") {
  override def bases(table: Table) = List(new JungleOasis(table), new TarPits(table))
  override def cards(owner: Player) = List[DeckCard]()
}

class JungleOasis(table: Table) extends Base("Jungle Oasis", Dinosaurs, 12, (2, 0, 0), table)

class TarPits(table: Table) extends Base("Tar Pits", Dinosaurs, 16, (4, 2, 1), table)
// minions destroyed here go to the bottom of their owner's draw pile instead of the discard

class WarRaptor(owner: Player) extends Minion("War Raptor", Dinosaurs, 2, owner) {
  // Gains +1 for each war raptor in play (including this one)
  override def strength() = super.strength + base.map(_.minions.count(_.is[WarRaptor])).getOrElse(0)
}

class ArmorStego(owner: Player) extends Minion("Armor Stego", Dinosaurs, 3, owner) {
  // Has +2 power on other player's turns.
  override def strength() = super.strength + (if (owner != table.currentPlayer) 2 else 0)
}

class Laseratops(owner: Player) extends Minion("Laseratops", Dinosaurs, 4, owner) {
  // Destroy a minion power 2 or less on this base.
  override def play(base: Base) {
    for (m <- owner.callback.selectMinion(base.minions.destructable.maxStrength(2)))
      m.destroy(owner)
  }
}

class KingRex(owner: Player) extends Minion("King Rex", Dinosaurs, 7, owner)

class Howl(owner: Player) extends Action("Howl", Dinosaurs, owner) {
  // Each of your minions gains +1 power until the end of your turn.
  override def play(user: Player) { Bonus.untilTurnEnd(user, 1) }
}

class Augmentation(owner: Player) extends Action("Augmentation", Dinosaurs, owner) {
  // One minion gains +4 power until the end of your turn.
  override def play(user: Player) {
    for (m <- user.callback.selectMinion(table.minions))
      Bonus.untilTurnEnd(m, 4)
  }
}

class NaturalSelection(owner: Player) extends Action("Natural Selection", Dinosaurs, owner) {
  // Choose one of your minions on a base. Destroy a minion there with power less then yours.
  override def play(user: Player) {
    for (m0 <- user.callback.selectMinion(table.minions.ownedBy(user));
         b <- m0.base;
         m1 <- user.callback.selectMinion(b.minions.destructable.maxStrength(m0.strength - 1)))
      m1.destroy(user)
  }
}

class Upgrade(owner: Player) extends Action("Upgrade", Dinosaurs, owner) {
  // play on a minion. ongoing: this minion has +2 power
}

class Rampage(owner: Player) extends Action("Rampage", Dinosaurs, owner) {
  // reduce the breakpoint of a base by the power of one of your minions on that base
  // until end of turn
  // (power/breakpoint are linked - if minion is given power boost, break point should lower further)
}

class WildlifePreserve {
  // Play on a base. Ongoing: Your minoins here are not affected by other player's actions.
}

class SurvivalOfTheFittest {
  // destroy the lowest-power minion (you choose in case of a tie)
  // on a base with a higher-power minion
}

class ToothAndClawAndGuns(owner: Player) extends Action("Tooth and Claw... and Guns", Dinosaurs, owner) {
  // play on a minion
  // ongoing: if an ability would affect this minion, destroy this card and the ability does not
  // affect this minion
}