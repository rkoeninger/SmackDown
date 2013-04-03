package smackdown

import scala.util.Random

class Table {
  var players = List[Player]()
  var baseDrawPile = List[Base]()
  var basesInPlay = Set[Base]()
  var baseDiscardPile = Set[Base]()
}

class Player(val name: String, val factions: List[Faction], val table: Table, val callback: Callback) {
  var points = 0
  var hand = Set[DeckCard]()
  var discardPile = Set[DeckCard]()
  var drawPile = List[DeckCard]()
  var expireOnTurnBegin = Set[Effect]()
  var expireOnTurnEnd = Set[Effect]()
  var bonuses = Set[Bonus]()
  var moves = Set[Move]()
  
  def minionsInPlay() = table.basesInPlay.flatMap(_.minions).filter(_.owner == this)
  
  def beginTurn() {
    moves = Set(new PlayMinion(), new PlayAction())
    
    expireOnTurnBegin.map(_.expire)
    expireOnTurnBegin = Set[Effect]()
  }
  
  def endTurn() {
    moves = Set()
    
    expireOnTurnEnd.map(_.expire)
    expireOnTurnEnd = Set[Effect]()
    
    draw(2)
    
    while (hand.size > 10)
      callback.selectFromHand().map(_.moveToDiscard)
  }
  
  def draw() {
    if (replenishDrawPile) drawPile(0).moveToHand
  }
  
  def draw(count: Int) {
    (1 to count) foreach draw
  }
  
  def reveal() = {
    if (replenishDrawPile) Some(drawPile(0)) else None
  }
  
  private def replenishDrawPile(): Boolean = {
    if (drawPile.isEmpty) {
      if (discardPile.isEmpty)
        return false
      drawPile = Random.shuffle(discardPile.toList)
      discardPile = Set()
    }
    return true
  }
  
  def randomDiscard() {
    if (hand.size > 0)
      hand = dropIndex(hand.toList, Random.nextInt(hand.size)).toSet
  }
  
  private def dropIndex[T](xs: List[T], n: Int) = {
    val (a, b) = xs splitAt n
    a ::: (b drop 1)
  }
}

trait Callback {
  def selectBase(predicate: Base => Boolean): Option[Base]
  def selectMinion(predicate: Minion => Boolean): Option[Minion]
  def selectAction(predicate: Action => Boolean): Option[Action]
  def selectFaction(): Option[Faction]
  def selectPlayer(predicate: Player => Boolean): Option[Player]
  def selectFromHand(predicate: DeckCard => Boolean = (x => true)): Option[DeckCard]
  def selectBoolean(): Boolean
}

abstract class Faction(val name: String) {
  def bases(table: Table): List[Base]
  def cards(owner: Player): List[DeckCard]
}

abstract class Card(val name: String, val faction: Faction)

class Base(name: String, faction: Faction, val breakPoint: Int, val scoreValues: (Int, Int, Int), val table: Table) extends Card(name, faction) {
  
  var cards = Set[DeckCard]()
  def minions() = cards.filter(_.isInstanceOf[Minion]).map(_.asInstanceOf[Minion]).toSet
  def actions() = cards.filter(_.isInstanceOf[Action]).map(_.asInstanceOf[Action]).toSet
  var bonuses = Set[Bonus]()
  
  def totalStrength() = minions.map(_.strength).sum
  
  def isInPlay() = table.basesInPlay.contains(this)
  
  def beforeScore() {}
  
  def onScore() {}
  
  def afterScore(newBase: Base) {}
  
  /**
   * Returns a mapping of players to (scoreReward, ranking) where the score reward is one of the
   * three values in scoreValues and the ranking is 1 for winner, 2 for runner-up, etc.
   * Rank values are 1, 2, 3... regardless of ties. If there is a tie for 1st and a third player
   * with less strength, the two players with greater strength will get scoreValues[0] and rank 1,
   * the weaker player will get scoreValues[2] and rank 2.
   * The mapping defaults to (0, 0) for (no points, not present).
   */
  def score(): Map[Player, (Int, Int)] = {
    val playerStrengths = minions.groupBy(_.owner).map(x => (x._1, x._2.map(_.strength).sum))
    val sortedStrengths = playerStrengths.values.toList.distinct.sorted.reverse
    var rewardCount = 0
    var rewardRank = 1
    
    val rewards = sortedStrengths.map(strength =>
      if (rewardCount < 3) {
        val reward = scoreValues.productElement(rewardCount).asInstanceOf[Int]
        val rewardGroup = playerStrengths.filter(_._2 == strength).map(_._1 -> (reward, rewardRank))
        rewardCount += rewardGroup.size
        rewardRank += 1
        rewardGroup
      }
      else Map[Player, (Int, Int)]()
    )
    
    val result = if (rewards.isEmpty) Map[Player, (Int, Int)]() else rewards.reduce(_ ++ _)
    result.withDefaultValue((0, 0))
  }
}

abstract class DeckCard(name: String, faction: Faction, val owner: Player) extends Card(name, faction) {
  var base: Option[Base] = None
  
  def moveToHand() {
    if (! isInHand) {
      remove
      owner.hand += this
    }
  }
  def moveToDiscard() {
    if (! isInDiscardPile) {
      remove
      owner.discardPile += this
    }
  }
  def moveToDrawPileTop() {
    remove
    owner.drawPile = this :: owner.drawPile
  }
  def moveToDrawPileBottom() {
    remove
    owner.drawPile = owner.drawPile :+ this
  }
  def moveToBase(base: Base) {
    remove
    if (this.base != Some(base)) {
      base.cards += this
      this.base = Some(base)
    }
  }
  private def remove() {
    if (isInHand) owner.hand -= this
    else if (isInDrawPile) owner.drawPile = owner.drawPile.filterNot(_ == this)
    else if (isInDiscardPile) owner.discardPile -= this
    else if (isOnBase) {
      base.map(_.cards -= this)
      base = None
    }
  }
  def isInHand() = owner.hand.contains(this)
  def isInDrawPile() = owner.drawPile.contains(this)
  def isInDiscardPile() = owner.discardPile.contains(this)
  def isOnBase() = base.isDefined
  def isOnBase(base: Base) = base.cards.contains(this)
}

class Minion(name: String, faction: Faction, startingStrength: Int, owner: Player) extends DeckCard(name, faction, owner) {
  var bonuses = Set[Bonus]()
  def isOnTable() = base.isDefined
  def strength() = startingStrength
    + bonuses.map(_.getBonus(this)).sum
    + (if (isOnBase) owner.bonuses.map(_.getBonus(this)).sum else 0)
    + base.map(_.bonuses.map(_.getBonus(this)).sum).getOrElse(0)
  def play(base: Base) {}
  def destroy(destroyer: Player) {
    moveToDiscard
  }
  def beforeScore(base: Base) {}
  def afterScore(base: Base, newBase: Base) {}
}

class Action(name: String, faction: Faction, owner: Player) extends DeckCard(name, faction, owner) {
  def play(user: Player) {}
  def beforeScore(base: Base) {}
  def afterScore(base: Base, newBase: Base) {}
}

trait Bonus {
  def getBonus(minion: Minion): Int
}

object Bonus {
  def apply(func: Minion => Int) = new Bonus { def getBonus(minion: Minion) = func(minion) }
  def apply(value: Int) = new Bonus { def getBonus(minion: Minion) = value }
}

trait Effect {
  def expire(): Unit
}

object Effect {
  def apply(func: => Unit) = new Effect { def expire() = func }
}

trait Move {
  def isPlayable(user: Player): Boolean
  def play(user: Player, callback: Callback)
}

class PlayMinion extends Move {
  def isPlayable(user: Player) = user.hand.exists(_.isInstanceOf[Minion])
  def play(user: Player, callback: Callback) {
    val m = callback.selectMinion(user.hand.contains(_))
    if (m.isEmpty) return
    val b = callback.selectBase(_ => true)
    if (b.isEmpty) return
    b.get.cards += m.get
    m.get.play(b.get)
  }
}

class PlayAction extends Move {
  def isPlayable(user: Player) = user.hand.exists(_.isInstanceOf[Action])
  def play(user: Player, callback: Callback) {
    val a = callback.selectAction(user.hand.contains(_))
    if (a.isEmpty) return
    val b = callback.selectBase(_ => true)
    if (b.isEmpty) return
    a.get.play(user)
  }
}