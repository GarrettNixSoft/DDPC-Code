package gameState;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import assets.Music;
import credits.CenteredTextElement;
import credits.CreditRollElement;
import credits.CreditsElement;
import credits.Delay;
import credits.ScrollingElement;
import credits.ScrollingParagraphElement;
import credits.ScrollingSectionElement;
import util.FadeManager;
import util.data.DataCache;
import util.data.SaveData;
import util.input.KeyInput;

public class CreditsState extends GameState {
	
	private ArrayList<CreditsElement> elements;
	private int currentElement;
	private boolean complete;
	
	// timing
	private long timer;
	private boolean started;
	
	public CreditsState(GameStateManager gsm) {
		this.gsm = gsm;
		elements = new ArrayList<CreditsElement>();
		elements.add(new CenteredTextElement(new String[] {
				"Doki Doki Platformer Club!",
				"by Floober101"
		}, Color.white, 2500, 1100));
		elements.add(new Delay(200));
		elements.add(new CenteredTextElement(new String[] {
				"Thank you for playing my fan game.",
				"I hope you enjoyed it!"
		}, Color.white, 2500, 1100));
		elements.add(new Delay(200));
		elements.add(new CenteredTextElement(new String[] {
				"Making this game has been the most",
				"fun experience I've had in a long time.",
				"I've learned so much, and I'm really proud",
				"of how this little project turned out."
		}, Color.white, 4500, 1100));
		elements.add(new Delay(200));
		elements.add(new CenteredTextElement(new String[] {
				"But of course, I didn't make this alone.",
				"I want to thank everyone who let me",
				"use their amazing work to make this",
				"game all that it could be."
		}, Color.white, 4500, 1100));
		elements.add(new Delay(200));
		elements.add(new CenteredTextElement(new String[] {
				"And I'd also like to thank all the wonderful",
				"people in the DDLC community who I've",
				"had the chance to meet and interact with.",
				"You're all awesome. <3"
		}, Color.white, 4500, 1100));
		elements.add(new Delay(200));
		ScrollingSectionElement s1 = new ScrollingSectionElement("Music/Sfx", new String[] {
				"Menu Music",
				"Sayori Level Music",
				"Natsuki Level Music",
				"Yuri Level Music",
				"Monika Final Level Music",
				"Level Win Music",
				"Sayori End Music",
				"Collapse Level Music",
				"Monika Epilogue Music",
				"Credits Music",
				"Thunder Loop",
				"Thunder Clap",
				"Static Noise",
				"Slice Sound"
		}, new String[] {
				"MyNewSoundTrack",
				"3Pills",
				"Dan Salvato",
				"Visager",
				"3Pills",
				"3Pills",
				"Visager",
				"Visager",
				"Visager",
				"Visager",
				"DoKashiteru (OpenGameArt)",
				"FreqMan (freesound.org)",
				"WaveLynx (freesound.org)",
				"SoundEffectsFactory (youtube)"
		});
		ScrollingSectionElement s2 = new ScrollingSectionElement("Game Design", new String[] {
				"Programming",
				"Levels",
				"Writing",
				"Level Y4"
		}, new String[] {
				"Floober101",
				"Floober101",
				"Floober101",
				"TragicUnicorn"
		});
		ScrollingSectionElement s3 = new ScrollingSectionElement("Sprites/Textures", new String[] {
				"Base Character Sprites",
				"Spider Sprites",
				"Background Clouds",
				"Forest Background",
				"Space Background (Collapse)"
		}, new String[] {
				"JackSunslight",
				"Reemax (OpenGameArt)",
				"Guido Bos (OpenGameArt)",
				"AhNinniah (OpenGameArt)",
				"Westbeam (OpenGameArt)"
		});
		ScrollingParagraphElement s4 = new ScrollingParagraphElement(new String[] {
				"Special thanks to u/TragicUnicorn and",
				"u/Pacmantaco for encouraging me throughout",
				"the development process. You guys helped",
				"me a lot, more than you know. Love ya <3"
		});
		ScrollingParagraphElement s5 = new ScrollingParagraphElement(new String[] {
				"Now, I figured I'd put a little section",
				"in here to shoutout as many awesome people",
				"as I can, just for funsies. In no particular",
				"order, of course! (And if you're not in the",
				"list, don't worry, you're still awesome!)"
		});
		ScrollingSectionElement s6 = new ScrollingSectionElement("Awesome People!", new String[] {
				"u/TragicUnicorn",
				"u/Pacmantaco",
				"u/OssiPap",
				"u/NemesisAtlas",
				"u/Cradlax",
				"u/TwixsterTheTrickster",
				"u/zatask",
				"u/MrAppleSpiceMan",
				"u/snakegarringer",
				"u/TheBurningPhoenix1",
				"u/ItsAKangasaur",
				"u/Tonyukuk-Ashide",
				"u/SandwichCutter",
				"u/TurretBot",
				"u/TheBloodyPuppet_2",
				"u/Th3_Shr00m",
				"u/spartanboi96",
				"u/SlushieOfHats",
				"u/JamesAffRedd17",
				"u/Williekins",
				"u/Seanofthedead64",
				"u/-ZeroL-",
				"u/AmericanTeaLover",
				"u/RandomCockAsian",
				"u/ArmyOfAaron",
				"u/theseconddennis",
				"u/MiximumDennis",
				"u/QuackersEncheese",
				"u/Isra_Redfield",
				"u/speaker96",
				"u/FailSandwich",
				"u/lil-mister-universe",
				"u/xDarkWind7",
				"u/o--3-o",
				"u/Shizen1448",
				"u/ProtectAllThatIsGood",
				"u/Spar-kie",
				"u/woutmees",
				"If I made a typo in anyone's name",
				"Also if you're not on the list"
		}, new String[] {
				"u/ProfessorHerb",
				"u/TheKrazyJames",
				"u/FrustratingDiplomacy",
				"u/zellexe",
				"u/Steve_Must_Die",
				"u/Manfred_Danfred",
				"u/Overlord166",
				"u/Tskool",
				"u/Sonic9103",
				"u/Fwort",
				"u/RivalXL",
				"u/priviolette",
				"u/Squatori",
				"u/MasterOfMundus",
				"u/Fairclouds",
				"u/Sheevo_RM",
				"u/Eira9601",
				"u/Steven-The-GOAT",
				"u/AceologyGaming",
				"u/left4deadify",
				"u/AgentJohn20",
				"u/justsomerandomyguy",
				"u/Lethalmilk",
				"u/Taffin_Blur",
				"u/OliverCNorton",
				"u/AngeloTheAngelo",
				"u/MrMutlu",
				"u/seboostian_I_art",
				"u/maximuffin2",
				"u/chrislaf",
				"u/Athelas7",
				"u/JustRandomUsr",
				"u/sexy_spanish_name",
				"u/OldSoulja",
				"u/paulchartres",
				"u/KiraMarinheart",
				"u/DestinyPvEGal",
				"",
				"I'M SUPER SORRY",
				"I still love you <3"
		});
		ScrollingElement[] sections = {s1, s2, s3, s4, s5, s6};
		elements.add(new CreditRollElement(sections));
		elements.add(new CenteredTextElement(new String[] {
				"Special thanks to Dan Salvato for creating",
				"such a fantastic game. It changed my life,",
				"and brought together a wonderful community."
		}, Color.white));
		elements.add(new Delay(1000));
		timer = System.nanoTime();
		DataCache.m_complete = true;
		System.out.println("unlocked");
		SaveData.save();
	}
	
	private void start() {
		Music.resetVolume();
		Music.playOnce(Music.CREDITS);
		elements.get(0).start();
		started = true;
	}
	
	public void update() {
		if (!started) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > 2000) start();
			else return;
		}
		if (complete) return;
		if (elements.get(currentElement).finished()) {
			currentElement++;
			if (currentElement == elements.size()) {
				complete = true;
				FadeManager.fadeOut(2.0f, GameStateManager.MENU_STATE, true);
				Music.fade(0, 2000);
			}
			else elements.get(currentElement).start();
		}
		else elements.get(currentElement).update();
	}
	
	public void render() {
		if (complete || !started) return;
		elements.get(currentElement).render();
	}
	
	protected void handleInput() {
		if (KeyInput.isPressed(KeyInput.ESC)) gsm.setState(GameStateManager.MENU_STATE);
	}
}