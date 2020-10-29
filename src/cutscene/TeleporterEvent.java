package cutscene;

import entity.Teleporter;
import entity.Teleporter2Way;
import util.data.LevelCache;

public class TeleporterEvent extends CutsceneEvent {
	
	// types
	public static final int ONE_WAY = 0;
	public static final int TWO_WAY = 1;
	
	// data
	private LevelCache lc;
	private int type;
	private int x1, x2, y1, y2;
	
	// activation
	private boolean finished;
	
	public TeleporterEvent(LevelCache lc, int type, int[] coords) {
		this.lc = lc;
		this.type = type;
		x1 = coords[0];
		y1 = coords[1];
		x2 = coords[2];
		y2 = coords[3];
	}
	
	public void update() {
		Teleporter t = null;
		if (type == ONE_WAY) t = new Teleporter(lc.tilemap, x1, y1, x2, y2);
		else if (type == TWO_WAY) t = new Teleporter2Way(lc.tilemap, x1, y1, x2, y2);
		t.setOpen(false);
		t.open();
		lc.parent.getEntityHandler().addInteractive(t);
		finished = true;
	}
	
	public boolean finished() {
		return finished;
	}
}