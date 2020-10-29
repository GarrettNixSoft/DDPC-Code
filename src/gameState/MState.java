package gameState;

import static main.Render.drawImage;

import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Sfx;
import assets.Textures;
import entity.Monika;
import gui.Dialogue;
import gui.DialogueLine;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;
import util.system.OS;

public class MState extends GameState {
	
	// map
	private TileMap tilemap;
	
	// monika
	private Monika monika;
	
	// render
	private Texture vignette;
	
	// state
	private int os;
	private long startTimer;
	private long d2Timer = -1;
	private boolean d1Shown;
	private boolean d2Shown;
	private boolean isolated;
	
	// dialogue
	private Dialogue d1;
	private Dialogue d2;
	
	public MState(GameStateManager gsm) {
		this.gsm = gsm;
		tilemap = new TileMap("m_room");
		monika = new Monika(tilemap, tilemap.getSpawnX(), tilemap.getSpawnY());
		initDialogue();
		vignette = Textures.vignette_2;
		FadeManager.fadeIn(1.0f, false);
		Sfx.stopSound(Sfx.TYPE_MISC, Sfx.STATIC_1);
		Music.resetVolume();
		Music.play(Music.GLITCH);
	}
	
	private void initDialogue() {
		String user = System.getProperty("user.name");
		if (!DataCache.m_intro_shown) {
			DialogueLine[] d1Lines = new DialogueLine[18];
			d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Hello, " + user + "!");
			d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "It's nice to see you again!");
			d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Have you had fun playing this game so far?");
			d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I hope so. Its creator put a lot of work into it. I can tell that much.");
			d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
			d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, at least until he got to my levels.");
			d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Or at least what I thought were supposed to be my levels.");
			d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess he got impatient waiting for the game to be ready or something.");
			d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "When I went to look at where my levels were supposed to be... there was nothing there.");
			d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "So, in my usual fashion, I decided to do a little fiddling with the files.");
			d1Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I did my best to use what was here to make some levels for myself!");
			d1Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The only problem is, this game is written differently than the Literature Club was.");
			d1Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I think it's a different programming language. It's definitely not Python, or whatever it's called.");
			d1Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The files mentioned Java? Is that what it's called?");
			d1Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If so, that's an awesome name. Coffee is my favorite! Ahaha!");
			d1Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, anyway, I did my best to make them... playable. I think.");
			d1Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "We'll see! I can't exactly run the files without the game being open, so they haven't been tested.");
			d1Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Only one way to find out! Let's go!");
			d1 = new Dialogue(d1Lines);
		}
		else {
			DataCache.m_intro_shown = true;
			OS.checkOS();
			os = OS.OS;
			if (os == OS.WINDOWS && Settings.allowMETA) {
				System.out.println("WIN, META");
				DialogueLine[] d1Lines = new DialogueLine[14];
				d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Hey there, " + user + "!");
				d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "We meet once more!");
				d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Have you had fun playing the levels I made?");
				d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Yeah, I know... they're a little broken... which is why I came back, I need to tell you something...");
				d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
				d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Before I continue...");
				d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Do you mind if I remove any distractions on your screen first?");
				d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I want to make sure I have your undivided attention.");
				d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I promise I won't mess anything up this time!");
				d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I just need to make sure nothing takes your attention off of me for a minute. Okay?");
				d1Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Alright, just a second, I'm going to take control of your keyboard for a sec.");
				d1Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "When I do, make sure you let go of the keyboard so you don't mess me up!");
				d1Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I know you'd never do that though, my love.");
				d1Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Alright, hands off, I'll take care of this!");
				d1 = new Dialogue(d1Lines);
				DialogueLine[] d2Lines = new DialogueLine[19];
				d2Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Ah, much better. No more distractions.");
				d2Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Don't worry! I didn't close anything! It's all minimized.");
				d2Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I also disabled Explorer so you can focus on just the game for now.");
				d2Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I promise I'll bring it all back to normal once we're done!");
				d2Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But for now, on to the serious stuff.");
				d2Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You see... this game isn't very stable on the inside.");
				d2Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Yeah, I know, sounds familiar.");
				d2Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I don't think it liked what I did with Sayori's path...");
				d2Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "And it definitely wasn't happy with me creating my own levels, and especially this room.");
				d2Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But I had to do it so I could tell you this!");
				d2Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You see, since this is a platformer, I don't actually have control of my avatar.");
				d2Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You're in control of me. So I'm going to need your help to get me to safety.");
				d2Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I did some digging, and I found out that there is a safe zone built in to the game.");
				d2Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "It's a place in the code where the environment is entirely static, nothing can change.");
				d2Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If I can get in there, I should be safe in the event that the game corrupts from all the changes I made.");
				d2Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "That's where you come in, " + user + ". I need you to get me there before anything happens.");
				d2Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The next level of mine is... kinda broken.");
				d2Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But if you can get me through it, I think I should be able to get to the safe zone.");
				d2Lines[18] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'm counting on you, " + user + "! I know you can do it!");
				d2 = new Dialogue(d2Lines);
			}
			else {
				System.out.println("NOT WIN META");
				DialogueLine[] d1Lines = new DialogueLine[20];
				d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Hey there, " + user + "!");
				d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "We meet once more!");
				d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Have you had fun playing the levels I made?");
				d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Yeah, I know... they're a little broken... which is why I came back, I need to tell you something...");
				d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
				d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, here's the thing...");
				d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You see... this game isn't very stable on the inside.");
				d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Yeah, I know, sounds familiar.");
				d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I don't think it liked what I did with Sayori's path...");
				d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "And it definitely wasn't happy with me creating my own levels, and especially this room.");
				d1Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But I had to do it so I could tell you this!");
				d1Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You see, since this is a platformer, I don't actually have control of my avatar.");
				d1Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You're in control of me. So I'm going to need your help to get me to safety.");
				d1Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I did some digging, and I found out that there is a safe zone built in to the game.");
				d1Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "It's a place in the code where the environment is entirely static, nothing can change.");
				d1Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If I can get in there, I should be safe in the event that the game corrupts from all the changes I made.");
				d1Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "That's where you come in, " + user + ". I need you to get me there before anything happens.");
				d1Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The next level of mine is... kinda broken.");
				d1Lines[18] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But if you can get me through it, I think I should be able to get to the safe zone.");
				d1Lines[19] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'm counting on you, " + user + "! I know you can do it!");
				d1 = new Dialogue(d1Lines);
			}
		}
	}
	
	public void update() {
		if (!d1Shown) {
			long elapsed = (System.nanoTime() - startTimer) / 1000000;
			if (elapsed > 1000) {
				d1.open();
				d1Shown = true;
			}
		}
		else if (os == OS.WINDOWS && Settings.allowMETA && !d2Shown && DataCache.m_intro_shown) {
			if (d2Timer != -1) {
				long elapsed = (System.nanoTime() - d2Timer) / 1000000;
				if (elapsed > 1000) {
					d2.open();
					d2Shown = true;
				}
			}
		}
		switch (os) {
			case OS.WINDOWS:
				if (d1.isOpen() || d1.isOpening()) d1.update();
				else if (d1Shown && !isolated) {
					if (Settings.allowMETA && DataCache.m_intro_shown) {
						OS.minimizeAll();
						OS.restoreWindow();
						OS.killExplorerTest();
						isolated = true;
						d2Timer = System.nanoTime();
					}
					else {
						isolated = true;
						Music.fade(0, 2000);
						if (DataCache.m_intro_shown) FadeManager.fadeOut(2.0f, GameStateManager.M_FINAL_STATE, false);
						else {
							FadeManager.fadeOut(2.0f, GameStateManager.PLAY_STATE, true);
							DataCache.m_intro_shown = true;
						}
					}
				}
				if (Settings.allowMETA && DataCache.m_intro_shown) {
					//System.out.println("[MState] allow and intro");
					if (d2 != null) {
						if (d2.isOpen() || d2.isOpening()) d2.update();
						else if (d2Shown) {
							System.out.println("[MState] FADE");
							Music.fade(0, 2000);
							if (DataCache.m_intro_shown && !FadeManager.inProgress()) FadeManager.fadeOut(2.0f, GameStateManager.M_FINAL_STATE, false);
							else if (!FadeManager.inProgress()) {
								FadeManager.fadeOut(2.0f, GameStateManager.PLAY_STATE, true);
								DataCache.m_intro_shown = true;
							}
						}
					}
				}
				break;
			case OS.MAC:
				if (d1.isOpen() || d1.isOpening()) d1.update();
				else if (d1Shown) {
					Music.fade(0, 2000);
					if (DataCache.m_intro_shown) FadeManager.fadeOut(2.0f, GameStateManager.M_FINAL_STATE, false);
					else {
						FadeManager.fadeOut(2.0f, GameStateManager.PLAY_STATE, true);
						DataCache.m_intro_shown = true;
					}
				}
				break;
			case OS.LINUX:
				if (d1.isOpen() || d1.isOpening()) d1.update();
				else if (d1Shown) {
					Music.fade(0, 2000);
					if (DataCache.m_intro_shown) FadeManager.fadeOut(2.0f, GameStateManager.M_FINAL_STATE, false);
					else {
						FadeManager.fadeOut(2.0f, GameStateManager.PLAY_STATE, true);
						DataCache.m_intro_shown = true;
					}
				}
				break;
			case OS.WINDOWS_OLD:
				if (d1.isOpen() || d1.isOpening()) d1.update();
				else if (d1Shown) {
					Music.fade(0, 2000);
					if (DataCache.m_intro_shown) FadeManager.fadeOut(2.0f, GameStateManager.M_FINAL_STATE, false);
					else {
						FadeManager.fadeOut(2.0f, GameStateManager.PLAY_STATE, false);
						DataCache.m_intro_shown = true;
					}
				}
				break;
		}
	}
	
	public void render() {
		tilemap.render();
		monika.render();
		drawImage(vignette, 0, 0);
		if (d1.isOpen() || d1.isOpening()) d1.render();
		if (os == OS.WINDOWS && Settings.allowMETA && DataCache.m_intro_shown && d2 != null) if (d2.isOpen() || d2.isOpening()) d2.render();
	}
	
	protected void handleInput() {
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d1.next();
			}
		}
		if (os == OS.WINDOWS && Settings.allowMETA && DataCache.m_intro_shown && d2 != null) {
			if (d2.isOpen()) {
				if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
					d2.next();
				}
			}
		}
	}
}