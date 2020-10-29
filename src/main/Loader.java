package main;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.openal.AL10;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.OpenALStreamPlayer;
import org.newdawn.slick.openal.StreamSound;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import assets.Fonts;
import assets.Music;
import assets.Sfx;
import assets.SoundManager;
import assets.Textures;
import gui.DialogueEffect;
import gui.DialogueLine;

public class Loader {
	
	public static void loadAssets() {
		PrintStream defaultStream = System.out;
		PrintStream dummyStream = new PrintStream(new OutputStream() {
			public void write(int b) {
				// nothing
			}
		});
		System.setOut(dummyStream);
		Textures.load();
		Music.load();
		SoundManager.load();
		Sfx.load();
		Fonts.load();
		System.setOut(defaultStream);
	}
	
	public static Texture loadTexture(String folder, String name) {
		Texture texture = null;
		InputStream in = ResourceLoader.getResourceAsStream("res/textures/" + folder + "/" + name + ".png");
		try {
			texture = TextureLoader.getTexture("PNG", in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	public static Texture loadTextureTGA(String folder, String name) {
		//Render.drawRect(-1, -1, Render.WIDTH, Render.HEIGHT, Color.white);
		//Display.update();
		Texture texture = null;
		InputStream in = ResourceLoader.getResourceAsStream("res/textures/" + folder + "/" + name + ".tga");
		try {
			texture = TextureLoader.getTexture("TGA", in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return texture;
	}
	
	public static Texture[] loadSprites(String name) {
		//Render.drawRect(-1, -1, Render.WIDTH, Render.HEIGHT, Color.white);
		//Display.update();
		BufferedImage sheet = null;
		BufferedImage[] frames = null;
		try {
			// load sheet
			sheet = ImageIO.read(Render.class.getResourceAsStream("/res/textures/sprites/" + name + ".png"));
			frames = new BufferedImage[sheet.getWidth() / 60];
			// extract frames
			for (int i = 0; i < frames.length; i++) {
				frames[i] = sheet.getSubimage(i * 60, 0, 60, 80);
			}
			// convert to Texture objects
			Texture[] sprites = new Texture[frames.length];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = BufferedImageUtil.getTexture("", frames[i]);
			}
			return sprites;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Texture[] loadSprites(String name, int width, int height) {
		BufferedImage sheet = null;
		BufferedImage[] frames = null;
		try {
			// load sheet
			sheet = ImageIO.read(Render.class.getResourceAsStream("/res/textures/sprites/" + name + ".png"));
			frames = new BufferedImage[sheet.getWidth() / width];
			// extract frames
			for (int i = 0; i < frames.length; i++) {
				frames[i] = sheet.getSubimage(i * width, 0, width, height);
			}
			// convert to Texture objects
			Texture[] sprites = new Texture[frames.length];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = BufferedImageUtil.getTexture("", frames[i]);
			}
			return sprites;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Texture[] loadSprites(String folder, String name, int width, int height) {
		BufferedImage sheet = null;
		BufferedImage[] frames = null;
		try {
			// load sheet
			sheet = ImageIO.read(Render.class.getResourceAsStream("/res/textures/" + folder + "/" + name + ".png"));
			frames = new BufferedImage[sheet.getWidth() / width];
			// extract frames
			for (int i = 0; i < frames.length; i++) {
				frames[i] = sheet.getSubimage(i * width, 0, width, height);
			}
			// convert to Texture objects
			Texture[] sprites = new Texture[frames.length];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = BufferedImageUtil.getTexture("", frames[i]);
			}
			return sprites;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Texture[] loadGlitchSprites(String name) {
		Texture[] textures = new Texture[16];
		for (int i = 0; i < textures.length; i++) {
			textures[i] = loadTexture("sprites/expansion/glitch", name + "_" + i);
		}
		return textures;
	}
	
	public static Texture[] loadGlitchTextures(String folder, String name) {
		Texture[] textures = new Texture[16];
		for (int i = 0; i < textures.length; i++) {
			textures[i] = loadTexture(folder, name + "_" + i);
		}
		return textures;
	}
	
	public static Texture[][] loadTileset(String name, int tileSize) {
		BufferedImage sheet = null;
		BufferedImage[][] tiles = null;
		try {
			// load sheet
			sheet = ImageIO.read(Render.class.getResourceAsStream("/res/textures/tilesets/" + name + ".png"));
			tiles = new BufferedImage[sheet.getHeight() / tileSize][sheet.getWidth() / tileSize];
			// extract tiles
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[i].length; j++) {
					tiles[i][j] = sheet.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize);
				}
			}
			// convert to Texture objects
			Texture[][] tileset = new Texture[tiles.length][tiles[0].length];
			System.out.println("tileset rows: " + tiles.length);
			System.out.println("tileset cols: " + tiles[0].length);
			for (int i = 0; i < tileset.length; i++) {
				for (int j = 0; j < tileset[i].length; j++) {
					tileset[i][j] = BufferedImageUtil.getTexture("", tiles[i][j]);
				}
			}
			return tileset;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Audio loadAudio(String folder, String name) {
		try {
			return AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sfx/" + folder + "/" + name + ".wav"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static org.newdawn.slick.Music loadMusic(String name) {
		try {
			return new org.newdawn.slick.Music("res/music/" + name + ".wav");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Audio loadMusicAudio(String name) {
		try {
			return AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/music/" + name + ".wav"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static StreamSound loadMusicStream(String name) {
		try {
			int source = AL10.alGenSources();
			OpenALStreamPlayer streamPlayer = new OpenALStreamPlayer(source, "res/music/ogg/" + name + ".ogg");
			return new StreamSound(streamPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static UnicodeFont loadFont(String name, int size) {
		try {
	        InputStream inputStream = ResourceLoader.getResourceAsStream("res/fonts/" + name + ".ttf");
	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        awtFont = awtFont.deriveFont(size); // set font size
	        UnicodeFont result = new UnicodeFont(awtFont, size, false, false);
	        result.getEffects().add(new ColorEffect(java.awt.Color.white));
	        result.addNeheGlyphs();
	        result.loadGlyphs();
	        return result;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static UnicodeFont loadDefaultFont(String name, int type, int size) {
		try {
			Font f = new Font(name, type, size);
			UnicodeFont result = new UnicodeFont(f);
	        result.getEffects().add(new ColorEffect(java.awt.Color.white));
	        result.addNeheGlyphs();
	        result.loadGlyphs();
	        return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Font loadAwtFont(String name, int size) {
		try {
	        InputStream inputStream = ResourceLoader.getResourceAsStream("res/fonts/" + name + ".ttf");
	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        awtFont = awtFont.deriveFont(size); // set font size
	        return awtFont;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static Font loadDefaultAwtFont(String name, int type, int size) {
		Font f = new Font(name, type, size);
		return f;
	}
	
	public static DialogueLine[] loadDialogue(String file) {
		DialogueLine[] lines = null;
		try {
			InputStream in = Loader.class.getResourceAsStream("/dialogue/" + file + ".dlg");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			ArrayList<String> data = new ArrayList<String>();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				data.add(line);
			}
			lines = new DialogueLine[data.size()];
			for (int i = 0; i < lines.length; i++) {
				String str = data.get(i);
				String[] tokens = new String[2];
				tokens[0] = str.substring(0, str.indexOf(" "));
				tokens[1] = str.substring(str.indexOf(" ") + 1);
				String name = "";
				String line = "";
				Texture face = null;
				boolean auto = false;
				if (tokens[0].startsWith("*")) {
					auto = true;
					tokens[0] = tokens[0].substring(1);
				}
				// get name
				if (tokens[0].equals("m")) {
					name = "Monika";
					face = Textures.dialogue_faces[3];
				}
				else if (tokens[0].equals("s")) {
					name = "Sayori";
					face = Textures.dialogue_faces[0];
				}
				else if (tokens[0].equals("n")) {
					name = "Natsuki";
					face = Textures.dialogue_faces[1];
				}
				else if (tokens[0].equals("y")) {
					name = "Yuri";
					face = Textures.dialogue_faces[2];
				}
				else if (tokens[0].equals("mr")) {
					name = "Mirror";
					face = Textures.dialogue_faces[5];
				}
				else if (tokens[0].equals("mr?")) {
					name = "???";
					face = Textures.dialogue_faces[5];
				}
				else if (tokens[0].equals("f")) {
					name = "Floober";
					face = Textures.dialogue_faces[4];
				}
				// add user name
				String user = System.getProperty("user.name");
				tokens[1] = tokens[1].replace("[user]", user);
				// check for effect
				DialogueEffect effect = null;
				String[] lineTokens = {};
				try {
					lineTokens = tokens[1].substring(tokens[1].lastIndexOf("\"") + 2).split(" ");
				} catch (StringIndexOutOfBoundsException se) {
					// do nothing
				}
				// get line
				line = tokens[1].substring(0, tokens[1].lastIndexOf("\"") + 1);
				//for (String s : lineTokens) System.out.println("[Loader] lineToken = " + s);
				//System.out.println("[Loader] ***");
				if (lineTokens.length > 1) {
					if (lineTokens[0].equals("glitch_face")) {
						// character glitch
						effect = new DialogueEffect(DialogueEffect.CHARACTER);
						// get glitch textures
						if (lineTokens[1].equals("sayori")) effect.setEffect(Textures.dialogue_faces_glitch[0]);
						else if (lineTokens[1].equals("natsuki")) effect.setEffect(Textures.dialogue_faces_glitch[1]);
						else if (lineTokens[1].equals("yuri")) effect.setEffect(Textures.dialogue_faces_glitch[2]);
						else if (lineTokens[1].equals("monika")) effect.setEffect(Textures.dialogue_faces_glitch[3]);
						else if (lineTokens[1].equals("mirror")) effect.setEffect(Textures.dialogue_faces_glitch[4]);
						// get duration
						float duration = Float.parseFloat(lineTokens[2]);
						effect.setDuration((int) (duration * 1000));
					}
					else if (lineTokens[0].equals("glitch_text")) {
						effect = new DialogueEffect(DialogueEffect.DIALOGUE);
						int frequency = Integer.parseInt(lineTokens[1]);
						effect.setFrequency(frequency);
						effect.setLength(line.length());
					}
				}
				// set up line
				lines[i] = new DialogueLine(face, name, line);
				if (effect != null) lines[i].setEffect(effect);
				if (auto) lines[i].setAuto();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
}