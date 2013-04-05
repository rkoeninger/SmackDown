package smackdown

import Utils._

object Robots extends Faction("Robots") {
  override def bases(table: Table) = List(new CentralBrain(table), new Factory2341337(table))
  override def cards(owner: Player) = List(
    new MicrobotAlpha(owner),
    new MicrobotArchive(owner),
    new MicrobotFixer(owner), new MicrobotFixer(owner),
    new MicrobotGuard(owner), new MicrobotGuard(owner),
    new MicrobotReclaimer(owner), new MicrobotReclaimer(owner),
    new Zapbot(owner), new Zapbot(owner), new Zapbot(owner), new Zapbot(owner),
    new Hoverbot(owner), new Hoverbot(owner), new Hoverbot(owner),
    new Warbot(owner), new Warbot(owner),
    new Nukebot(owner),
    new TechCenter(owner),
    new TechCenter(owner)
  )
}

class CentralBrain(table: Table) extends Base("The Central Brain", Robots, 19, (4, 2, 1), table) {
  // Each minion here gains +1 power
  bonuses += Bonus(1)
}

class Factory2341337(table: Table) extends Base("Factory 234-1337", Robots, 25, (2, 2, 0), table) {
  // When this base scores, each player gets +1 point for every 5 minion strength
  override def onScore() {
    minions.groupBy(_.owner).map(x => x._1.points += x._2.map(_.strength).sum / 5)
  }
}

abstract class Microbot(name: String, owner: Player) extends Minion(name, Robots, 1, owner) {
  def isMicrobot(minion: Minion) = minion.is[Microbot] || owner.minionsInPlay.exists(_.is[MicrobotAlpha])
}

class MicrobotAlpha(owner: Player) extends Microbot("Microbot Alpha", owner) {
  // Gains +1 for each other Microbot in play. All of your minions are considered microbots
  override def strength() = super.strength + (owner.minionsInPlay - this).count(isMicrobot(_))
}

class MicrobotArchive(owner: Player) extends Microbot("Microbot Archive", owner) {
  // When is minion or any other Microbot is destroyed, draw a card
  // TODO: need onMinionDestroyed event
}

class MicrobotFixer(owner: Player) extends Microbot("Microbot Fixer", owner) {
  // If this is the first minion you played this turn, you may play an extra minion.
  // All microbots gain +1
  val microbotBonus = Bonus(m => if (isMicrobot(m)) 1 else 0)
  override def play(base: Base) {
    owner.bonuses += microbotBonus
  }
  override def destroy(destroyer: Player) {
    owner.bonuses -= microbotBonus // TODO: need generic method for when a minion is dismissed or disabled
  }
}

class MicrobotGuard(owner: Player) extends Microbot("Microbot Guard", owner) {
  // Destroy a minion on this base with power less than the number of minions you have on this base
  // TODO: does that include this Microbot Guard?
  override def play(base: Base) {
    owner.callback.selectMinion(m => m.destructable && m.isOnBase(base) && m.strength < base.minions.count(_.owner == owner)).map(_.destroy(owner))
    // TODO: must destroy if possible even if only option belongs to the owner
  }
}

class MicrobotReclaimer(owner: Player) extends Microbot("Microbot Reclaimer", owner)
  // If this is the first minion you played this turn, you may play an extra minion.
  // TODO: player/table needs a move history
  // You may reshuffle any number of microbots from your discard into your deck.

class Zapbot(owner: Player) extends Minion("Zapbot", Robots, 2, owner) {
  // You may play an extra minion power 2 or less
  override def play(base: Base) {
    val m = owner.callback.selectFromHand(_.is[Minion])
    // ... select a base, etc...
    // TODO: need a function on Callback for this (playing a minion) 
  }
}

class Hoverbot(owner: Player) extends Minion("Hoverbot", Robots, 3, owner) {
  // Reveal the top card of your card, if it is a minion, you may play it as an extra minion
  override def play(base: Base) {
    val card = owner.reveal
    if (card.isDefined) {
      // ... select a base, etc...
      // TODO: need a function on Callback for this (playing a minion) 
    }
  }
}

class Warbot(owner: Player) extends Minion("Warbot", Robots, 4, owner) {
  // This minion cannot be destroyed
  override def destructable = false
}

class Nukebot(owner: Player) extends Minion("Nukebot", Robots, 5, owner) {
  // When this minion is destroyed, destroy all other player's minions on this base
  override def destroy(destroyer: Player) {
    base.map(_.minions.filter(m => m.destructable && m.owner != owner).map(_.destroy(owner)))
  }
}

class TechCenter(owner: Player) extends Action("Tech Center", Robots, owner) {
  // Choose a base, draw 1 card for each minion you have there
  override def play(user: Player) {
    user.callback.selectBase(x => true).map(b => user.draw(b.minions.filter(_.owner == user).size))
  }
}