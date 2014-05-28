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
    // then have player choose items in `effects` until the list is empty
  }
}

```

event system

event reification
