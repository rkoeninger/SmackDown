package smackdown

object Wizards extends Faction("Wizards") {
  override def bases(table: Table) = List[Base]()
  override def cards(owner: Player) = List[DeckCard]()
}