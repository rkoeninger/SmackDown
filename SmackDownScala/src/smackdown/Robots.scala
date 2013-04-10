package smackdown

import Utils._

object Robots extends Faction("Robots") {
  override def bases(table: Table) = Set(new CentralBrain(table), new Factory4361337(table))
  override def cards(owner: Player) = Deck(owner,
    1.of[MicrobotAlpha],
    1.of[MicrobotArchive],
    2.of[MicrobotFixer],
    2.of[MicrobotGuard],
    2.of[MicrobotReclaimer],
    4.of[Zapbot],
    3.of[Hoverbot],
    2.of[Warbot],
    1.of[Nukebot],
    2.of[TechCenter])
}

class CentralBrain(table: Table) extends Base("The Central Brain", Robots, 19, (4, 2, 1), table) {
  // Each minion here gains +1 power.
  bonuses += Bonus(1)
}

class Factory4361337(table: Table) extends Base("Factory 234-1337", Robots, 25, (2, 2, 0), table) {
  // When this base scores, each player gets +1 point for every 5 minion strength.
  override def onScore() {
    for (p <- minions.map(_.owner))
      p.points += minions.ownedBy(p).map(_.strength).sum / 5
  }
}

abstract class Microbot(name: String, owner: Player) extends Minion(name, Robots, 1, owner) {
  // A minion must be in play for Microbot Alpha to make it into a Microbot.
  def isMicrobot(minion: Minion) = minion.is[Microbot] || (minion.isOnTable && owner.minionsInPlay.exists(_.is[MicrobotAlpha]))
}

class MicrobotAlpha(owner: Player) extends Microbot("Microbot Alpha", owner) {
  // Gains +1 for each other Microbot in play. All of your minions are considered microbots.
  override def strength() = super.strength + (owner.minionsInPlay - this).count(isMicrobot(_))
}

class MicrobotArchive(owner: Player) extends Microbot("Microbot Archive", owner) {
  // When is minion or any other Microbot is destroyed, draw a card.
  override def minionDestroyed(minion: Minion, base: Base) {
    if (isMicrobot(minion))
      owner.draw
  }
}

class MicrobotFixer(owner: Player) extends Microbot("Microbot Fixer", owner) {
  // If this is the first minion you played this turn, you may play an extra minion.
  // All microbots gain +1.
  val microbotBonus = Bonus(m => if (isMicrobot(m)) 1 else 0)
  override def play(base: Base) {
    owner.bonuses += microbotBonus
  }
  override def destroy(destroyer: Player) {
    owner.bonuses -= microbotBonus // TODO: need generic method for when a minion is dismissed or disabled
    super.destroy(destroyer)
  }
}

class MicrobotGuard(owner: Player) extends Microbot("Microbot Guard", owner) {
  // Destroy a minion on this base with power less than the number of minions you have on this base.
  override def play(base: Base) {
    for (m <- owner.chooseMinionOnBase(base, base.minions.ownedBy(owner).size - 1))
      m.destroy(owner)
  }
}

class MicrobotReclaimer(owner: Player) extends Microbot("Microbot Reclaimer", owner)
  // If this is the first minion you played this turn, you may play an extra minion.
  // TODO: player/table needs a move history
  // You may reshuffle any number of microbots from your discard into your deck.

class Zapbot(owner: Player) extends Minion("Zapbot", Robots, 2, owner) {
  // You may play an extra minion power 2 or less.
  override def play(base: Base) {
    for (m <- owner.chooseMinionInHand(2))
      owner.playMinion(m) 
  }
}

class Hoverbot(owner: Player) extends Minion("Hoverbot", Robots, 3, owner) {
  // Reveal the top card of your draw pile, if it is a minion, you may play it as an extra minion.
  override def play(base: Base) {
    for (c <- owner.reveal;
         m <- if (c.is[Minion]) Some(c.as[Minion]) else None)
      if (owner.chooseYesNo)
        owner.playMinion(m)
  }
}

class Warbot(owner: Player) extends Minion("Warbot", Robots, 4, owner) {
  // This minion cannot be destroyed
  override def destructable = false
}

class Nukebot(owner: Player) extends Minion("Nukebot", Robots, 5, owner) {
  // When this minion is destroyed, destroy all other player's minions on this base.
  override def destroy(destroyer: Player) {
    for (b <- base;
         m <- b.minions.destructable.filter(_.owner != this.owner))
      m.destroy(owner)
  }
}

class TechCenter(owner: Player) extends Action("Tech Center", Robots, owner) {
  // Choose a base, draw 1 card for each minion you have there.
  override def play(user: Player) {
    for (b <- user.chooseBaseInPlay)
      user.draw(b.minions.ownedBy(user).size)
  }
}