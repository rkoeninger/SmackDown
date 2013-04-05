package smackdown

object Aliens extends Faction("Aliens") {
  override def bases(table: Table) = List(new Homeworld(table), new Mothership(table))
  override def cards(owner: Player) = List(
    new Collector(owner), new Collector(owner), new Collector(owner), new Collector(owner),
    new Scout(owner), new Scout(owner), new Scout(owner),
    new Invader(owner), new Invader(owner),
    new SupremeOverlord(owner),
    new Abduction(owner),
    new CropCircles(owner)
  )
}

class Homeworld(table: Table) extends Base("The Homeworld", Aliens, 23, (4, 2, 1), table) {
  // after each time a minion is played here,
  // its owner my play an extra minion of power 2 or less
}

class Mothership(table: Table) extends Base("The Mothership", Aliens, 20, (4, 2, 1), table) {
  // after this base scores, the winner may return one of his or her minions
  // power 3 or less from here to their hand
}

class Collector(owner: Player) extends Minion("Collector", Aliens, 2, owner) {
  // you may return a minion power 2 or less to its owners hand
}

class Scout(owner: Player) extends Minion("Scout", Aliens, 3, owner) {
  // after this base is scored, you may place in minion in your hand instead of the discard pile
}

class Invader(owner: Player) extends Minion("Invader", Aliens, 3, owner) {
  // you gain 1 point
  override def play(base: Base) { owner.points += 1 }
}

class SupremeOverlord(owner: Player) extends Minion("Supreme Overlord", Aliens, 5, owner) {
  // you may return a minion to its owners hand
}

class Abduction(owner: Player) extends Action("Abduction", Aliens, owner) {
  // return a minion to its owners hand. play an extra minion
}

class Disintegrator(owner: Player) extends Action("Disintegrator", Aliens, owner) {
  // place a minion power 3 or less on the bottom of its owners draw pile
}

class CropCircles(owner: Player) extends Action("Crop Circles", Aliens, owner) {
  // choose a base. return each minion on that base to its owners hand
  override def play(user: Player) {
    user.callback.selectBase(_.isInPlay).foreach(_.minions.foreach(_.moveToHand))
  }
}

class JammedSignal(owner: Player) extends Action("Jammed Signal", Aliens, owner) {
  // play on a base. ongoing: all players ignore this base's ability
}

class Probe(owner: Player) extends Action("Probe", Aliens, owner) {
  // look at an opponent's hand and choose a minion in it. that player discards that minion
}

class Terraforming // TODO: need card text

class BeamUp(owner: Player) extends Action("Beam Up", Aliens, owner) {
  // return a minion to its owners hand
}

class Invasion(owner: Player) extends Action("Invasion", Aliens, owner) {
  // move a minion to another base
}