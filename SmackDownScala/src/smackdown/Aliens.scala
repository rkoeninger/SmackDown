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

class Homeworld(table: Table) extends Base("The Homeworld", Aliens, 12, (4, 2, 1), table)

class Mothership(table: Table) extends Base("The Mothership", Aliens, 12, (4, 2, 1), table)

class Collector(owner: Player) extends Minion("Collector", Aliens, 2, owner)

class Scout(owner: Player) extends Minion("Scout", Aliens, 3, owner)

class Invader(owner: Player) extends Minion("Invader", Aliens, 3, owner) {
  override def play(base: Base) { owner.points += 1 }
}

class SupremeOverlord(owner: Player) extends Minion("Supreme Overlord", Aliens, 5, owner)

class Abduction(owner: Player) extends Action("Abduction", Aliens, owner)

class CropCircles(owner: Player) extends Action("Crop Circles", Aliens, owner)