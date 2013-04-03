package smackdown

object Ninjas extends Faction("Ninjas") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}