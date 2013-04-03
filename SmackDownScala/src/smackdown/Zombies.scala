package smackdown

object Zombies extends Faction("Zombies") {
  override def bases(table: Table) = List[Base](new RhodesPlazaMall(table), new EvansCityCemetery(table))
  override def cards(owner: Player) = List[DeckCard]()
}

class RhodesPlazaMall(table: Table) extends Base("Rhodes Plaza Mall", Zombies, 24, (0, 0, 0), table) {
  // When this base scores, each player gets +1 point for each minion they have here
  override def afterScore(newBase: Base) {
    minions.groupBy(_.owner).map(x => x._1.points += x._2.size)
  }
}

class EvansCityCemetery(table: Table) extends Base("Evans City Cemetery", Zombies, 20, (5, 3, 2), table) {
  // The winner discards their hand and draws 5 cards
  override def afterScore(newBase: Base) {
    score.filter(_._2._2 == 1).map(_._1).foreach(p => {
      p.hand.map(_.moveToDiscard)
      p.draw(5)
    })
  }
}

class Walker(owner: Player) extends Minion("Walker", Zombies, 2, owner)
// Look at the top card of your deck, you may place it in your discard pile

class TenaciousZ(owner: Player) extends Minion("Tenacious Z", Zombies, 2, owner)
// May be played as an extra minion from the discard. Only one Ten-Z may be played this way per turn
// TODO: need onTurnBegin event so Ten-Z can add Play Ten-Z to player's move list

class GraveDigger(owner: Player) extends Minion("Grave Digger", Zombies, 4, owner)
// You may place a minion from your discard into your hand

class ZombieLord(owner: Player) extends Minion("Zombie Lord", Zombies, 5, owner)
// You may play an extra minion from your discard on each base where you have no minion