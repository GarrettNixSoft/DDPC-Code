package minigame;

import assets.Textures;
import effects.entity.GlitchRandom;
import effects.entity.ShakeEffect;
import entity.util.GlitchAnimation;
import org.newdawn.slick.opengl.Texture;
import tile.TileMap;

public class GhostMonika extends GhostYuri {


	public GhostMonika(TileMap tm) {
		super(tm);
		idleFrames = Textures.m_ghost_idle;
		walkFrames = Textures.m_ghost_walk;
		jumpFrames = Textures.m_ghost_jump;
		fallFrames = Textures.m_ghost_fall;
		attackFrames = Textures.m_ghost_attack;
		// init glitched sprites
		idleGlitchFrames = Textures.m_ghost_idle_glitch;
		walkGlitchFrames = Textures.m_ghost_walk_glitch;
		jumpGlitchFrames = Textures.m_ghost_jump_glitch;
		fallGlitchFrames = Textures.m_ghost_fall_glitch;
		attackGlitchFrames = Textures.m_ghost_attack_glitch;
		animation = new GlitchAnimation();
		animation.setGlitchFrames(idleGlitchFrames);
		animation.setGlitchDelay(100);
		animation.setFrames(idleFrames);
		glitch = new GlitchRandom(animation, new ShakeEffect());
		// CHECK ALL
//		System.out.println("idleFrames has null: " + containsNull(idleFrames));
//		System.out.println("walkFrames has null: " + containsNull(walkFrames));
//		System.out.println("jumpFrames has null: " + containsNull(jumpFrames));
//		System.out.println("fallFrames has null: " + containsNull(fallFrames));
//		System.out.println("attackFrames has null: " + containsNull(attackFrames));
//		System.out.println("idleGlitchFrames has null: " + containsNull(idleGlitchFrames));
//		System.out.println("walkGlitchFrames has null: " + containsNull(walkGlitchFrames));
//		System.out.println("jumpGlitchFrames has null: " + containsNull(jumpGlitchFrames));
//		System.out.println("fallGlitchFrames has null: " + containsNull(fallGlitchFrames));
//		System.out.println("attackGlitchFrames has null: " + containsNull(attackGlitchFrames));
	}

	private boolean containsNull(Texture[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) return true;
		}
		return false;
	}

	private boolean containsNull(Texture[][] array) {
		for (int i = 0; i < array.length; i++) {
			if (containsNull(array[i])) return true;
		}
		return false;
	}
}
