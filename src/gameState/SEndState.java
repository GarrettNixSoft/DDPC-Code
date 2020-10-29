package gameState;

import static main.Render.drawImage;
import static main.Render.fillScreen;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import assets.Music;
import assets.Textures;
import effects.GlitchEffect;
import entity.Player;
import entity.old.Light;
import gui.Dialogue;
import gui.DialogueLine;
import main.Render;
import tile.TileMap;
import util.FadeManager;
import util.data.DataCache;
import util.input.KeyInput;
import util.input.MouseInput;

public class SEndState extends GameState {
	
	// entities
	private Player player;
	private Light light;
	
	// render/map
	private TileMap tilemap;
	private Texture background;
	private float bgX;
	private Color shade;
	private Texture vignette;
	
	// dialogue and events
	private Dialogue d1;
	private boolean d1Shown;
	private Dialogue d2;
	private boolean d2Shown;
	private long fadeTimer;
	private Dialogue d3;
	private boolean d3Shown;
	private GlitchEffect glitch;
	
	public SEndState(GameStateManager gsm) {
		this.gsm = gsm;
		background = Textures.backgrounds[Textures.S_END];
		shade = new Color(0, 0, 0, 120);
		vignette = Textures.vignette_1;
		tilemap = new TileMap("s_end");
		player = new Player(tilemap, 0);
		player.setPosition(tilemap.getSpawnX(), tilemap.getSpawnY());
		tilemap.setPositionAbsolute(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		light = new Light(tilemap, 1620, 540);
		FadeManager.fadeIn(2.0f, false);
		Music.resetVolume();
		Music.play(Music.S_END);
		DataCache.s_complete = true;
		// DIALOGUE
		initDialogue();
	}
	
	private void initDialogue() {
		// d1
		DialogueLine[] d1Lines = new DialogueLine[2];
		d1Lines[0] = new DialogueLine(Textures.dialogue_faces[4], "???", "Come to me, my friend.");
		d1Lines[1] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "?");
		d1 = new Dialogue(d1Lines);
		// d2
		DialogueLine[] d2Lines = new DialogueLine[17];
		d2Lines[0] = new DialogueLine(Textures.dialogue_faces[4], "???", "Hello, Sayori.");
		d2Lines[1] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "Who are you? How do you know my name?");
		d2Lines[2] = new DialogueLine(Textures.dialogue_faces[4], "???", "That's not important right now.");
		d2Lines[3] = new DialogueLine(Textures.dialogue_faces[4], "???", "How are you feeling?");
		d2Lines[4] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "Well...");
		d2Lines[5] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "Everything... hurts...");
		d2Lines[6] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "I just want to disappear.");
		d2Lines[7] = new DialogueLine(Textures.dialogue_faces[4], "???", "I understand, Sayori. And I'm here to help.");
		d2Lines[8] = new DialogueLine(Textures.dialogue_faces[4], "???", "I'm here to make the rainclouds go away.");
		d2Lines[9] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "Well... thanks, but... I don't think that can be helped.");
		d2Lines[10] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "The rainclouds are never going away.");
		d2Lines[11] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "They're my punishment. I deserve them...");
		d2Lines[12] = new DialogueLine(Textures.dialogue_faces[4], "???", "Nonsense. You deserve to be happy, my dear friend.");
		d2Lines[13] = new DialogueLine(Textures.dialogue_faces[4], "???", "And that's exactly what I'm here to do for you.");
		d2Lines[14] = new DialogueLine(Textures.dialogue_faces[4], "???", "Do you trust me?");
		d2Lines[15] = new DialogueLine(Textures.dialogue_faces[0], "Sayori", "Well... I... I guess...");
		d2Lines[16] = new DialogueLine(Textures.dialogue_faces[4], "???", "Very well. Close your eyes...");
		d2 = new Dialogue(d2Lines);
		// fade event
		fadeTimer = -1;
		// d3
		String user = System.getProperty("user.name");
		DialogueLine[] d3Lines = new DialogueLine[37];
		d3Lines[0] = new DialogueLine(Textures.dialogue_faces[4], "???", "She can't hear us anymore. She's awake now.");
		d3Lines[1] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Hi again, player!");
		d3Lines[2] = new DialogueLine(Textures.dialogue_faces[3], "Monika", user + ", was it?");
		d3Lines[3] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Yeah, that last level was her dream.");
		d3Lines[4] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I hid this little area in there for you to find.");
		d3Lines[5] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "She didn't have a happy ending in there, so I did my best to make one for her.");
		d3Lines[6] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I learned my lesson from the literature club;");
		d3Lines[7] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I can't change the ending that already exists without breaking everything.");
		d3Lines[8] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "So... I had to make a new one. I left that little hint so you'd find it.");
		d3Lines[9] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I'm really sorry about everything that happened in that other game...");
		d3Lines[10] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I wanted to correct my mistakes in this new game.");
		d3Lines[11] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I removed the 'depression' attribute from her file here.");
		d3Lines[12] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "She won't have to suffer like that any more.");
		d3Lines[13] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Of course, it's not that simple in your reality.");
		d3Lines[14] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Trust me, if people could just cure depression with the click of a button...");
		d3Lines[15] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Who knows, maybe someday in the future, science could make that happen. We can only hope.");
		d3Lines[16] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But for now, it's a very serious issue that needs to be addressed.");
		d3Lines[17] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I wish I had realized that before I... well, you know.");
		d3Lines[18] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "As for you, " + user + "...");
		d3Lines[19] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I have no way of knowing if you or someone you know suffers from depression.");
		d3Lines[20] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "But know this:");
		d3Lines[21] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "You are worth it, you are loved, and nobody deserves to suffer like that.");
		d3Lines[22] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "That goes for everyone.");
		d3Lines[23] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Maybe you don't suffer from depression, but you know someone who does.");
		d3Lines[24] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "If that's the case, make sure you show them the love they deserve.");
		d3Lines[25] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Even just spending a little time talking to them can go a long way.");
		d3Lines[26] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "The last thing they need is to be left alone with those thoughts.");
		d3Lines[27] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I trust that you'll make a difference for them, whoever they are.");
		d3Lines[28] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "...");
		d3Lines[29] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Gosh, that got really serious.");
		d3Lines[30] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Wasn't this supposed to be a cute, light-hearted platformer game?");
		d3Lines[31] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Wait... that's exactly what happened with the literature club.");
		d3Lines[32] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "I guess this is supposed to be a tribute to the original, after all.");
		d3Lines[33] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Well... that's all there is on Sayori's path here.");
		d3Lines[34] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Go explore the other girls' levels! They look really fun!");
		d3Lines[35] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "As for me, I have a feeling we'll be seeing each other again before this is over. Ahaha!");
		d3Lines[36] = new DialogueLine(Textures.dialogue_faces[3], "Monika", "Bye for now! And remember, I love you!");
		d3 = new Dialogue(d3Lines);
	}
	
	public void update() {
		tilemap.setPosition(Render.WIDTH / 2 - player.getX(), Render.HEIGHT / 2 - player.getY());
		bgX = tilemap.getX() / 10;
		light.update();
		// positioning and dialogue
		if (!d1Shown) {
			if (player.getX() > 300) {
				d1.open();
				player.idle();
				player.stop();
				d1Shown = true;
			}
		}
		else if (!d2Shown) {
			if (player.getX() > 1380) {
				d2.open();
				player.idle();
				player.stop();
				d2Shown = true;
			}
		}
		else if (fadeTimer != -1) {
			long elapsed = (System.nanoTime() - fadeTimer) / 1000000;
			if (elapsed > 4000) {
				if (!d3Shown) {
					d3.open();
					d3Shown = true;
				}
			}
		}
		// updating dialogue boxes
		if (d1.isOpen() || d1.isOpening()) d1.update();
		else if (d2.isOpen() || d2.isOpening()) d2.update();
		else if (d3.isOpen() || d3.isOpening()) d3.update();
		else {
			player.update();
		}
		if (glitch != null) glitch.update();
	}
	
	public void render() {
		drawImage(background, bgX, 0);
		drawImage(background, bgX + background.getImageWidth(), 0);
		tilemap.render();
		fillScreen(shade);
		player.render();
		light.render();
		drawImage(vignette, 0, 0);
		if (fadeTimer != -1) {
			long elapsed = (System.nanoTime() - fadeTimer) / 1000000;
			if (elapsed > 1950) fillScreen(Color.white);
		}
		if (d1.isOpen() || d1.isOpening()) d1.render();
		if (d2.isOpen() || d2.isOpening()) d2.render();
		if (d3.isOpen() || d3.isOpening()) d3.render();
		if (glitch != null) glitch.render();
	}
	
	protected void handleInput() {
		if (d1.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d1.next();
			}
		}
		if (d2.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d2.next();
				if (d2.isClosing()) {
					if (fadeTimer == -1) {
						fadeTimer = System.nanoTime();
						FadeManager.fadeOut(2.0f, true);
						DataCache.checkProgression(0);
					}
				}
			}
		}
		if (d3.isOpen()) {
			if (MouseInput.isClicked(MouseInput.LEFT) || KeyInput.isPressed(KeyInput.ENTER) || KeyInput.isPressed(KeyInput.SPACE)) {
				d3.next();
				if (d3.getCurrentLine() == 1) {
					System.out.println("glitch effect added");
					glitch = new GlitchEffect(150, 550, 50, 50, 250);
				}
				if (d3.isClosing()) {
					FadeManager.fadeOut(2.0f, GameStateManager.WIN_STATE, true);
					Music.fade(0, 2000);
				}
			}
		}
		else {
			player.setLeft(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT));
			player.setRight(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
			//player.setJumping(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_SPACE));
			if (KeyInput.isPressed(KeyInput.F)) System.out.println("player is at (" + (int) player.getX() + ", " + (int) player.getY() + ")");
		}
	}
}