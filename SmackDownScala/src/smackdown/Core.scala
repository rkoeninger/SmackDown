package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

class Table {
  var players = List[Player]()
  var turn = 0
  def currentPlayer = players(turn)
  var baseDrawPile = List[Base]()
  var basesInPlay = Set[Base]()
  var baseDiscardPile = Set[Base]()
  def minions() = basesInPlay.flatMap(_.minions)
  def actions() = basesInPlay.flatMap(_.actions) ++ minions.flatMap(_.actions)
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
  var moves = Set[Ability]()
  var onTurnBeginSet = Set[Unit => Unit]()
  var onTurnEndSet = Set[Unit => Unit]()
  var abilities = List[Set[Ability]]()
  
  def onTurnBegin(todo: => Unit) { onTurnBeginSet += (_ => todo) }
  
  def onTurnEnd(todo: => Unit) { onTurnEndSet += (_ => todo) }
  
  def minionsInPlay() = table.basesInPlay.flatMap(_.minions).filter(_.owner == this)
  
  def beginTurn() {
    abilities = List(Set(new PlayMinion(), new PlayAction()))
    
    onTurnBeginSet.foreach(_.apply())
    onTurnBeginSet = Set[Unit => Unit]()
  }
  
  def endTurn() {
    moves = Set()
    
    onTurnEndSet.foreach(_.apply())
    onTurnEndSet = Set[Unit => Unit]()
    
    draw(2)
    
    while (hand.size > 10)
      for (c <- choose.card.inHand) c moveTo Discard
  }
  
  def draw() {
    if (replenishDrawPile) { if (! drawPile.isEmpty) drawPile(0) moveTo Hand }
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
    1 to count foreach { x => randomDiscard }
  }
  
  def otherPlayers() = table.players.filterNot(_ == this)
  
  def playMinion() {
    val move = new PlayMinion()
    if (move.isPlayable(this))
      move.play(this)
  }
  
  def playMinion(maxPower: Int) {
    val move = new PlayMinion(maxPower)
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
    def player = new Choice[Player](Player.this, table.players.toSet)
    def faction = new Choice[Faction](Player.this, table.factions)
    def card = new {
      def inHand = new Choice[DeckCard](Player.this, Player.this.hand)
      def inDiscardPile = new Choice[DeckCard](Player.this, Player.this.discardPile)
    }
    def base = new {
      def inPlay = new Choice[Base](Player.this, table.basesInPlay)
      def inDeck = new Choice[Base](Player.this, table.baseDrawPile.toSet)
    }
    def action = new {
      def inHand = new Choice[Action](Player.this, Player.this.hand.actions)
      def inPlay = new Choice[Action](Player.this, table.actions)
      def inDrawPile = new Choice[Action](Player.this, Player.this.drawPile.actions.toSet)
      def inDiscardPile = new Choice[Action](Player.this, Player.this.discardPile.actions)
      def onBase(base: Base) = new Choice[Action](Player.this, base.actions)
      def onMinion(minion: Minion) = new Choice[Action](Player.this, minion.actions)
    }
    def minion = new {
      def inHand = new Choice[Minion](Player.this, Player.this.hand.minions)
      def inHand(player: Player) = new Choice[Minion](Player.this, player.hand.minions)
      def inDiscardPile = new Choice[Minion](Player.this, Player.this.discardPile.minions)
      def inPlay = new Choice[Minion](Player.this, table.minions)
      def onBase(base: Base) = new Choice[Minion](Player.this, base.minions)
    }
  }
  
  def chooseYesNo = callback.confirm
}

trait Callback {
  def choose[T](options: Set[T]): Decision[T] = Impossible
  def chooseOrder[T](options: List[T]): List[T] = options
  def chooseAny[T](options: Set[T]): Set[T] = Set()
  def confirm(): Boolean = false
  def reveal(card: DeckCard) {}
  def reveal(cards: Set[DeckCard]) {}
  def pointsGained(player: Player, oldPoints: Int, newPoints: Int) {}
}

class Choice[A](val me: Player, options: Set[A]) {
  def filter(f: A => Boolean): Choice[A] = new Choice[A](me, options filter f)
  private def decide = me.callback.choose(options)
  def foreach[B](f: A => B) { decide foreach f }
  def map[B](f: A => B) { decide map f }
  def flatMap[B](f: A => Decision[B]) { decide flatMap f }
}

sealed trait Decision[+A] {
  def filter(f: A => Boolean): Decision[A]
  def foreach[B](f: A => B) { map(f) }
  def map[B](f: A => B): Decision[B]
  def flatMap[B](f: A => Decision[B]): Decision[B]
}

object Decision {
  def apply[A](value: A) = Chosen(value)
}

sealed case class Chosen[+A](value: A) extends Decision[A] {
  def filter(f: A => Boolean) = if (f(value)) this else Impossible
  def map[B](f: A => B) = Chosen(f(value))
  def flatMap[B](f: A => Decision[B]) = f(value)
}

case object Impossible extends Decision[Nothing] {
  def filter(f: Nothing => Boolean) = Impossible
  def map[B](f: Nothing => B) = Impossible
  def flatMap[B](f: Nothing => Decision[B]) = Impossible
}

case object Decline extends Decision[Nothing] {
  def filter(f: Nothing => Boolean) = Decline
  def map[B](f: Nothing => B) = Decline
  def flatMap[B](f: Nothing => Decision[B]) = Decline
}

abstract class Faction(val name: String) {
  def bases(table: Table): Set[Base]
  def cards(owner: Player): Set[DeckCard]
}

object Deck {
  def apply(owner: Player, cs: (Player => Set[DeckCard])*) = cs.flatMap(_(owner)).toSet
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
  def minions() = cards.minions
  def actions() = cards.actions
  var minionBonuses = Set[Bonus]()
  var bonuses = Set[BreakPointBonus]()
  def breakPoint() = math.max(0, startingBreakPoint + bonuses.map(_(this)).sum)
  
  def totalPower() = minions.map(_.power).sum
  
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
   * with less power, the two players with greater power will get scoreValues[0] and rank 1,
   * the weaker player will get scoreValues[2] and rank 2.
   */
  def score() = {
    val playerPowers = minions.groupBy(_.owner).map(x => (x._1, x._2.map(_.power).sum))
    val sortedPowers = playerPowers.values.toList.distinct.sorted.reverse
    
    def assignRewards(remainingPowerLevels: List[Int], rewardRank: Int, rewardCount: Int): Set[Rank] =
      if (rewardCount >= 3 || remainingPowerLevels.isEmpty) Set[Rank]()
      else {
        val power = remainingPowerLevels.head
        val reward = scoreValues.productElement(rewardCount).as[Int]
        val rewardGroup = playerPowers.filter(_._2 == power).map(x => new Rank(x._1, reward, rewardRank)).toSet
        rewardGroup ++ assignRewards(remainingPowerLevels.tail, rewardRank + 1, rewardCount + rewardGroup.size)
      }
    
    assignRewards(sortedPowers, 1, 0)
  }
}

case class Rank(val player: Player, val score: Int, val rank: Int) {
  def winner() = rank == 1
  def runnerUp() = rank == 2
}

abstract class DeckCard(name: String, faction: Faction, val owner: Player) extends Card(name, faction) {
  var base: Option[Base] = None
  
  def table() = owner.table
  
  def moveTo(dest: Destination) = dest match {
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

class Minion(name: String, faction: Faction, power0: Int, owner: Player)
extends DeckCard(name, faction, owner) with Destination {
  
  var bonuses = Set[Bonus]()
  var actions = Set[Action]()
  def isOnTable() = base.isDefined
  def power() = power0
    + bonuses.map(_(this)).sum
    + (if (isOnBase) owner.bonuses.map(_(this)).sum else 0)
    + base.map(_.minionBonuses.map(_(this)).sum).getOrElse(0)
  def play(base: Base): Ability = NullAbility
  def destructable() = true
  def destroyBy(destroyer: Player) {
    if (destructable) this moveTo Discard
  }
  def beforeScore(base: Base): Ability = NullAbility
  def afterScore(base: Base, newBase: Base): Ability = NullAbility
  def minionDestroyed(minion: Minion, base: Base) {}
  def minionPlayed(minion: Minion) {}
}

class Action(name: String, faction: Faction, owner: Player) extends DeckCard(name, faction, owner) {
  override def moveTo(dest: Destination) = dest match {
    case minion: Minion => {
      remove
      if (this.minion != Some(minion)) {
        minion.actions += this
        this.minion = Some(minion)
      }
    }
    case _ => super.moveTo(dest)
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
  def play(user: Player): Ability = NullAbility
  def beforeScore(base: Base): Ability = NullAbility
  def afterScore(base: Base, newBase: Base): Ability = NullAbility
  def destroy(card: Card) {}
}

trait Bonus {
  def apply(minion: Minion): Int
}

object Bonus {
  def apply(func: Minion => Int) = new Bonus { def apply(minion: Minion) = func(minion) }
  def apply(value: Int) = new Bonus { def apply(minion: Minion) = value }
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
  def apply(base: Base): Int
}

object BreakPointBonus {
  def apply(func: Base => Int) = new BreakPointBonus { def apply(base: Base) = func(base) }
  def apply(value: Int) = new BreakPointBonus { def apply(base: Base) = value }
  def untilTurnEnd(player: Player, base: Base, func: Base => Int) {
    val bonus = BreakPointBonus(func)
    base.bonuses += bonus
    player.onTurnEnd { base.bonuses -= bonus }
  }
}

class Property[M, A](entity: M, initValue: A) {
  private var modifiers = Set[(M, A) => A]()
  def value = modifiers.foldLeft(initValue)((cur, op) => op(entity, cur))
  def modify(f: (M, A) => A) { modifiers = modifiers + f }
  def remove(f: (M, A) => A) { modifiers = modifiers - f }
}

trait Ability {
  def isPlayable(user: Player): Boolean
  def play(user: Player)
}

object Ability {
  def apply(todo: => Unit) = new Ability() {
    def isPlayable(user: Player) = true
    def play(user: Player) = todo
  }
}

object NullAbility extends Ability {
  def isPlayable(user: Player) = false
  def play(user: Player) {}
}

class PlayMinion(maxPower: Int) extends Ability {
  def this() = this(Int.MaxValue)
  def isPlayable(user: Player) = user.hand.exists(m => m.is[Minion] && m.as[Minion].power <= maxPower)
  def play(user: Player) {
    for (m <- user.choose.minion.inHand.powerAtMost(maxPower);
         b <- user.choose.base.inPlay) {
      m.play(b)
      b.cards += m
    }
  }
}

class PlayMinionOnBase(base: Base) extends Ability {
  def isPlayable(user: Player) = user.hand.exists(m => m.is[Minion])
  def play (user: Player) {
    for (m <- user.choose.minion.inHand) {
      m.play(base)
      base.cards += m
    }
  }
}

class PlayAction extends Ability {
  def isPlayable(user: Player) = user.hand.exists(_.is[Action])
  def play(user: Player) {
    for (a <- user.choose.action.inHand)
      a.play(user)
  }
}