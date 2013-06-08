package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

object Ghosts extends Faction("Ghosts") {
  override def bases(table: Table) = Set(new DreadGazebo(table), new HauntedHouse(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Ghost],
    3.of[Spirit],
    2.of[Haunting],
    1.of[Specter],
    2.of[GhastlyArrival],
    2.of[Incorporeal],
    1.of[ShadyDeal],
    1.of[TheDeadRise],
    1.of[DoorToTheBeyond],
    1.of[MakeContact],
    1.of[AcrossTheDivide],
    1.of[Seance])
}

class DreadGazebo(table: Table) extends Base("The Dread Gazebo", Ghosts, 20, (4, 2, 1), table) {
	//Players may not play actions on this base.
}

class HauntedHouse(table: Table) extends Base("Haunted House", Ghosts, 18, (4, 3, 2), table) {
	//When a player plays a minion here, that player must discard 1 card.
}

class Ghost(owner: Player) extends Minion("Ghost", Ghosts, 2, owner) {
	//You may discard a card from your hand.
}

class Spirit(owner: Player) extends Minion("Spirit", Ghosts, 3, owner) {
	//Choose a minion.  You may discard cards equal to its power to destroy it.
}

class Haunting(owner: Player) extends Minion("Haunting", Ghosts, 3, owner) {
	//Ongoing: If you have two or fewer cards in your hand, this minion has +3 power and is not affected by other players' cards.
}

class Specter(owner: Player) extends Minion("Specter", Ghosts, 5, owner) {
	//Special: If you have 2 or fewer cards in your hand, any time you can play a minion, you can play this card from your discard pile instead.
}

class GhastlyArrival(owner: Player) extends Action("Ghastly Arrival", Ghosts, owner) {
	//You may play an extra minion and/or an extra action.
}

class Incorporeal(owner: Player) extends Action("Incorporeal", Ghosts, owner) {
	//Play on one of your minions.  Ongoing: This minion is not affected by other players' cards.
}

class ShadyDeal(owner: Player) extends Action("Shady Deal", Ghosts, owner) {
	//If you have two or fewer cards in your hand, gain 1 VP.
}

class TheDeadRise(owner: Player) extends Action("The Dead Rise", Ghosts, owner) {
	//Discard any number of cards.  Play an extra minion from your discard pile with power less than the number of cards you discarded.
}

class DoorToTheBeyond(owner: Player) extends Action("Door To The Beyond", Ghosts, owner) {
	//Play on a base.  Ongoing: If you have two or fewer cards in your hand, each of your minions here has +2 power.
}

class MakeContact(owner: Player) extends Action("Make Contact", Ghosts, owner) {
	//You can only play this card if its the last card in your hand.  Play on a minion.  
	//Ongoing: Treat this minion as yours while it and this card are in play.
}

class AcrossTheDivide(owner: Player) extends Action("Across The Divide", Ghosts, owner){
	//Choose a card name.  Place any number of minions with that name from your discard pile into your hand.
}

class Seance(owner: Player) extends Action("Seance", Ghosts, owner) {
	//If you have 2 or fewer cards in your hand, draw until you have 5 cards.
}