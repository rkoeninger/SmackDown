package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

class Table {
  var players = List[Player]()
  var currentPlayer = null // FIXME: don't use null!
  var baseDrawPile = List[Base]()
  var basesInPlay = Set[Base]()
  var baseDiscardPile = Set[Base]()
  def minions() = basesInPlay.flatMap(_.minions)
  def actions() = basesInPlay.flatMap(_.actions) ++ minions().flatMap(_.actions())
  def factions() = Set[Faction]()
}

class Player(val name: String, val factions: List[Faction], val table: Table, val callback: Callback) {
  private var pointsGained = 0
  
  def points = pointsGained
  def addPoints(amount: Int) {
    val oldPoints = pointsGained
    pointsGained += amount
    callback.pointsGained(this, oldPoints, pointsGained)
  }
  
  var hand = Set[DeckCard]()
  var discardPile = Set[DeckCard]()
  var drawPile = List[DeckCard]()
  var bonuses = Set[Bonus]()
  var moves = Set[Move]()
  var onTurnBeginSet = Set[Unit => Unit]()
  var onTurnEndSet = Set[Unit => Unit]()
  
  def onTurnBegin(todo: => Unit) { onTurnBeginSet += (_ => todo) }
  
  def onTurnEnd(todo: => Unit) { onTurnEndSet += (_ => todo) }
  
  def minionsInPlay() = table.basesInPlay.flatMap(_.minions).filter(_.owner == this)
  
  def beginTurn() {
    moves = Set(new PlayMinion(), new PlayAction())
    
    onTurnBeginSet.foreach(_.apply())
    onTurnBeginSet = Set[Unit => Unit]()
  }
  
  def endTurn() {
    moves = Set()
    
    onTurnEndSet.foreach(_.apply())
    onTurnEndSet = Set[Unit => Unit]()
    
    draw(2)
    
    while (hand.size > 10)
      for (c <- choose.card.inHand) c --> Discard
  }
  
  def draw() {
    if (replenishDrawPile) { if (! drawPile.isEmpty) drawPile(0) --> Hand }
  }
  
  def draw(count: Int) {
    (1 to count).foreach(x => draw)
    // without the x =>, the draw(Int) version will be used, causing infinite loop
  }
  
  def reveal() = if (replenishDrawPile) {
      val card = drawPile(0)
      callback.reveal(card)
      Some(card)
    }
    else None
  
  def reveal(count: Int) = if (replenishDrawPile) {
      val actualCount = math.min(count, drawPile.size)
      val cards = drawPile.take(actualCount).toSet
      callback.reveal(cards)
      cards
    }
    else Set[Card]()
    
  def peek() = if (replenishDrawPile) Some(drawPile(0)) else None
  
  def shuffle() {
    drawPile = Random.shuffle(drawPile)
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
      hand = hand.toList.dropIndex(Random.nextInt(hand.size)).toSet
  }
  
  def randomDiscard(count: Int) {
    (1 to count).foreach(x => randomDiscard)
  }
  
  def otherPlayers() = table.players.filterNot(_ == this)
  
  def playMinion() {
    val move = new PlayMinion()
    if (move.isPlayable(this))
      move.play(this)
  }
  
  def playMinion(maxStrength: Int) {
    val move = new PlayMinion(maxStrength)
    if (move.isPlayable(this))
      move.play(this)
  }
  
  def playMinion(base: Base) {
    val move = new PlayMinionOnBase(base)
    if (move.isPlayable(this))
      move.play(this)
  }
  
  def playMinion(m: Minion) {
    for (m <- choose.minion.inHand;
         b <- choose.base.inPlay) {
      m.play(b)
      b.cards += m
    }
  }
  
  def playAction() {
    val move = new PlayAction()
    if (move.isPlayable(this))
      move.play(this)
  }
  
  def playAction(a: Action) {
    a.play(this)
  }
  
  def choose = new {
    def player = new PlayerChoice(Player.this, table.players.toSet)
    def faction = new Choice[Faction](Player.this, table.factions)
    def card = new {
      def inHand = new Choice[DeckCard](Player.this, Player.this.hand)
      def inDiscardPile = new Choice[DeckCard](Player.this, Player.this.discardPile)
    }
    def base = new {
      def inPlay = new BaseChoice(Player.this, table.basesInPlay)
      def inDeck = new BaseChoice(Player.this, table.baseDrawPile.toSet)
    }
    def action = new {
      def inHand = new ActionChoice(Player.this, Player.this.hand.actions)
      def inPlay = new ActionChoice(Player.this, table.actions)
      def inDrawPile = new ActionChoice(Player.this, Player.this.drawPile.actions.toSet)
      def inDiscardPile = new ActionChoice(Player.this, Player.this.discardPile.actions)
      def onBase(base: Base) = new ActionChoice(Player.this, base.actions)
      def onMinion(minion: Minion) = new ActionChoice(Player.this, minion.actions)
    }
    def minion = new {
      def inHand = new MinionChoice(Player.this, Player.this.hand.minions)
      def inHand(player: Player) = new MinionChoice(Player.this, player.hand.minions)
      def inDiscardPile = new MinionChoice(Player.this, Player.this.discardPile.minions)
      def inPlay = new MinionChoice(Player.this, table.minions)
      def onBase(base: Base) = new MinionChoice(Player.this, base.minions)
    }
  }
  
  def chooseYesNo = callback.confirm
}

trait Callback {
  def choose[T](options: Set[T]): Option[T] = None
  def chooseOrder[T](options: List[T]): List[T] = options
  def chooseAny[T](options: Set[T]): Set[T] = Set()
  def confirm(): Boolean = false
  def reveal(card: DeckCard) {}
  def reveal(cards: Set[DeckCard]) {}
  def pointsGained(player: Player, oldPoints: Int, newPoints: Int) {}
}

abstract class Faction(val name: String) {
  def bases(table: Table): Set[Base]
  def cards(owner: Player): Set[DeckCard]
}

abstract class Card(val name: String, val faction: Faction)

sealed trait Destination

object Hand extends Destination
object Discard extends Destination
object DrawTop extends Destination
object DrawBottom extends Destination

class Base(name: String, faction: Faction, startingBreakPoint: Int, val scoreValues: (Int, Int, Int), val table: Table)
extends Card(name, faction) with Destination {
  
  var cards = Set[DeckCard]()
  def minions() = cards.ofType[Minion]
  def actions() = cards.ofType[Action]
  var minionBonuses = Set[Bonus]()
  var bonuses = Set[BreakPointBonus]()
  def breakPoint() = math.max(0, startingBreakPoint + bonuses.map(_.getBonus(this)).sum)
  
  def totalStrength() = minions.map(_.strength).sum
  
  def isInPlay() = table.basesInPlay.contains(this)
  
  def minionPlayed(minion: Minion) {}
  def minionMovedHere(minion: Minion) {}
  def minionMovedAway(minion: Minion) {}
  def minionDestroyed(minion: Minion, base: Base) {}
  def onTurnBegin(player: Player) {}
  def beforeScore() {}
  def onScore() {}
  def afterScore(newBase: Base) {}
  
  /**
   * Returns a mapping of players to (scoreReward, ranking) where the score reward is one of the
   * three values in scoreValues and the ranking is 1 for winner, 2 for runner-up, etc.
   * Rank values are 1, 2, 3... regardless of ties. If there is a tie for 1st and a third player
   * with less strength, the two players with greater strength will get scoreValues[0] and rank 1,
   * the weaker player will get scoreValues[2] and rank 2.
   */
  def score() = {
    val playerStrengths = minions.groupBy(_.owner).map(x => (x._1, x._2.map(_.strength).sum))
    val sortedStrengths = playerStrengths.values.toList.distinct.sorted.reverse
    var rewardCount = 0
    var rewardRank = 1
    
    val ranks = sortedStrengths.map(strength =>
      if (rewardCount < 3) Some {
        val reward = scoreValues.productElement(rewardCount).as[Int]
        val rewardGroup = playerStrengths.filter(_._2 == strength).map(x => new Rank(x._1, reward, rewardRank)).toSet
        rewardCount += rewardGroup.size
        rewardRank += 1
        rewardGroup
      }
      else None
    )
    
    ranks.flatten.foldLeft(Set[Rank]())(_ ++ _)
  }
}

case class Rank(val player: Player, val score: Int, val rank: Int) {
  def winner() = rank == 1
  def runnerUp() = rank == 2
}

abstract class DeckCard(name: String, faction: Faction, val owner: Player) extends Card(name, faction) {
  var base: Option[Base] = None
  
  def table() = owner.table
  
  def -->(dest: Destination) = dest match {
    case Hand => if (! isInHand) {
      remove
      owner.hand += this
    }
    case Discard => if (! isInDiscardPile) {
      remove
      owner.discardPile += this
    }
    case DrawTop => {
      remove
      owner.drawPile = this :: owner.drawPile
    }
    case DrawBottom => {
      remove
      owner.drawPile = owner.drawPile :+ this
    }
    case base: Base => {
      remove
      if (this.base != Some(base)) {
        base.cards += this
        this.base = Some(base)
      }
    }
    case _ => { sys.error("Invalid DeckCard destination") }
  }
  protected def remove() {
    if (isInHand) owner.hand -= this
    else if (isInDrawPile) owner.drawPile = owner.drawPile.filterNot(_ == this)
    else if (isInDiscardPile) owner.discardPile -= this
    else if (isOnBase) {
      base.foreach(_.cards -= this)
      base = None
    }
  }
  def isInHand() = owner.hand.contains(this)
  def isInDrawPile() = owner.drawPile.contains(this)
  def isInDiscardPile() = owner.discardPile.contains(this)
  def isOnBase() = base.isDefined
  def isOnBase(base: Base) = base.cards.contains(this)
  def beginTurn() {}
  def endTurn() {}
}

class Minion(name: String, faction: Faction, startingStrength: Int, owner: Player)
extends DeckCard(name, faction, owner) with Destination {
  
  var bonuses = Set[Bonus]()
  var actions = Set[Action]()
  def isOnTable() = base.isDefined
  def strength() = startingStrength
    + bonuses.map(_.getBonus(this)).sum
    + (if (isOnBase) owner.bonuses.map(_.getBonus(this)).sum else 0)
    + base.map(_.minionBonuses.map(_.getBonus(this)).sum).getOrElse(0)
  def play(base: Base) {}
  def destructable() = true
  def destroy(destroyer: Player) {
    if (destructable) this --> Discard
  }
  def beforeScore(base: Base) {}
  def afterScore(base: Base, newBase: Base) {}
  def minionDestroyed(minion: Minion, base: Base) {}
  def minionPlayed(minion: Minion) {}
}

class Action(name: String, faction: Faction, owner: Player) extends DeckCard(name, faction, owner) {
  override def -->(dest: Destination) = dest match {
    case minion: Minion => {
      remove
      if (this.minion != Some(minion)) {
        minion.actions += this
        this.minion = Some(minion)
      }
    }
    case _ => super .-->(dest)
  }
  protected override def remove() {
    if (isOnMinion) {
      minion.foreach(_.actions -= this)
      minion = None
    } else {
      super.remove()
    }
  }
  var minion: Option[Minion] = None
  def isOnMinion() = minion.isDefined
  def play(user: Player) {}
  def beforeScore(base: Base) {}
  def afterScore(base: Base, newBase: Base) {}
  def destroy(card: Card) {}
}

trait Bonus {
  def getBonus(minion: Minion): Int
}

object Bonus {
  def apply(func: Minion => Int) = new Bonus { def getBonus(minion: Minion) = func(minion) }
  def apply(value: Int) = new Bonus { def getBonus(minion: Minion) = value }
  def untilTurnEnd(player: Player, value: Int) {
    val bonus = Bonus(value)
    player.bonuses += bonus
    player.onTurnEnd { player.bonuses -= bonus }
  }
  def untilTurnEnd(player: Player, minion: Minion, value: Int) {
    val bonus = Bonus(value)
    minion.bonuses += bonus
    player.onTurnEnd { minion.bonuses -= bonus }
  }
}

trait BreakPointBonus {
  def getBonus(base: Base): Int
}

object BreakPointBonus {
  def apply(func: Base => Int) = new BreakPointBonus { def getBonus(base: Base) = func(base) }
  def apply(value: Int) = new BreakPointBonus { def getBonus(base: Base) = value }
  def untilTurnEnd(player: Player, base: Base, func: Base => Int) {
    val bonus = BreakPointBonus(func)
    base.bonuses += bonus
    player.onTurnEnd { base.bonuses -= bonus }
  }
}

trait Move {
  def isPlayable(user: Player): Boolean
  def play(user: Player)
}

class PlayMinion(maxStrength: Int) extends Move {
  def this() = this(Int.MaxValue)
  def isPlayable(user: Player) = user.hand.exists(m => m.is[Minion] && m.as[Minion].strength <= maxStrength)
  def play(user: Player) {
    for (m <- user.choose.minion.inHand.strengthAtMost(maxStrength);
         b <- user.choose.base.inPlay) {
      m.play(b)
      b.cards += m
    }
  }
}

class PlayMinionOnBase(base: Base) extends Move {
  def isPlayable(user: Player) = user.hand.exists(m => m.is[Minion])
  def play (user: Player) {
    for (m <- user.choose.minion.inHand) {
      m.play(base)
      base.cards += m
    }
  }
}

class PlayAction extends Move {
  def isPlayable(user: Player) = user.hand.exists(_.is[Action])
  def play(user: Player) {
    for (a <- user.choose.action.inHand)
      a.play(user)
  }
}