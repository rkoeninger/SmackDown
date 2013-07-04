package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import Utils._

object Dinosaurs extends Faction("Dinosaurs") {
  override def bases(table: Table) = Set(new JungleOasis(table), new TarPits(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[WarRaptor],
    3.of[ArmorStego],
    2.of[Laseratops],
    1.of[KingRex],
    2.of[Howl],
    2.of[Augmentation],
    1.of[Upgrade],
    1.of[NaturalSelection],
    1.of[Rampage],
    1.of[SurvivalOfTheFittest],
    1.of[WildlifePreserve],
    1.of[ToothAndClawAndGuns])
}

class JungleOasis(table: Table) extends Base("Jungle Oasis", Dinosaurs, 12, (2, 0, 0), table)

class TarPits(table: Table) extends Base("Tar Pits", Dinosaurs, 16, (4, 2, 1), table)
// minions destroyed here go to the bottom of their owner's draw pile instead of the discard

class WarRaptor(owner: Player) extends Minion("War Raptor", Dinosaurs, 2, owner) {
  // Ongoing: Gains +1 for each War Raptor on this base (including this one).
  override def power() = super.power + base.map(_.minions.count(_.is[WarRaptor])).getOrElse(0)
}

class ArmorStego(owner: Player) extends Minion("Armor Stego", Dinosaurs, 3, owner) {
  // Has +2 power on other player's turns.
  override def power() = super.power + (if (owner != table.currentPlayer) 2 else 0)
}

class Laseratops(owner: Player) extends Minion("Laseratops", Dinosaurs, 4, owner) {
  // Destroy a minion power 2 or less on this base.
  override def play(base: Base) {
    for (m <- owner.choose.minion.onBase(base).powerAtMost(2)) m.destroy(owner)
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
    for (m <- user.choose.minion.inPlay)
      Bonus.untilTurnEnd(user, m, 4)
  }
}

class NaturalSelection(owner: Player) extends Action("Natural Selection", Dinosaurs, owner) {
  // Choose one of your minions on a base. Destroy a minion there with power less then yours.
  override def play(user: Player) {
    for (m0 <- user.choose.minion.inPlay.mine;
         b <- m0.base;
         m1 <- user.choose.minion.onBase(b).powerAtMost(m0.power - 1))
      m1.destroy(user)
  }
}

class Upgrade(owner: Player) extends Action("Upgrade", Dinosaurs, owner) {
  // Play on a minion. Ongoing: This minion has +2 power.
  val bonus = Bonus(2)
  override def play(user: Player) {
    for (m <- user.choose.minion.inPlay)
      m.bonuses += bonus
  }
  override def destroy(card: Card) {
    for (m <- card.optionCast[Minion])
      m.bonuses -= bonus
  }
}

class SurvivalOfTheFittest(owner: Player) extends Action("Survival of the Fittest", Dinosaurs, owner) {
  // Destroy the lowest-power minion (you choose in case of a tie) on each base with a higher-power minion.
  override def play(user: Player) {
    for (b <- table.basesInPlay.filter(_.minions.map(_.power).size > 1);
         m <- user.callback.choose(b.minions.filter(_.power < b.minions.map(_.power).max)))
      m.destroy(user)
  }
}

class Rampage(owner: Player) extends Action("Rampage", Dinosaurs, owner) {
  // Reduce the breakpoint of a base by the power of one of your minions on that base until end of turn.
  override def play(user: Player) {
    for (b <- user.choose.base.inPlay;
         m <- user.choose.minion.onBase(b).mine)
      BreakPointBonus.untilTurnEnd(user, b, _ => - m.power)
  }
}

class WildlifePreserve(owner: Player) extends Action("Wildlife Preserve", Dinosaurs, owner) {
  // Play on a base. Ongoing: Your minoins here are not affected by other player's actions.
}

class ToothAndClawAndGuns(owner: Player) extends Action("Tooth and Claw... and Guns", Dinosaurs, owner) {
  // play on a minion
  // ongoing: if an ability would affect this minion, destroy this card and the ability does not
  // affect this minion
}