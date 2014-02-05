package smackdown

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.util.Random
import Utils._

object BearCavalry extends Faction("Bear Cavalry") {
  override def bases(table: Table) = Set(new TsarsPalace(table), new FieldOfHonor(table))
  override def cards(owner: Player) = Deck(owner,
    4.of[CubScout],
    3.of[BearCavalry],
    2.of[PolarCommando],
    1.of[GeneralIvan],
    2.of[Commision],
    2.of[YoureScrewed],
    1.of[BearHug],
    1.of[BearNecessities],
    1.of[Superiority],
    1.of[HighGround],
    1.of[YourePrettyMuchBorscht],
    1.of[BearRidesYou])
}

class TsarsPalace(table: Table) extends Base("Tsar's Palace", BearCavalry, 22, (5, 3, 2), table) {
	//Minions of power 2 or less cannot be played here.
}

class FieldOfHonor(table: Table) extends Base("Field Of Honor", BearCavalry, 18, (3, 2, 1), table) {
	//When you destroy a minion, you gain 1 VP.
}

class CubScout(owner: Player) extends Minion("Cub Scout", BearCavalry, 3, owner) {
	//Ongoing: After another player's minion moves here, if it has less power than this minion, destroy it.
}

class BearCavalry(owner: Player) extends Minion("Bear Cavalry", BearCavalry, 3, owner) {
	//Move another player's minion from here to another base.
}

class PolarCommando(owner: Player) extends Minion("Polar Commando", BearCavalry, 4, owner) {
	//Ongoing: If this is your only minion on this base, it has +2 power and cannot be destroyed.
}

class GeneralIvan(owner: Player) extends Minion("General Ivan", BearCavalry, 6, owner) {
	//Ongoing: Your minions cannot be destroyed.
}

class Commision(owner: Player) extends Action("Commission", BearCavalry, owner) {
	//Play an extra minion. Then move another player's minion from the extra minion's base to another base.
}

class YoureScrewed(owner: Player) extends Action("You're Screwed", BearCavalry, owner) {
	//Choose a base where you have a minion. Move another player's minion from there to another base.
}

class YourePrettyMuchBorscht(owner: Player) extends Action("You're Pretty Much Borscht", BearCavalry, owner) {
	//Choose a base where you have a minion. Move all other player's minions from there to one other base.
}

class BearHug(owner: Player) extends Action("Bear Hug", BearCavalry, owner) {
	//Each other player destroys one of his or her minions with the least power (owner chooses in case of ties).
}

class BearNecessities(owner: Player) extends Action("Bear Necessities", BearCavalry, owner) {
	//Destroy an action that has been played on a minion or base.
}

class Superiority(owner: Player) extends Action("Superiority", BearCavalry, owner) {
	//Play on a base. Ongoing: Your minions here cannot be destroyed, moved, or returned to your hand or deck by other players' cards.
}

class HighGround(owner: Player) extends Action("High Ground", BearCavalry, owner){
	//Play on a base. Ongoing: If you have a minion here, destroy any other player's minion that moves here.
}

class BearRidesYou(owner: Player) extends Action("Bear Rides You", BearCavalry, owner) {
	//Move one of your minions to another base.
}