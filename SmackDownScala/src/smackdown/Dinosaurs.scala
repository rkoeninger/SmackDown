package smackdown

object Dinosaurs extends Faction("Dinosaurs") {
  override def bases(table: Table) = List(new JungleOasis(table), new TarPits(table))
  override def cards(owner: Player) = List[DeckCard]()
}

class JungleOasis(table: Table) extends Base("Jungle Oasis", Dinosaurs, 12, (2, 0, 0), table)

class TarPits(table: Table) extends Base("Tar Pits", Dinosaurs, 16, (4, 2, 1), table)