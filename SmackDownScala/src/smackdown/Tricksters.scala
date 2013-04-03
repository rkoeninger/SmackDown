package smackdown

object Tricksters extends Faction("Tricksters") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}