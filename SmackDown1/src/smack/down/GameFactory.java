package smack.down;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import smack.down.aliens.*;
import smack.down.bases.*;
import smack.down.dinosaurs.*;
import smack.down.ninjas.*;
import smack.down.pirates.*;
import smack.down.robots.*;
import smack.down.tricksters.*;
import smack.down.wizards.*;
import smack.down.zombies.*;

public class GameFactory {
	
	public Table create(List<PlayerInfo> playerInfos) {
		List<Player> players = new ArrayList<Player>(playerInfos.size());
		
		for (PlayerInfo info : playerInfos) {
			List<DeckCard> cards = new ArrayList<DeckCard>();
			
			for (Faction faction : info.factions)
				cards.addAll(Arrays.asList(get(faction)));
			
			Collections.shuffle(cards);
			Player player = new Player(
				info.name,
				new CardList(cards.toArray(new DeckCard[0])),
				new CardSet(),
				new CardSet());
			
			for (DeckCard card : cards)
				card.setOwner(player);
			
			player.draw(5);
			players.add(player);
		}
		
		List<Base> bases = new ArrayList<Base>(Arrays.asList(allBases()));
		Collections.shuffle(bases);
		List<Base> startingBases = new ArrayList<Base>(players.size() + 1);
		
		for (int i = 0; i < players.size() + 1; ++i)
			startingBases.add(bases.remove(bases.size() - 1));
		
		Table table = new Table(players, bases, startingBases);
		
		for (Player player : players)
			player.setTable(table);
		
		return table;
	}
	
	public static class PlayerInfo {
		private String name;
		private List<Faction> factions;
		
		public PlayerInfo(String name, List<Faction> factions) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public List<Faction> getFactions() {
			return new ArrayList<Faction>(factions);
		}
	}
	
	private static Base[] allBases() {
		return new Base[] {
			new CaveOfShinies(),
			new CentralBrain(),
			new EvansCityCemetery(),
			new Factory2341337(),
			new GreatLibrary(),
			new GreyOpal(),
			new Homeworld(),
			new JungleOasis(),
			new Mothership(),
			new MushroomKingdom(),
			new NinjaDoku(),
			new RhodesPlazaMall(),
			new SchoolOfWizardry(),
			new TarPits(),
			new TempleOfGoju(),
			new Tortuga()
		};
	}
	
	private static DeckCard[] aliens() {
		return new DeckCard[] {
			new Collector(), new Collector(), new Collector(),
			new Invader(), new Invader(),
			new Scout(), new Scout(),
			new SupremeOverlord()
		};
	}
	
	private static DeckCard[] dinosaurs() {
		return new DeckCard[] {
			new WarRaptor(), new WarRaptor(), new WarRaptor(),
			new ArmorStego(), new ArmorStego(),
			new Laseratops(), new Laseratops(),
			new KingRex()
		};
	}
	
	private static DeckCard[] ninjas() {
		return new DeckCard[] {
			new NinjaAcolyte(), new NinjaAcolyte(), new NinjaAcolyte(),
			new Shinobi(), new Shinobi(),
			new TigerAssassin(), new TigerAssassin(),
			new NinjaMaster()
		};
	}
	
	private static DeckCard[] pirates() {
		return new DeckCard[] {
			new FirstMate(), new FirstMate(), new FirstMate(),
			new SaucyWench(), new SaucyWench(),
			new Buchaneer(), new Buchaneer(),
			new PirateKing()
		};
	}
	
	private static DeckCard[] robots() {
		return new DeckCard[] {
				new MicrobotGuard(), new MicrobotArchive(), new MicrobotAlpha(),
				new Zapbot(), new Zapbot(),
				new Warbot(), new Warbot(),
				new Nukebot()
		};
	}
	
	private static DeckCard[] tricksters() {
		return new DeckCard[] {
				new Gremlin(), new Gremlin(), new Gremlin(),
				new Gnome(), new Gnome(),
				new Brownie(), new Brownie(),
				new Leprechaun()
		};
	}
	
	private static DeckCard[] wizards() {
		return new DeckCard[] {
			new Neophyte(), new Neophyte(), new Neophyte(),
			new Chronomage(), new Chronomage(),
			new Archmage()
		};
	}
	
	private static DeckCard[] zombies() {
		return new DeckCard[] {
			new TenaciousZ(), new TenaciousZ(), new TenaciousZ(),
			new Walker(), new Walker(),
			new GraveDigger(), new GraveDigger(),
			new ZombieLord()
		};
	}
	
	private static DeckCard[] get(Faction faction) {
		switch (faction) {
		case Aliens: return aliens();
		case Dinosaurs: return dinosaurs();
		case Ninjas: return ninjas();
		case Pirates: return pirates();
		case Robots: return robots();
		case Tricksters: return tricksters();
		case Wizards: return wizards();
		case Zombies: return zombies();
		}
		
		throw new RuntimeException("no such faction");
	}
}
