package unused;

import java.util.ArrayList;

import effects.DynamicEffect;
import entity.Player;
import tile.TileMap;

public class EffectsManager {
	
	// assets
	private TileMap tilemap;
	private Player player;
	
	// effect IDs
	public static final int BLAST = 0;
	public static final int TELEPORT = 1;
	public static final int VANISH = 2;
	
	// effects
	private ArrayList<DynamicEffect> effects;
	
	public EffectsManager(TileMap tilemap, Player player) {
		this.tilemap = tilemap;
		this.player = player;
		effects = new ArrayList<DynamicEffect>();
	}
	
	public void playEffect(int effect) {
		switch (effect) {
			case BLAST:
				break;
			case TELEPORT:
				TeleportEffect t = new TeleportEffect(tilemap, player, (int) player.getX(), (int) player.getY());
				effects.add(t);
				break;
			case VANISH:
				break;
		}
	}
	
	public void update() {
		for (int i = 0; i < effects.size(); i++) {
			DynamicEffect v = effects.get(i);
			v.update();
			if (v.remove()) {
				effects.remove(i);
				i--;
			}
		}
	}
	
	public void render() {
		for (DynamicEffect v : effects) v.render();
	}
}