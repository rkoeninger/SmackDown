package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

object KillerPlants extends Faction("KillerPlants") {
  override def bases(table: Table) = Set(new Greenhouse(table), new SecretGrove(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[Sprout],
    3.of[WaterLily],
    2.of[WeedEater],
    1.of[VenusManTrap],
    2.of[InstaGrow],
    2.of[SleepSpores],
    1.of[Budding],
    1.of[Blossom],
    1.of[Overgrowth],
    1.of[Entangled],
    1.of[DeepRoots],
    1.of[ChokingVines])
}

class Greenhouse(table: Table) extends Base("The Greenhouse", KillerPlants, 24, (4, 2, 1), table) {
	//The winner may search his or her deck for a minion with power 2 or less and play it on the base that replaces this base.
}

class SecretGrove(table: Table) extends Base("Haunted House", KillerPlants, 21, (3, 2, 1), table) {
	//On your turn, you may play an extra minion of power 2 or less here.
}

class Sprout(owner: Player) extends Minion("Sprout", KillerPlants, 2, owner) {
	//Ongoing: Destroy this card at the start of your turn.  
	//You may search your deck for a minion of power 3 or less, and play it here as an extra minion.  Shuffle your deck.
}

class WaterLily(owner: Player) extends Minion("Water Lily", KillerPlants, 3, owner) {
	//Ongoing: Draw a card at the start of each of your turns.  Only use one Water Lily's ability each turn.
}

class WeedEater(owner: Player) extends Minion("Weed Eater", KillerPlants, 3, owner) {
	//This minion has -2 power on the turn you play it.
}

class VenusManTrap(owner: Player) extends Minion("Venus Man Trap", KillerPlants, 5, owner) {
	//Talent: Search your deck for a minion of power 2 or less and play it here as an extra minion.  Shuffle your deck.
}

class InstaGrow(owner: Player) extends Action("Insta-Grow", KillerPlants, owner) {
	//Play an extra minion.
}

class SleepSpores(owner: Player) extends Action("Sleep Spores", KillerPlants, owner) {
	//Play on a base.  Ongoing: Other players' minions have -1 power here (minimum power 0).
}

class Entangled(owner: Player) extends Action("Entangled", KillerPlants, owner) {
	//Play on a base. Ongoing: Minions may not be moved or returned to hands from any base where you have a minion.  
	//Destroy this card at the start of your turn.  
}

class Budding(owner: Player) extends Action("Budding", KillerPlants, owner) {
	//Choose a minion in play.  Search your deck for a copy of that card and place it into your hand.  Shuffle your deck.
}

class Blossom(owner: Player) extends Action("Blossom", KillerPlants, owner) {
	//Play up to three extra minions that all have the same name.
}

class Overgrowth(owner: Player) extends Action("Overgrowth", KillerPlants, owner) {
	//Play on a base.  Ongoing: At the start of your turn, reduce the breakpoint of this base to 0.
}

class DeepRoots(owner: Player) extends Action("Deep Roots", KillerPlants, owner){
	//Play on a base.  Ongoing: Your minions here cannot be moved or returned to your hand by other players' abilities.
}

class ChokingVines(owner: Player) extends Action("Choking Vines", KillerPlants, owner) {
	//Play on a minion.  Ongoing: At the start of your turn, destroy this minion.
}