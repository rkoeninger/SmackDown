# Effects Resolution System Design

So the hard part of implementing this game seems to be the effects system. Given a first impression, it looks like a card, when played, has some effect on the game state. Here's code based on the first impression:

``` scala

class SaucyWench extends Minion {
  def play(base: Base) {
    for (target <- owner.choose.minion.onBase(base).powerAtMost(2))
      target.destory()
  }
}

class Minion {
  def destory() { moveToDiscard() }
}

```

But then recall that effects can happen at the same time and need to be resolved by the current player. So we can't just run the `play` method, we need to collect a list of effects and ask the player to choose their order if the list has more than one item in it. So `play` now returns an `Effect` object:

``` scala

class SaucyWench extends Minion {
  def play(base: Base) = new Effect(this, "May destroy minion power 2 or less on {base}") {
    for (target <- owner.choose.minion.onBase(base).powerAtMost(2))
      target.destory()
  }
}

class Player {
  def play(minion: Minion, base: Base) {
    var effects = List(minion.play(base), base.minionPlayed(minion))
    resolveEffects(effects)
  }
  
  def resolveEffects(effects: List[Effect]) {
    // then have player choose items in `effects` until the list is empty
  }
}

```

But what about cards that respond to effects? If it's the card being destroyed, we can just override its `destroy` method:

``` scala

class Gremlin extends Minion {
  // Ongoing: After this minion is destroyed, draw a card
  // and each other player discards a random card.
  override def destroy() {
    owner.draw()
    for (p <- owner.otherPlayers)
      p.randomDiscard()
  }
}

```

But, of course, that would have to return an `Effect` object as well, so concurrent effects can be resolved at the current players' specified order.

``` scala

class Gremlin extends Minion {
  override def destroy() = new Effect(this, "Owner draws a card and each other player discards a random card") {
    super.destroy()
    owner.draw()
    for (p <- owner.otherPlayers)
      p.randomDiscard()
  }
}

// and

class Warbot extends Minion {
  // Ongoing: this minion cannot be destroyed
  override def destroy() = NoEffect
}

```

But what if other cards need to respond to the destruction of the minion? I guess we could go around and tell them all about it and collect the responses:

``` scala

class Minion {
  def destory() {
    val effects = allOtherCardsInTheGame.map(c => c.onMinionDestroyed(this))
    currentPlayer.resolveEffects(effects)
    moveToDiscard()
  }
}

```

But what if a card affects what kind of effects a card can have, like General Ivan ("Ongoing: Your minions cannot be destroyed")? We would have to be able to tell what kind of effects a card is going to have before we actually run its `play` method or resolve its `Effect` object.

Normally, the effect a card has is _inherent_ in its code. But that's not something that can be examined by the machine too well. We need a way to _reify_ the effects a card has so other cards can determine if the effect is something they need to respond to or pre-empt.

``` scala

sealed trait Effect(cause: Cause)
case class DestroyMinion(cause: Cause, target: Minion)
case class DestroyAction(cause: Cause, target: Action)
case class Move(cause: Cause, target: Minion, destination: Base)
case class ReturnToHand(cause: Cause, target: Minion)

sealed trait Cause
case object BaseCause extends Cause
case class PlayerCause(player: Player, card: Card)

sealed trait Response(cause: Cause)
case class Negate(cause: Cause)
case class NoResponse(cause: Cause)

class SaucyWench extends Minion {
  def play(base: Base) = {
    for (target <- owner.choose.minion.onBase(base).powerAtMost(2))
      return DestroyMinion(PlayerCause(owner, this), target)
    return NoEffect
  }
}

class GeneralIvan extends Minion {
  def respond(effect: Effect) = {
    return effect match {
      case dm: DestroyMinion if (dm.target.owner == owner) => Negate
      case _ => NoResponse
    }
}

class Overrun extends Action {
  def preempt(effect: Effect) {
    return effect match {
      case: pm: PlayMinion if (pm.minion.owner != owner) && (pm.base == attachedBase) => Prevent
      case _ => NoResponse
    }
  }
}

```

Probably need another level of effects/responses to handle something like Mark of Sleep. There would have to be a floating effect in play that pre-empts the playing of any actions by that player.

What other cards are "problem cards" that still don't fit into this model?
