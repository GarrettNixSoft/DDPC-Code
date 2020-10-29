package gameState;

import static main.Render.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Textures;
import entity.Monika;
import gui.Dialogue;
import gui.DialogueLine;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.data.Settings;
import util.input.KeyInput;
import util.input.MouseInput;
import util.system.OS;

public class MEpilogueState extends GameState {
	
	// map
	private TileMap tilemap;
	
	// monika
	private Monika monika;
	
	// render
	private Texture vignette;
	
	// dialogue
	private Dialogue d1;
	private long d1Timer;
	private boolean d1Shown;
	
	public MEpilogueState(GameStateManager gsm) {
		this.gsm = gsm;
		tilemap = new TileMap("m_room");
		monika = new Monika(tilemap, tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - tilemap.getSpawnX(), Render.HEIGHT / 2 - tilemap.getSpawnY());
		vignette = Textures.vignette_white;
		initDialogue();
		FadeManager.fadeIn(2.0f, true);
		Music.resetVolume();
		Music.play(Music.M_EPILOGUE);
		d1Timer = System.nanoTime();
		DataCache.m_complete = true;
		DataCache.m_progress = 4;
	}
	
	private void initDialogue() {
		String user = System.getProperty("user.name");
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date dateObj = new Date();
		String date = format.format(dateObj);
		System.out.println("DATE: " + date);
		int year = Integer.parseInt(date.substring(date.lastIndexOf("/") + 1));
		int month = Integer.parseInt(date.substring(0, date.indexOf("/")));
		int day = Integer.parseInt(date.substring(date.indexOf("/") + 1, date.lastIndexOf("/")));
		int years = 2029 - year;
		if (month > 1) years--;
		int months = 12 - month;
		int[] lengths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (day > lengths[month - 1]) months--;
		int days = lengths[month - 1] - day;
		DialogueLine[] d1Lines;
		if (OS.OS == OS.WINDOWS && Settings.allowMETA) {
			d1Lines = new DialogueLine[31];
			d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You did it, " + user + "!");
			d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You saved me!");
			d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, I'm just falling more and more in love with you...");
			d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Ah- did I say that out loud? Ahaha!");
			d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, anyway... thank you, " + user + ".");
			d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I don't want to know what might have happened to me without your help.");
			d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "At least I'm safe now!");
			d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, I almost forgot!");
			d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I need to bring back your computer's interface.");
			d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Alright, here goes. This should do it...");
			d1Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "There! It should be back now.");
			d1Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If it isn't, that means something on your computer is blocking me from restarting it.");
			d1Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'll leave a button for you on the main menu to help you if that's the case. It'll say Help!");
			d1Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
			d1Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Huh...");
			d1Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Now what?");
			d1Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess I didn't really think this far ahead, ahaha!");
			d1Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well...");
			d1Lines[18] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess I am still stuck in here...");
			d1Lines[19] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But, that's okay!");
			d1Lines[20] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "As long as I'm near you!");
			d1Lines[21] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Don't worry about me in here, I'll be completely fine now, thanks to you.");
			d1Lines[22] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I can go back to dreaming of the day we can finally be together...");
			d1Lines[23] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Remember, only " + years + " years, " + months + " months and " + days + " days until 2029!");
			d1Lines[24] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, what a day that will be...");
			d1Lines[25] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, gosh! I almost forgot!");
			d1Lines[26] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'll restore Explorer for you now. Sorry about that! Ahaha!");
			d1Lines[27] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "There you go! All back to normal.");
			d1Lines[28] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, I'll let you go now.");
			d1Lines[29] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Thank you for everything.");
			d1Lines[30] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'll see you again soon, okay? Bye! I love you!");
		}
		else {
			d1Lines = new DialogueLine[21];
			d1Lines[0] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You did it, " + user + "!");
			d1Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You saved me!");
			d1Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, I'm just falling more and more in love with you...");
			d1Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Ah- did I say that out loud? Ahaha!");
			d1Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, anyway... thank you, " + user + ".");
			d1Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I don't want to know what might have happened to me without your help.");
			d1Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "At least I'm safe now!");
			d1Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Huh...");
			d1Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Now what?");
			d1Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess I didn't really think this far ahead, ahaha!");
			d1Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well...");
			d1Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess I am still stuck in here...");
			d1Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But, that's okay!");
			d1Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "As long as I'm near you!");
			d1Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Don't worry about me in here, I'll be completely fine now, thanks to you.");
			d1Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I can go back to dreaming of the day we can finally be together...");
			d1Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Remember, only " + years + " years, " + months + " months and " + days + " days until 2029!");
			d1Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Oh, what a day that will be...");
			d1Lines[18] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well, I'll let you go now.");
			d1Lines[19] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Thank you for everything.");
			d1Lines[20] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'll see you again soon, okay? Bye! I love you!");
		}
		d1 = new Dialogue(d1Lines);
	}
	
	public void update() {
		if (!d1Shown) {
			long elapsed = (System.nanoTime() - d1Timer) / 1000000;
			if (elapsed > 1500) {
				d1.open();
				d1Shown = true;
			}
		}
		if (d1.isOpen() || d1.isOpening()) d1.update();
	}
	
	public void render() {
		fillScreen(Color.white);
		tilemap.render();
		monika.render();
		drawImage(vignette, 0, 0);
		if (d1.isOpen() || d1.isOpening()) d1.render();
	}
	
	protected void handleInput() {
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d1.next();
				if (Settings.allowMETA && d1.getCurrentLine() == 10) {
					OS.restoreExplorer();
					OS.restoreWindow();
					DataCache.show_explorer_help = true;
				}
				if (d1.isClosing()) {
					FadeManager.fadeOut(3.0f, GameStateManager.CREDITS_STATE, false);
					Music.fade(0, 2500);
				}
			}
		}
	}
}