package smackdown

import scala.util.Random
import Utils._

object Steampunks extends Faction("Steampunks") {
  override def bases(table: Table) = Set(new InventorsSalon(table), new Workshop(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[CaptainAhab],
    3.of[SteamMan],
    2.of[Mechanic],
    1.of[SteamQueen],
    2.of[Zeppelin],
    2.of[ScrapDiving],
    1.of[RotarySlugThrower],
    1.of[Accromotive],
    1.of[DifferenceEngine],
    1.of[OrnateDome],
    1.of[ChangeOfVenue],
    1.of[EscapeHatch])
}

class InventorsSalon(table: Table) extends Base("Inventor's Salon", Steampunks, 22, (4, 2, 1), table) {
	//The winner may take an action from his or her discard pile and place it in his or her hand.
}

class Workshop(table: Table) extends Base("Workshop", Steampunks, 20, (4, 2, 1), table) {
	//When a player plays an action card on this base, that player may play an extra action.
}

class CaptainAhab(owner: Player) extends Minion("Captain Ahab", Steampunks, 2, owner) {
	//Talent: Move this minion to a base that has one of your actions played on it.
}

class SteamMan(owner: Player) extends Minion("Steam Man", Steampunks, 3, owner) {
	//Ongoing: Has +1 power if this base has one of your actions played on it.
}

class Mechanic(owner: Player) extends Minion("Mechanic", Steampunks, 4, owner) {
	//Choose an action in your discard pile that can be played on a base and play it as an extra action.
}

class SteamQueen(owner: Player) extends Minion("Steam Queen", Steampunks, 5, owner) {
	//Ongoing: Your actions are not affected by other players' actions.
}

class Zeppelin(owner: Player) extends Action("Zeppelin", Steampunks, owner) {
	//Play on a base.  Talent: Move one of your minions from another base to here, or from here to another base.
}

class ScrapDiving(owner: Player) extends Action("Scrap Diving", Steampunks, owner) {
	//Place an action from your discard pile into your hand.
}

class RotarySlugThrower(owner: Player) extends Action("Rotary Slug Thrower", Steampunks, owner) {
	//Play on a base.  Ongoing: Your minions here each have +2 power.
}

class Accromotive(owner: Player) extends Action("Accromotive", Steampunks, owner) {
	//Play on a base.  Ongoing: If you have a minion here, you have +5 power here.
}

class DifferenceEngine(owner: Player) extends Action("Difference Engine", Steampunks, owner) {
	//Play on a base.  Ongoing: If you have a minion here, draw an extra card at the end of your turn.
}

class OrnateDome(owner: Player) extends Action("Ornate Dome", Steampunks, owner) {
	//Play on a base.  Destroy all other players' actions played here.  Ongoing: Other players cannot play actions on this base.
}

class ChangeOfVenue(owner: Player) extends Action("Change Of Venue", Steampunks, owner){
	//Place one of your actions that is on a base or minion into your hand.  Play it again as an extra action.
}

class EscapeHatch(owner: Player) extends Action("Escape Hatch", Steampunks, owner) {
	//Play on a base.  Ongoing: When your minions here are destroyed, place them into your hand instead of the discard pile.
}