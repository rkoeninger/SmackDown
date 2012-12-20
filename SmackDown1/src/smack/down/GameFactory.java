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
			Player player = new Player(info.name, new Deck(cards), new ArrayList<DeckCard>());

			for (DeckCard card : cards)
				card.setOwner(player);
			
			player.draw(5);
			players.add(player);
		}
		
		List<Base> bases = Arrays.asList(allBases);
		Collections.shuffle(bases);
		
		return new Table(players, bases);
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
	
	private static final Base[] allBases = {
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
	
	private static final DeckCard[] aliens = {
		new Collector(), new Collector(), new Collector(),
		new Invader(), new Invader(),
		new Scout(), new Scout(),
		new SupremeOverlord()
	};
	
	private static final DeckCard[] dinosaurs = {
		new WarRaptor(), new WarRaptor(), new WarRaptor(),
		new ArmorStego(), new ArmorStego(),
		new Laseratops(), new Laseratops(),
		new KingRex()
	};
	
	private static final DeckCard[] ninjas = {
		new NinjaAcolyte(), new NinjaAcolyte(), new NinjaAcolyte(),
		new Shinobi(), new Shinobi(),
		new TigerAssassin(), new TigerAssassin(),
		new NinjaMaster()
	};
	
	private static final DeckCard[] pirates = {
		new FirstMate(), new FirstMate(), new FirstMate(),
		new SaucyWench(), new SaucyWench(),
		new Buchaneer(), new Buchaneer(),
		new PirateKing()
	};
	
	private static final DeckCard[] robots = {
		new MicrobotGuard(), new MicrobotArchive(), new MicrobotAlpha(),
		new Zapbot(), new Zapbot(),
		new Warbot(), new Warbot(),
		new Nukebot()
	};
	
	private static final DeckCard[] tricksters = {
		new Gremlin(), new Gremlin(), new Gremlin(),
		new Gnome(), new Gnome(),
		new Brownie(), new Brownie(),
		new Leprechaun()
	};
	
	private static final DeckCard[] wizards = {
		new Neophyte(), new Neophyte(), new Neophyte(),
		new Chronomage(), new Chronomage(),
		new Archmage()
	};
	
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
		case Aliens: return aliens;
		case Dinosaurs: return dinosaurs;
		case Ninjas: return ninjas;
		case Pirates: return pirates;
		case Robots: return robots;
		case Tricksters: return tricksters;
		case Wizards: return wizards;
		case Zombies: return zombies();
		}
		
		throw new RuntimeException("no such faction");
	}
}
